package com.mycompany.sistemkepegawaian.web.rest;

import static com.mycompany.sistemkepegawaian.domain.CutiAsserts.*;
import static com.mycompany.sistemkepegawaian.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.sistemkepegawaian.IntegrationTest;
import com.mycompany.sistemkepegawaian.domain.Cuti;
import com.mycompany.sistemkepegawaian.domain.Pegawai;
import com.mycompany.sistemkepegawaian.repository.CutiRepository;
import com.mycompany.sistemkepegawaian.service.CutiService;
import com.mycompany.sistemkepegawaian.service.dto.CutiDTO;
import com.mycompany.sistemkepegawaian.service.mapper.CutiMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CutiResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CutiResourceIT {

    private static final LocalDate DEFAULT_TANGGAL_MULAI = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TANGGAL_MULAI = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TANGGAL_MULAI = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_TANGGAL_SELESAI = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TANGGAL_SELESAI = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TANGGAL_SELESAI = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_ALASAN = "AAAAAAAAAA";
    private static final String UPDATED_ALASAN = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cutis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CutiRepository cutiRepository;

    @Mock
    private CutiRepository cutiRepositoryMock;

    @Autowired
    private CutiMapper cutiMapper;

    @Mock
    private CutiService cutiServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCutiMockMvc;

    private Cuti cuti;

    private Cuti insertedCuti;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cuti createEntity(EntityManager em) {
        Cuti cuti = new Cuti()
            .tanggalMulai(DEFAULT_TANGGAL_MULAI)
            .tanggalSelesai(DEFAULT_TANGGAL_SELESAI)
            .alasan(DEFAULT_ALASAN)
            .status(DEFAULT_STATUS);
        // Add required entity
        Pegawai pegawai;
        if (TestUtil.findAll(em, Pegawai.class).isEmpty()) {
            pegawai = PegawaiResourceIT.createEntity();
            em.persist(pegawai);
            em.flush();
        } else {
            pegawai = TestUtil.findAll(em, Pegawai.class).get(0);
        }
        cuti.setPegawai(pegawai);
        return cuti;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cuti createUpdatedEntity(EntityManager em) {
        Cuti updatedCuti = new Cuti()
            .tanggalMulai(UPDATED_TANGGAL_MULAI)
            .tanggalSelesai(UPDATED_TANGGAL_SELESAI)
            .alasan(UPDATED_ALASAN)
            .status(UPDATED_STATUS);
        // Add required entity
        Pegawai pegawai;
        if (TestUtil.findAll(em, Pegawai.class).isEmpty()) {
            pegawai = PegawaiResourceIT.createUpdatedEntity();
            em.persist(pegawai);
            em.flush();
        } else {
            pegawai = TestUtil.findAll(em, Pegawai.class).get(0);
        }
        updatedCuti.setPegawai(pegawai);
        return updatedCuti;
    }

    @BeforeEach
    void initTest() {
        cuti = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCuti != null) {
            cutiRepository.delete(insertedCuti);
            insertedCuti = null;
        }
    }

    @Test
    @Transactional
    void createCuti() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Cuti
        CutiDTO cutiDTO = cutiMapper.toDto(cuti);
        var returnedCutiDTO = om.readValue(
            restCutiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cutiDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CutiDTO.class
        );

        // Validate the Cuti in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCuti = cutiMapper.toEntity(returnedCutiDTO);
        assertCutiUpdatableFieldsEquals(returnedCuti, getPersistedCuti(returnedCuti));

        insertedCuti = returnedCuti;
    }

    @Test
    @Transactional
    void createCutiWithExistingId() throws Exception {
        // Create the Cuti with an existing ID
        cuti.setId(1L);
        CutiDTO cutiDTO = cutiMapper.toDto(cuti);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCutiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cutiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cuti in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTanggalMulaiIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cuti.setTanggalMulai(null);

        // Create the Cuti, which fails.
        CutiDTO cutiDTO = cutiMapper.toDto(cuti);

        restCutiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cutiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTanggalSelesaiIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cuti.setTanggalSelesai(null);

        // Create the Cuti, which fails.
        CutiDTO cutiDTO = cutiMapper.toDto(cuti);

        restCutiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cutiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAlasanIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cuti.setAlasan(null);

        // Create the Cuti, which fails.
        CutiDTO cutiDTO = cutiMapper.toDto(cuti);

        restCutiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cutiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cuti.setStatus(null);

        // Create the Cuti, which fails.
        CutiDTO cutiDTO = cutiMapper.toDto(cuti);

        restCutiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cutiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCutis() throws Exception {
        // Initialize the database
        insertedCuti = cutiRepository.saveAndFlush(cuti);

        // Get all the cutiList
        restCutiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cuti.getId().intValue())))
            .andExpect(jsonPath("$.[*].tanggalMulai").value(hasItem(DEFAULT_TANGGAL_MULAI.toString())))
            .andExpect(jsonPath("$.[*].tanggalSelesai").value(hasItem(DEFAULT_TANGGAL_SELESAI.toString())))
            .andExpect(jsonPath("$.[*].alasan").value(hasItem(DEFAULT_ALASAN)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCutisWithEagerRelationshipsIsEnabled() throws Exception {
        when(cutiServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCutiMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cutiServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCutisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(cutiServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCutiMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(cutiRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCuti() throws Exception {
        // Initialize the database
        insertedCuti = cutiRepository.saveAndFlush(cuti);

        // Get the cuti
        restCutiMockMvc
            .perform(get(ENTITY_API_URL_ID, cuti.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cuti.getId().intValue()))
            .andExpect(jsonPath("$.tanggalMulai").value(DEFAULT_TANGGAL_MULAI.toString()))
            .andExpect(jsonPath("$.tanggalSelesai").value(DEFAULT_TANGGAL_SELESAI.toString()))
            .andExpect(jsonPath("$.alasan").value(DEFAULT_ALASAN))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    void getCutisByIdFiltering() throws Exception {
        // Initialize the database
        insertedCuti = cutiRepository.saveAndFlush(cuti);

        Long id = cuti.getId();

        defaultCutiFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCutiFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCutiFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCutisByTanggalMulaiIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCuti = cutiRepository.saveAndFlush(cuti);

        // Get all the cutiList where tanggalMulai equals to
        defaultCutiFiltering("tanggalMulai.equals=" + DEFAULT_TANGGAL_MULAI, "tanggalMulai.equals=" + UPDATED_TANGGAL_MULAI);
    }

    @Test
    @Transactional
    void getAllCutisByTanggalMulaiIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCuti = cutiRepository.saveAndFlush(cuti);

        // Get all the cutiList where tanggalMulai in
        defaultCutiFiltering(
            "tanggalMulai.in=" + DEFAULT_TANGGAL_MULAI + "," + UPDATED_TANGGAL_MULAI,
            "tanggalMulai.in=" + UPDATED_TANGGAL_MULAI
        );
    }

    @Test
    @Transactional
    void getAllCutisByTanggalMulaiIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCuti = cutiRepository.saveAndFlush(cuti);

        // Get all the cutiList where tanggalMulai is not null
        defaultCutiFiltering("tanggalMulai.specified=true", "tanggalMulai.specified=false");
    }

    @Test
    @Transactional
    void getAllCutisByTanggalMulaiIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCuti = cutiRepository.saveAndFlush(cuti);

        // Get all the cutiList where tanggalMulai is greater than or equal to
        defaultCutiFiltering(
            "tanggalMulai.greaterThanOrEqual=" + DEFAULT_TANGGAL_MULAI,
            "tanggalMulai.greaterThanOrEqual=" + UPDATED_TANGGAL_MULAI
        );
    }

    @Test
    @Transactional
    void getAllCutisByTanggalMulaiIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCuti = cutiRepository.saveAndFlush(cuti);

        // Get all the cutiList where tanggalMulai is less than or equal to
        defaultCutiFiltering(
            "tanggalMulai.lessThanOrEqual=" + DEFAULT_TANGGAL_MULAI,
            "tanggalMulai.lessThanOrEqual=" + SMALLER_TANGGAL_MULAI
        );
    }

    @Test
    @Transactional
    void getAllCutisByTanggalMulaiIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCuti = cutiRepository.saveAndFlush(cuti);

        // Get all the cutiList where tanggalMulai is less than
        defaultCutiFiltering("tanggalMulai.lessThan=" + UPDATED_TANGGAL_MULAI, "tanggalMulai.lessThan=" + DEFAULT_TANGGAL_MULAI);
    }

    @Test
    @Transactional
    void getAllCutisByTanggalMulaiIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCuti = cutiRepository.saveAndFlush(cuti);

        // Get all the cutiList where tanggalMulai is greater than
        defaultCutiFiltering("tanggalMulai.greaterThan=" + SMALLER_TANGGAL_MULAI, "tanggalMulai.greaterThan=" + DEFAULT_TANGGAL_MULAI);
    }

    @Test
    @Transactional
    void getAllCutisByTanggalSelesaiIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCuti = cutiRepository.saveAndFlush(cuti);

        // Get all the cutiList where tanggalSelesai equals to
        defaultCutiFiltering("tanggalSelesai.equals=" + DEFAULT_TANGGAL_SELESAI, "tanggalSelesai.equals=" + UPDATED_TANGGAL_SELESAI);
    }

    @Test
    @Transactional
    void getAllCutisByTanggalSelesaiIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCuti = cutiRepository.saveAndFlush(cuti);

        // Get all the cutiList where tanggalSelesai in
        defaultCutiFiltering(
            "tanggalSelesai.in=" + DEFAULT_TANGGAL_SELESAI + "," + UPDATED_TANGGAL_SELESAI,
            "tanggalSelesai.in=" + UPDATED_TANGGAL_SELESAI
        );
    }

    @Test
    @Transactional
    void getAllCutisByTanggalSelesaiIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCuti = cutiRepository.saveAndFlush(cuti);

        // Get all the cutiList where tanggalSelesai is not null
        defaultCutiFiltering("tanggalSelesai.specified=true", "tanggalSelesai.specified=false");
    }

    @Test
    @Transactional
    void getAllCutisByTanggalSelesaiIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCuti = cutiRepository.saveAndFlush(cuti);

        // Get all the cutiList where tanggalSelesai is greater than or equal to
        defaultCutiFiltering(
            "tanggalSelesai.greaterThanOrEqual=" + DEFAULT_TANGGAL_SELESAI,
            "tanggalSelesai.greaterThanOrEqual=" + UPDATED_TANGGAL_SELESAI
        );
    }

    @Test
    @Transactional
    void getAllCutisByTanggalSelesaiIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCuti = cutiRepository.saveAndFlush(cuti);

        // Get all the cutiList where tanggalSelesai is less than or equal to
        defaultCutiFiltering(
            "tanggalSelesai.lessThanOrEqual=" + DEFAULT_TANGGAL_SELESAI,
            "tanggalSelesai.lessThanOrEqual=" + SMALLER_TANGGAL_SELESAI
        );
    }

    @Test
    @Transactional
    void getAllCutisByTanggalSelesaiIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCuti = cutiRepository.saveAndFlush(cuti);

        // Get all the cutiList where tanggalSelesai is less than
        defaultCutiFiltering("tanggalSelesai.lessThan=" + UPDATED_TANGGAL_SELESAI, "tanggalSelesai.lessThan=" + DEFAULT_TANGGAL_SELESAI);
    }

    @Test
    @Transactional
    void getAllCutisByTanggalSelesaiIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCuti = cutiRepository.saveAndFlush(cuti);

        // Get all the cutiList where tanggalSelesai is greater than
        defaultCutiFiltering(
            "tanggalSelesai.greaterThan=" + SMALLER_TANGGAL_SELESAI,
            "tanggalSelesai.greaterThan=" + DEFAULT_TANGGAL_SELESAI
        );
    }

    @Test
    @Transactional
    void getAllCutisByAlasanIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCuti = cutiRepository.saveAndFlush(cuti);

        // Get all the cutiList where alasan equals to
        defaultCutiFiltering("alasan.equals=" + DEFAULT_ALASAN, "alasan.equals=" + UPDATED_ALASAN);
    }

    @Test
    @Transactional
    void getAllCutisByAlasanIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCuti = cutiRepository.saveAndFlush(cuti);

        // Get all the cutiList where alasan in
        defaultCutiFiltering("alasan.in=" + DEFAULT_ALASAN + "," + UPDATED_ALASAN, "alasan.in=" + UPDATED_ALASAN);
    }

    @Test
    @Transactional
    void getAllCutisByAlasanIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCuti = cutiRepository.saveAndFlush(cuti);

        // Get all the cutiList where alasan is not null
        defaultCutiFiltering("alasan.specified=true", "alasan.specified=false");
    }

    @Test
    @Transactional
    void getAllCutisByAlasanContainsSomething() throws Exception {
        // Initialize the database
        insertedCuti = cutiRepository.saveAndFlush(cuti);

        // Get all the cutiList where alasan contains
        defaultCutiFiltering("alasan.contains=" + DEFAULT_ALASAN, "alasan.contains=" + UPDATED_ALASAN);
    }

    @Test
    @Transactional
    void getAllCutisByAlasanNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCuti = cutiRepository.saveAndFlush(cuti);

        // Get all the cutiList where alasan does not contain
        defaultCutiFiltering("alasan.doesNotContain=" + UPDATED_ALASAN, "alasan.doesNotContain=" + DEFAULT_ALASAN);
    }

    @Test
    @Transactional
    void getAllCutisByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCuti = cutiRepository.saveAndFlush(cuti);

        // Get all the cutiList where status equals to
        defaultCutiFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllCutisByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCuti = cutiRepository.saveAndFlush(cuti);

        // Get all the cutiList where status in
        defaultCutiFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllCutisByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCuti = cutiRepository.saveAndFlush(cuti);

        // Get all the cutiList where status is not null
        defaultCutiFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllCutisByStatusContainsSomething() throws Exception {
        // Initialize the database
        insertedCuti = cutiRepository.saveAndFlush(cuti);

        // Get all the cutiList where status contains
        defaultCutiFiltering("status.contains=" + DEFAULT_STATUS, "status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllCutisByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCuti = cutiRepository.saveAndFlush(cuti);

        // Get all the cutiList where status does not contain
        defaultCutiFiltering("status.doesNotContain=" + UPDATED_STATUS, "status.doesNotContain=" + DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void getAllCutisByPegawaiIsEqualToSomething() throws Exception {
        Pegawai pegawai;
        if (TestUtil.findAll(em, Pegawai.class).isEmpty()) {
            cutiRepository.saveAndFlush(cuti);
            pegawai = PegawaiResourceIT.createEntity();
        } else {
            pegawai = TestUtil.findAll(em, Pegawai.class).get(0);
        }
        em.persist(pegawai);
        em.flush();
        cuti.setPegawai(pegawai);
        cutiRepository.saveAndFlush(cuti);
        Long pegawaiId = pegawai.getId();
        // Get all the cutiList where pegawai equals to pegawaiId
        defaultCutiShouldBeFound("pegawaiId.equals=" + pegawaiId);

        // Get all the cutiList where pegawai equals to (pegawaiId + 1)
        defaultCutiShouldNotBeFound("pegawaiId.equals=" + (pegawaiId + 1));
    }

    private void defaultCutiFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCutiShouldBeFound(shouldBeFound);
        defaultCutiShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCutiShouldBeFound(String filter) throws Exception {
        restCutiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cuti.getId().intValue())))
            .andExpect(jsonPath("$.[*].tanggalMulai").value(hasItem(DEFAULT_TANGGAL_MULAI.toString())))
            .andExpect(jsonPath("$.[*].tanggalSelesai").value(hasItem(DEFAULT_TANGGAL_SELESAI.toString())))
            .andExpect(jsonPath("$.[*].alasan").value(hasItem(DEFAULT_ALASAN)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));

        // Check, that the count call also returns 1
        restCutiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCutiShouldNotBeFound(String filter) throws Exception {
        restCutiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCutiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCuti() throws Exception {
        // Get the cuti
        restCutiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCuti() throws Exception {
        // Initialize the database
        insertedCuti = cutiRepository.saveAndFlush(cuti);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cuti
        Cuti updatedCuti = cutiRepository.findById(cuti.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCuti are not directly saved in db
        em.detach(updatedCuti);
        updatedCuti
            .tanggalMulai(UPDATED_TANGGAL_MULAI)
            .tanggalSelesai(UPDATED_TANGGAL_SELESAI)
            .alasan(UPDATED_ALASAN)
            .status(UPDATED_STATUS);
        CutiDTO cutiDTO = cutiMapper.toDto(updatedCuti);

        restCutiMockMvc
            .perform(put(ENTITY_API_URL_ID, cutiDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cutiDTO)))
            .andExpect(status().isOk());

        // Validate the Cuti in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCutiToMatchAllProperties(updatedCuti);
    }

    @Test
    @Transactional
    void putNonExistingCuti() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cuti.setId(longCount.incrementAndGet());

        // Create the Cuti
        CutiDTO cutiDTO = cutiMapper.toDto(cuti);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCutiMockMvc
            .perform(put(ENTITY_API_URL_ID, cutiDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cutiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cuti in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCuti() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cuti.setId(longCount.incrementAndGet());

        // Create the Cuti
        CutiDTO cutiDTO = cutiMapper.toDto(cuti);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCutiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cutiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cuti in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCuti() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cuti.setId(longCount.incrementAndGet());

        // Create the Cuti
        CutiDTO cutiDTO = cutiMapper.toDto(cuti);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCutiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cutiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cuti in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCutiWithPatch() throws Exception {
        // Initialize the database
        insertedCuti = cutiRepository.saveAndFlush(cuti);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cuti using partial update
        Cuti partialUpdatedCuti = new Cuti();
        partialUpdatedCuti.setId(cuti.getId());

        partialUpdatedCuti.tanggalMulai(UPDATED_TANGGAL_MULAI).tanggalSelesai(UPDATED_TANGGAL_SELESAI).status(UPDATED_STATUS);

        restCutiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCuti.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCuti))
            )
            .andExpect(status().isOk());

        // Validate the Cuti in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCutiUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCuti, cuti), getPersistedCuti(cuti));
    }

    @Test
    @Transactional
    void fullUpdateCutiWithPatch() throws Exception {
        // Initialize the database
        insertedCuti = cutiRepository.saveAndFlush(cuti);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cuti using partial update
        Cuti partialUpdatedCuti = new Cuti();
        partialUpdatedCuti.setId(cuti.getId());

        partialUpdatedCuti
            .tanggalMulai(UPDATED_TANGGAL_MULAI)
            .tanggalSelesai(UPDATED_TANGGAL_SELESAI)
            .alasan(UPDATED_ALASAN)
            .status(UPDATED_STATUS);

        restCutiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCuti.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCuti))
            )
            .andExpect(status().isOk());

        // Validate the Cuti in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCutiUpdatableFieldsEquals(partialUpdatedCuti, getPersistedCuti(partialUpdatedCuti));
    }

    @Test
    @Transactional
    void patchNonExistingCuti() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cuti.setId(longCount.incrementAndGet());

        // Create the Cuti
        CutiDTO cutiDTO = cutiMapper.toDto(cuti);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCutiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cutiDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cutiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cuti in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCuti() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cuti.setId(longCount.incrementAndGet());

        // Create the Cuti
        CutiDTO cutiDTO = cutiMapper.toDto(cuti);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCutiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cutiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cuti in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCuti() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cuti.setId(longCount.incrementAndGet());

        // Create the Cuti
        CutiDTO cutiDTO = cutiMapper.toDto(cuti);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCutiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cutiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cuti in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCuti() throws Exception {
        // Initialize the database
        insertedCuti = cutiRepository.saveAndFlush(cuti);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cuti
        restCutiMockMvc
            .perform(delete(ENTITY_API_URL_ID, cuti.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cutiRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Cuti getPersistedCuti(Cuti cuti) {
        return cutiRepository.findById(cuti.getId()).orElseThrow();
    }

    protected void assertPersistedCutiToMatchAllProperties(Cuti expectedCuti) {
        assertCutiAllPropertiesEquals(expectedCuti, getPersistedCuti(expectedCuti));
    }

    protected void assertPersistedCutiToMatchUpdatableProperties(Cuti expectedCuti) {
        assertCutiAllUpdatablePropertiesEquals(expectedCuti, getPersistedCuti(expectedCuti));
    }
}
