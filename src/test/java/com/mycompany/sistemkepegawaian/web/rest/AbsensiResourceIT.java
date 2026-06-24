package com.mycompany.sistemkepegawaian.web.rest;

import static com.mycompany.sistemkepegawaian.domain.AbsensiAsserts.*;
import static com.mycompany.sistemkepegawaian.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.sistemkepegawaian.IntegrationTest;
import com.mycompany.sistemkepegawaian.domain.Absensi;
import com.mycompany.sistemkepegawaian.domain.Pegawai;
import com.mycompany.sistemkepegawaian.repository.AbsensiRepository;
import com.mycompany.sistemkepegawaian.service.AbsensiService;
import com.mycompany.sistemkepegawaian.service.dto.AbsensiDTO;
import com.mycompany.sistemkepegawaian.service.mapper.AbsensiMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link AbsensiResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AbsensiResourceIT {

    private static final LocalDate DEFAULT_TANGGAL = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TANGGAL = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TANGGAL = LocalDate.ofEpochDay(-1L);

    private static final Instant DEFAULT_JAM_MASUK = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_JAM_MASUK = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_JAM_KELUAR = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_JAM_KELUAR = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/absensis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AbsensiRepository absensiRepository;

    @Mock
    private AbsensiRepository absensiRepositoryMock;

    @Autowired
    private AbsensiMapper absensiMapper;

    @Mock
    private AbsensiService absensiServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAbsensiMockMvc;

    private Absensi absensi;

    private Absensi insertedAbsensi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Absensi createEntity(EntityManager em) {
        Absensi absensi = new Absensi()
            .tanggal(DEFAULT_TANGGAL)
            .jamMasuk(DEFAULT_JAM_MASUK)
            .jamKeluar(DEFAULT_JAM_KELUAR)
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
        absensi.setPegawai(pegawai);
        return absensi;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Absensi createUpdatedEntity(EntityManager em) {
        Absensi updatedAbsensi = new Absensi()
            .tanggal(UPDATED_TANGGAL)
            .jamMasuk(UPDATED_JAM_MASUK)
            .jamKeluar(UPDATED_JAM_KELUAR)
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
        updatedAbsensi.setPegawai(pegawai);
        return updatedAbsensi;
    }

    @BeforeEach
    void initTest() {
        absensi = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedAbsensi != null) {
            absensiRepository.delete(insertedAbsensi);
            insertedAbsensi = null;
        }
    }

    @Test
    @Transactional
    void createAbsensi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Absensi
        AbsensiDTO absensiDTO = absensiMapper.toDto(absensi);
        var returnedAbsensiDTO = om.readValue(
            restAbsensiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(absensiDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AbsensiDTO.class
        );

        // Validate the Absensi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAbsensi = absensiMapper.toEntity(returnedAbsensiDTO);
        assertAbsensiUpdatableFieldsEquals(returnedAbsensi, getPersistedAbsensi(returnedAbsensi));

        insertedAbsensi = returnedAbsensi;
    }

    @Test
    @Transactional
    void createAbsensiWithExistingId() throws Exception {
        // Create the Absensi with an existing ID
        absensi.setId(1L);
        AbsensiDTO absensiDTO = absensiMapper.toDto(absensi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAbsensiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(absensiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Absensi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTanggalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        absensi.setTanggal(null);

        // Create the Absensi, which fails.
        AbsensiDTO absensiDTO = absensiMapper.toDto(absensi);

        restAbsensiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(absensiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkJamMasukIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        absensi.setJamMasuk(null);

        // Create the Absensi, which fails.
        AbsensiDTO absensiDTO = absensiMapper.toDto(absensi);

        restAbsensiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(absensiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkJamKeluarIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        absensi.setJamKeluar(null);

        // Create the Absensi, which fails.
        AbsensiDTO absensiDTO = absensiMapper.toDto(absensi);

        restAbsensiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(absensiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        absensi.setStatus(null);

        // Create the Absensi, which fails.
        AbsensiDTO absensiDTO = absensiMapper.toDto(absensi);

        restAbsensiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(absensiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAbsensis() throws Exception {
        // Initialize the database
        insertedAbsensi = absensiRepository.saveAndFlush(absensi);

        // Get all the absensiList
        restAbsensiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(absensi.getId().intValue())))
            .andExpect(jsonPath("$.[*].tanggal").value(hasItem(DEFAULT_TANGGAL.toString())))
            .andExpect(jsonPath("$.[*].jamMasuk").value(hasItem(DEFAULT_JAM_MASUK.toString())))
            .andExpect(jsonPath("$.[*].jamKeluar").value(hasItem(DEFAULT_JAM_KELUAR.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAbsensisWithEagerRelationshipsIsEnabled() throws Exception {
        when(absensiServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAbsensiMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(absensiServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAbsensisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(absensiServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAbsensiMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(absensiRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAbsensi() throws Exception {
        // Initialize the database
        insertedAbsensi = absensiRepository.saveAndFlush(absensi);

        // Get the absensi
        restAbsensiMockMvc
            .perform(get(ENTITY_API_URL_ID, absensi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(absensi.getId().intValue()))
            .andExpect(jsonPath("$.tanggal").value(DEFAULT_TANGGAL.toString()))
            .andExpect(jsonPath("$.jamMasuk").value(DEFAULT_JAM_MASUK.toString()))
            .andExpect(jsonPath("$.jamKeluar").value(DEFAULT_JAM_KELUAR.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    void getAbsensisByIdFiltering() throws Exception {
        // Initialize the database
        insertedAbsensi = absensiRepository.saveAndFlush(absensi);

        Long id = absensi.getId();

        defaultAbsensiFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAbsensiFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAbsensiFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAbsensisByTanggalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAbsensi = absensiRepository.saveAndFlush(absensi);

        // Get all the absensiList where tanggal equals to
        defaultAbsensiFiltering("tanggal.equals=" + DEFAULT_TANGGAL, "tanggal.equals=" + UPDATED_TANGGAL);
    }

    @Test
    @Transactional
    void getAllAbsensisByTanggalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAbsensi = absensiRepository.saveAndFlush(absensi);

        // Get all the absensiList where tanggal in
        defaultAbsensiFiltering("tanggal.in=" + DEFAULT_TANGGAL + "," + UPDATED_TANGGAL, "tanggal.in=" + UPDATED_TANGGAL);
    }

    @Test
    @Transactional
    void getAllAbsensisByTanggalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAbsensi = absensiRepository.saveAndFlush(absensi);

        // Get all the absensiList where tanggal is not null
        defaultAbsensiFiltering("tanggal.specified=true", "tanggal.specified=false");
    }

    @Test
    @Transactional
    void getAllAbsensisByTanggalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAbsensi = absensiRepository.saveAndFlush(absensi);

        // Get all the absensiList where tanggal is greater than or equal to
        defaultAbsensiFiltering("tanggal.greaterThanOrEqual=" + DEFAULT_TANGGAL, "tanggal.greaterThanOrEqual=" + UPDATED_TANGGAL);
    }

    @Test
    @Transactional
    void getAllAbsensisByTanggalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAbsensi = absensiRepository.saveAndFlush(absensi);

        // Get all the absensiList where tanggal is less than or equal to
        defaultAbsensiFiltering("tanggal.lessThanOrEqual=" + DEFAULT_TANGGAL, "tanggal.lessThanOrEqual=" + SMALLER_TANGGAL);
    }

    @Test
    @Transactional
    void getAllAbsensisByTanggalIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAbsensi = absensiRepository.saveAndFlush(absensi);

        // Get all the absensiList where tanggal is less than
        defaultAbsensiFiltering("tanggal.lessThan=" + UPDATED_TANGGAL, "tanggal.lessThan=" + DEFAULT_TANGGAL);
    }

    @Test
    @Transactional
    void getAllAbsensisByTanggalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAbsensi = absensiRepository.saveAndFlush(absensi);

        // Get all the absensiList where tanggal is greater than
        defaultAbsensiFiltering("tanggal.greaterThan=" + SMALLER_TANGGAL, "tanggal.greaterThan=" + DEFAULT_TANGGAL);
    }

    @Test
    @Transactional
    void getAllAbsensisByJamMasukIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAbsensi = absensiRepository.saveAndFlush(absensi);

        // Get all the absensiList where jamMasuk equals to
        defaultAbsensiFiltering("jamMasuk.equals=" + DEFAULT_JAM_MASUK, "jamMasuk.equals=" + UPDATED_JAM_MASUK);
    }

    @Test
    @Transactional
    void getAllAbsensisByJamMasukIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAbsensi = absensiRepository.saveAndFlush(absensi);

        // Get all the absensiList where jamMasuk in
        defaultAbsensiFiltering("jamMasuk.in=" + DEFAULT_JAM_MASUK + "," + UPDATED_JAM_MASUK, "jamMasuk.in=" + UPDATED_JAM_MASUK);
    }

    @Test
    @Transactional
    void getAllAbsensisByJamMasukIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAbsensi = absensiRepository.saveAndFlush(absensi);

        // Get all the absensiList where jamMasuk is not null
        defaultAbsensiFiltering("jamMasuk.specified=true", "jamMasuk.specified=false");
    }

    @Test
    @Transactional
    void getAllAbsensisByJamKeluarIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAbsensi = absensiRepository.saveAndFlush(absensi);

        // Get all the absensiList where jamKeluar equals to
        defaultAbsensiFiltering("jamKeluar.equals=" + DEFAULT_JAM_KELUAR, "jamKeluar.equals=" + UPDATED_JAM_KELUAR);
    }

    @Test
    @Transactional
    void getAllAbsensisByJamKeluarIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAbsensi = absensiRepository.saveAndFlush(absensi);

        // Get all the absensiList where jamKeluar in
        defaultAbsensiFiltering("jamKeluar.in=" + DEFAULT_JAM_KELUAR + "," + UPDATED_JAM_KELUAR, "jamKeluar.in=" + UPDATED_JAM_KELUAR);
    }

    @Test
    @Transactional
    void getAllAbsensisByJamKeluarIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAbsensi = absensiRepository.saveAndFlush(absensi);

        // Get all the absensiList where jamKeluar is not null
        defaultAbsensiFiltering("jamKeluar.specified=true", "jamKeluar.specified=false");
    }

    @Test
    @Transactional
    void getAllAbsensisByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAbsensi = absensiRepository.saveAndFlush(absensi);

        // Get all the absensiList where status equals to
        defaultAbsensiFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllAbsensisByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAbsensi = absensiRepository.saveAndFlush(absensi);

        // Get all the absensiList where status in
        defaultAbsensiFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllAbsensisByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAbsensi = absensiRepository.saveAndFlush(absensi);

        // Get all the absensiList where status is not null
        defaultAbsensiFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllAbsensisByStatusContainsSomething() throws Exception {
        // Initialize the database
        insertedAbsensi = absensiRepository.saveAndFlush(absensi);

        // Get all the absensiList where status contains
        defaultAbsensiFiltering("status.contains=" + DEFAULT_STATUS, "status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllAbsensisByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAbsensi = absensiRepository.saveAndFlush(absensi);

        // Get all the absensiList where status does not contain
        defaultAbsensiFiltering("status.doesNotContain=" + UPDATED_STATUS, "status.doesNotContain=" + DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void getAllAbsensisByPegawaiIsEqualToSomething() throws Exception {
        Pegawai pegawai;
        if (TestUtil.findAll(em, Pegawai.class).isEmpty()) {
            absensiRepository.saveAndFlush(absensi);
            pegawai = PegawaiResourceIT.createEntity();
        } else {
            pegawai = TestUtil.findAll(em, Pegawai.class).get(0);
        }
        em.persist(pegawai);
        em.flush();
        absensi.setPegawai(pegawai);
        absensiRepository.saveAndFlush(absensi);
        Long pegawaiId = pegawai.getId();
        // Get all the absensiList where pegawai equals to pegawaiId
        defaultAbsensiShouldBeFound("pegawaiId.equals=" + pegawaiId);

        // Get all the absensiList where pegawai equals to (pegawaiId + 1)
        defaultAbsensiShouldNotBeFound("pegawaiId.equals=" + (pegawaiId + 1));
    }

    private void defaultAbsensiFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAbsensiShouldBeFound(shouldBeFound);
        defaultAbsensiShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAbsensiShouldBeFound(String filter) throws Exception {
        restAbsensiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(absensi.getId().intValue())))
            .andExpect(jsonPath("$.[*].tanggal").value(hasItem(DEFAULT_TANGGAL.toString())))
            .andExpect(jsonPath("$.[*].jamMasuk").value(hasItem(DEFAULT_JAM_MASUK.toString())))
            .andExpect(jsonPath("$.[*].jamKeluar").value(hasItem(DEFAULT_JAM_KELUAR.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));

        // Check, that the count call also returns 1
        restAbsensiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAbsensiShouldNotBeFound(String filter) throws Exception {
        restAbsensiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAbsensiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAbsensi() throws Exception {
        // Get the absensi
        restAbsensiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAbsensi() throws Exception {
        // Initialize the database
        insertedAbsensi = absensiRepository.saveAndFlush(absensi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the absensi
        Absensi updatedAbsensi = absensiRepository.findById(absensi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAbsensi are not directly saved in db
        em.detach(updatedAbsensi);
        updatedAbsensi.tanggal(UPDATED_TANGGAL).jamMasuk(UPDATED_JAM_MASUK).jamKeluar(UPDATED_JAM_KELUAR).status(UPDATED_STATUS);
        AbsensiDTO absensiDTO = absensiMapper.toDto(updatedAbsensi);

        restAbsensiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, absensiDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(absensiDTO))
            )
            .andExpect(status().isOk());

        // Validate the Absensi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAbsensiToMatchAllProperties(updatedAbsensi);
    }

    @Test
    @Transactional
    void putNonExistingAbsensi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        absensi.setId(longCount.incrementAndGet());

        // Create the Absensi
        AbsensiDTO absensiDTO = absensiMapper.toDto(absensi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAbsensiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, absensiDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(absensiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Absensi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAbsensi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        absensi.setId(longCount.incrementAndGet());

        // Create the Absensi
        AbsensiDTO absensiDTO = absensiMapper.toDto(absensi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAbsensiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(absensiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Absensi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAbsensi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        absensi.setId(longCount.incrementAndGet());

        // Create the Absensi
        AbsensiDTO absensiDTO = absensiMapper.toDto(absensi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAbsensiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(absensiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Absensi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAbsensiWithPatch() throws Exception {
        // Initialize the database
        insertedAbsensi = absensiRepository.saveAndFlush(absensi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the absensi using partial update
        Absensi partialUpdatedAbsensi = new Absensi();
        partialUpdatedAbsensi.setId(absensi.getId());

        partialUpdatedAbsensi.jamMasuk(UPDATED_JAM_MASUK).jamKeluar(UPDATED_JAM_KELUAR);

        restAbsensiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAbsensi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAbsensi))
            )
            .andExpect(status().isOk());

        // Validate the Absensi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAbsensiUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAbsensi, absensi), getPersistedAbsensi(absensi));
    }

    @Test
    @Transactional
    void fullUpdateAbsensiWithPatch() throws Exception {
        // Initialize the database
        insertedAbsensi = absensiRepository.saveAndFlush(absensi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the absensi using partial update
        Absensi partialUpdatedAbsensi = new Absensi();
        partialUpdatedAbsensi.setId(absensi.getId());

        partialUpdatedAbsensi.tanggal(UPDATED_TANGGAL).jamMasuk(UPDATED_JAM_MASUK).jamKeluar(UPDATED_JAM_KELUAR).status(UPDATED_STATUS);

        restAbsensiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAbsensi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAbsensi))
            )
            .andExpect(status().isOk());

        // Validate the Absensi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAbsensiUpdatableFieldsEquals(partialUpdatedAbsensi, getPersistedAbsensi(partialUpdatedAbsensi));
    }

    @Test
    @Transactional
    void patchNonExistingAbsensi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        absensi.setId(longCount.incrementAndGet());

        // Create the Absensi
        AbsensiDTO absensiDTO = absensiMapper.toDto(absensi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAbsensiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, absensiDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(absensiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Absensi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAbsensi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        absensi.setId(longCount.incrementAndGet());

        // Create the Absensi
        AbsensiDTO absensiDTO = absensiMapper.toDto(absensi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAbsensiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(absensiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Absensi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAbsensi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        absensi.setId(longCount.incrementAndGet());

        // Create the Absensi
        AbsensiDTO absensiDTO = absensiMapper.toDto(absensi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAbsensiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(absensiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Absensi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAbsensi() throws Exception {
        // Initialize the database
        insertedAbsensi = absensiRepository.saveAndFlush(absensi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the absensi
        restAbsensiMockMvc
            .perform(delete(ENTITY_API_URL_ID, absensi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return absensiRepository.count();
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

    protected Absensi getPersistedAbsensi(Absensi absensi) {
        return absensiRepository.findById(absensi.getId()).orElseThrow();
    }

    protected void assertPersistedAbsensiToMatchAllProperties(Absensi expectedAbsensi) {
        assertAbsensiAllPropertiesEquals(expectedAbsensi, getPersistedAbsensi(expectedAbsensi));
    }

    protected void assertPersistedAbsensiToMatchUpdatableProperties(Absensi expectedAbsensi) {
        assertAbsensiAllUpdatablePropertiesEquals(expectedAbsensi, getPersistedAbsensi(expectedAbsensi));
    }
}
