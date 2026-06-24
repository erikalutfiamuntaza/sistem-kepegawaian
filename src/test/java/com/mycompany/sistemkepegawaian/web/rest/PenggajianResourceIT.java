package com.mycompany.sistemkepegawaian.web.rest;

import static com.mycompany.sistemkepegawaian.domain.PenggajianAsserts.*;
import static com.mycompany.sistemkepegawaian.web.rest.TestUtil.createUpdateProxyForBean;
import static com.mycompany.sistemkepegawaian.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.sistemkepegawaian.IntegrationTest;
import com.mycompany.sistemkepegawaian.domain.Pegawai;
import com.mycompany.sistemkepegawaian.domain.Penggajian;
import com.mycompany.sistemkepegawaian.repository.PenggajianRepository;
import com.mycompany.sistemkepegawaian.service.PenggajianService;
import com.mycompany.sistemkepegawaian.service.dto.PenggajianDTO;
import com.mycompany.sistemkepegawaian.service.mapper.PenggajianMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link PenggajianResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PenggajianResourceIT {

    private static final LocalDate DEFAULT_BULAN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BULAN = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_BULAN = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_GAJI_POKOK = new BigDecimal(1);
    private static final BigDecimal UPDATED_GAJI_POKOK = new BigDecimal(2);
    private static final BigDecimal SMALLER_GAJI_POKOK = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_BONUS = new BigDecimal(1);
    private static final BigDecimal UPDATED_BONUS = new BigDecimal(2);
    private static final BigDecimal SMALLER_BONUS = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_POTONGAN = new BigDecimal(1);
    private static final BigDecimal UPDATED_POTONGAN = new BigDecimal(2);
    private static final BigDecimal SMALLER_POTONGAN = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_TOTAL_GAJI = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_GAJI = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_GAJI = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/penggajians";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PenggajianRepository penggajianRepository;

    @Mock
    private PenggajianRepository penggajianRepositoryMock;

    @Autowired
    private PenggajianMapper penggajianMapper;

    @Mock
    private PenggajianService penggajianServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPenggajianMockMvc;

    private Penggajian penggajian;

    private Penggajian insertedPenggajian;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Penggajian createEntity(EntityManager em) {
        Penggajian penggajian = new Penggajian()
            .bulan(DEFAULT_BULAN)
            .gajiPokok(DEFAULT_GAJI_POKOK)
            .bonus(DEFAULT_BONUS)
            .potongan(DEFAULT_POTONGAN)
            .totalGaji(DEFAULT_TOTAL_GAJI);
        // Add required entity
        Pegawai pegawai;
        if (TestUtil.findAll(em, Pegawai.class).isEmpty()) {
            pegawai = PegawaiResourceIT.createEntity();
            em.persist(pegawai);
            em.flush();
        } else {
            pegawai = TestUtil.findAll(em, Pegawai.class).get(0);
        }
        penggajian.setPegawai(pegawai);
        return penggajian;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Penggajian createUpdatedEntity(EntityManager em) {
        Penggajian updatedPenggajian = new Penggajian()
            .bulan(UPDATED_BULAN)
            .gajiPokok(UPDATED_GAJI_POKOK)
            .bonus(UPDATED_BONUS)
            .potongan(UPDATED_POTONGAN)
            .totalGaji(UPDATED_TOTAL_GAJI);
        // Add required entity
        Pegawai pegawai;
        if (TestUtil.findAll(em, Pegawai.class).isEmpty()) {
            pegawai = PegawaiResourceIT.createUpdatedEntity();
            em.persist(pegawai);
            em.flush();
        } else {
            pegawai = TestUtil.findAll(em, Pegawai.class).get(0);
        }
        updatedPenggajian.setPegawai(pegawai);
        return updatedPenggajian;
    }

    @BeforeEach
    void initTest() {
        penggajian = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPenggajian != null) {
            penggajianRepository.delete(insertedPenggajian);
            insertedPenggajian = null;
        }
    }

    @Test
    @Transactional
    void createPenggajian() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Penggajian
        PenggajianDTO penggajianDTO = penggajianMapper.toDto(penggajian);
        var returnedPenggajianDTO = om.readValue(
            restPenggajianMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(penggajianDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PenggajianDTO.class
        );

        // Validate the Penggajian in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPenggajian = penggajianMapper.toEntity(returnedPenggajianDTO);
        assertPenggajianUpdatableFieldsEquals(returnedPenggajian, getPersistedPenggajian(returnedPenggajian));

        insertedPenggajian = returnedPenggajian;
    }

    @Test
    @Transactional
    void createPenggajianWithExistingId() throws Exception {
        // Create the Penggajian with an existing ID
        penggajian.setId(1L);
        PenggajianDTO penggajianDTO = penggajianMapper.toDto(penggajian);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPenggajianMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(penggajianDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Penggajian in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBulanIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        penggajian.setBulan(null);

        // Create the Penggajian, which fails.
        PenggajianDTO penggajianDTO = penggajianMapper.toDto(penggajian);

        restPenggajianMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(penggajianDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGajiPokokIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        penggajian.setGajiPokok(null);

        // Create the Penggajian, which fails.
        PenggajianDTO penggajianDTO = penggajianMapper.toDto(penggajian);

        restPenggajianMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(penggajianDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBonusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        penggajian.setBonus(null);

        // Create the Penggajian, which fails.
        PenggajianDTO penggajianDTO = penggajianMapper.toDto(penggajian);

        restPenggajianMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(penggajianDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPotonganIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        penggajian.setPotongan(null);

        // Create the Penggajian, which fails.
        PenggajianDTO penggajianDTO = penggajianMapper.toDto(penggajian);

        restPenggajianMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(penggajianDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalGajiIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        penggajian.setTotalGaji(null);

        // Create the Penggajian, which fails.
        PenggajianDTO penggajianDTO = penggajianMapper.toDto(penggajian);

        restPenggajianMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(penggajianDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPenggajians() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        // Get all the penggajianList
        restPenggajianMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(penggajian.getId().intValue())))
            .andExpect(jsonPath("$.[*].bulan").value(hasItem(DEFAULT_BULAN.toString())))
            .andExpect(jsonPath("$.[*].gajiPokok").value(hasItem(sameNumber(DEFAULT_GAJI_POKOK))))
            .andExpect(jsonPath("$.[*].bonus").value(hasItem(sameNumber(DEFAULT_BONUS))))
            .andExpect(jsonPath("$.[*].potongan").value(hasItem(sameNumber(DEFAULT_POTONGAN))))
            .andExpect(jsonPath("$.[*].totalGaji").value(hasItem(sameNumber(DEFAULT_TOTAL_GAJI))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPenggajiansWithEagerRelationshipsIsEnabled() throws Exception {
        when(penggajianServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPenggajianMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(penggajianServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPenggajiansWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(penggajianServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPenggajianMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(penggajianRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPenggajian() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        // Get the penggajian
        restPenggajianMockMvc
            .perform(get(ENTITY_API_URL_ID, penggajian.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(penggajian.getId().intValue()))
            .andExpect(jsonPath("$.bulan").value(DEFAULT_BULAN.toString()))
            .andExpect(jsonPath("$.gajiPokok").value(sameNumber(DEFAULT_GAJI_POKOK)))
            .andExpect(jsonPath("$.bonus").value(sameNumber(DEFAULT_BONUS)))
            .andExpect(jsonPath("$.potongan").value(sameNumber(DEFAULT_POTONGAN)))
            .andExpect(jsonPath("$.totalGaji").value(sameNumber(DEFAULT_TOTAL_GAJI)));
    }

    @Test
    @Transactional
    void getPenggajiansByIdFiltering() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        Long id = penggajian.getId();

        defaultPenggajianFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPenggajianFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPenggajianFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPenggajiansByBulanIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        // Get all the penggajianList where bulan equals to
        defaultPenggajianFiltering("bulan.equals=" + DEFAULT_BULAN, "bulan.equals=" + UPDATED_BULAN);
    }

    @Test
    @Transactional
    void getAllPenggajiansByBulanIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        // Get all the penggajianList where bulan in
        defaultPenggajianFiltering("bulan.in=" + DEFAULT_BULAN + "," + UPDATED_BULAN, "bulan.in=" + UPDATED_BULAN);
    }

    @Test
    @Transactional
    void getAllPenggajiansByBulanIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        // Get all the penggajianList where bulan is not null
        defaultPenggajianFiltering("bulan.specified=true", "bulan.specified=false");
    }

    @Test
    @Transactional
    void getAllPenggajiansByBulanIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        // Get all the penggajianList where bulan is greater than or equal to
        defaultPenggajianFiltering("bulan.greaterThanOrEqual=" + DEFAULT_BULAN, "bulan.greaterThanOrEqual=" + UPDATED_BULAN);
    }

    @Test
    @Transactional
    void getAllPenggajiansByBulanIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        // Get all the penggajianList where bulan is less than or equal to
        defaultPenggajianFiltering("bulan.lessThanOrEqual=" + DEFAULT_BULAN, "bulan.lessThanOrEqual=" + SMALLER_BULAN);
    }

    @Test
    @Transactional
    void getAllPenggajiansByBulanIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        // Get all the penggajianList where bulan is less than
        defaultPenggajianFiltering("bulan.lessThan=" + UPDATED_BULAN, "bulan.lessThan=" + DEFAULT_BULAN);
    }

    @Test
    @Transactional
    void getAllPenggajiansByBulanIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        // Get all the penggajianList where bulan is greater than
        defaultPenggajianFiltering("bulan.greaterThan=" + SMALLER_BULAN, "bulan.greaterThan=" + DEFAULT_BULAN);
    }

    @Test
    @Transactional
    void getAllPenggajiansByGajiPokokIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        // Get all the penggajianList where gajiPokok equals to
        defaultPenggajianFiltering("gajiPokok.equals=" + DEFAULT_GAJI_POKOK, "gajiPokok.equals=" + UPDATED_GAJI_POKOK);
    }

    @Test
    @Transactional
    void getAllPenggajiansByGajiPokokIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        // Get all the penggajianList where gajiPokok in
        defaultPenggajianFiltering("gajiPokok.in=" + DEFAULT_GAJI_POKOK + "," + UPDATED_GAJI_POKOK, "gajiPokok.in=" + UPDATED_GAJI_POKOK);
    }

    @Test
    @Transactional
    void getAllPenggajiansByGajiPokokIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        // Get all the penggajianList where gajiPokok is not null
        defaultPenggajianFiltering("gajiPokok.specified=true", "gajiPokok.specified=false");
    }

    @Test
    @Transactional
    void getAllPenggajiansByGajiPokokIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        // Get all the penggajianList where gajiPokok is greater than or equal to
        defaultPenggajianFiltering(
            "gajiPokok.greaterThanOrEqual=" + DEFAULT_GAJI_POKOK,
            "gajiPokok.greaterThanOrEqual=" + UPDATED_GAJI_POKOK
        );
    }

    @Test
    @Transactional
    void getAllPenggajiansByGajiPokokIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        // Get all the penggajianList where gajiPokok is less than or equal to
        defaultPenggajianFiltering("gajiPokok.lessThanOrEqual=" + DEFAULT_GAJI_POKOK, "gajiPokok.lessThanOrEqual=" + SMALLER_GAJI_POKOK);
    }

    @Test
    @Transactional
    void getAllPenggajiansByGajiPokokIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        // Get all the penggajianList where gajiPokok is less than
        defaultPenggajianFiltering("gajiPokok.lessThan=" + UPDATED_GAJI_POKOK, "gajiPokok.lessThan=" + DEFAULT_GAJI_POKOK);
    }

    @Test
    @Transactional
    void getAllPenggajiansByGajiPokokIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        // Get all the penggajianList where gajiPokok is greater than
        defaultPenggajianFiltering("gajiPokok.greaterThan=" + SMALLER_GAJI_POKOK, "gajiPokok.greaterThan=" + DEFAULT_GAJI_POKOK);
    }

    @Test
    @Transactional
    void getAllPenggajiansByBonusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        // Get all the penggajianList where bonus equals to
        defaultPenggajianFiltering("bonus.equals=" + DEFAULT_BONUS, "bonus.equals=" + UPDATED_BONUS);
    }

    @Test
    @Transactional
    void getAllPenggajiansByBonusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        // Get all the penggajianList where bonus in
        defaultPenggajianFiltering("bonus.in=" + DEFAULT_BONUS + "," + UPDATED_BONUS, "bonus.in=" + UPDATED_BONUS);
    }

    @Test
    @Transactional
    void getAllPenggajiansByBonusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        // Get all the penggajianList where bonus is not null
        defaultPenggajianFiltering("bonus.specified=true", "bonus.specified=false");
    }

    @Test
    @Transactional
    void getAllPenggajiansByBonusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        // Get all the penggajianList where bonus is greater than or equal to
        defaultPenggajianFiltering("bonus.greaterThanOrEqual=" + DEFAULT_BONUS, "bonus.greaterThanOrEqual=" + UPDATED_BONUS);
    }

    @Test
    @Transactional
    void getAllPenggajiansByBonusIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        // Get all the penggajianList where bonus is less than or equal to
        defaultPenggajianFiltering("bonus.lessThanOrEqual=" + DEFAULT_BONUS, "bonus.lessThanOrEqual=" + SMALLER_BONUS);
    }

    @Test
    @Transactional
    void getAllPenggajiansByBonusIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        // Get all the penggajianList where bonus is less than
        defaultPenggajianFiltering("bonus.lessThan=" + UPDATED_BONUS, "bonus.lessThan=" + DEFAULT_BONUS);
    }

    @Test
    @Transactional
    void getAllPenggajiansByBonusIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        // Get all the penggajianList where bonus is greater than
        defaultPenggajianFiltering("bonus.greaterThan=" + SMALLER_BONUS, "bonus.greaterThan=" + DEFAULT_BONUS);
    }

    @Test
    @Transactional
    void getAllPenggajiansByPotonganIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        // Get all the penggajianList where potongan equals to
        defaultPenggajianFiltering("potongan.equals=" + DEFAULT_POTONGAN, "potongan.equals=" + UPDATED_POTONGAN);
    }

    @Test
    @Transactional
    void getAllPenggajiansByPotonganIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        // Get all the penggajianList where potongan in
        defaultPenggajianFiltering("potongan.in=" + DEFAULT_POTONGAN + "," + UPDATED_POTONGAN, "potongan.in=" + UPDATED_POTONGAN);
    }

    @Test
    @Transactional
    void getAllPenggajiansByPotonganIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        // Get all the penggajianList where potongan is not null
        defaultPenggajianFiltering("potongan.specified=true", "potongan.specified=false");
    }

    @Test
    @Transactional
    void getAllPenggajiansByPotonganIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        // Get all the penggajianList where potongan is greater than or equal to
        defaultPenggajianFiltering("potongan.greaterThanOrEqual=" + DEFAULT_POTONGAN, "potongan.greaterThanOrEqual=" + UPDATED_POTONGAN);
    }

    @Test
    @Transactional
    void getAllPenggajiansByPotonganIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        // Get all the penggajianList where potongan is less than or equal to
        defaultPenggajianFiltering("potongan.lessThanOrEqual=" + DEFAULT_POTONGAN, "potongan.lessThanOrEqual=" + SMALLER_POTONGAN);
    }

    @Test
    @Transactional
    void getAllPenggajiansByPotonganIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        // Get all the penggajianList where potongan is less than
        defaultPenggajianFiltering("potongan.lessThan=" + UPDATED_POTONGAN, "potongan.lessThan=" + DEFAULT_POTONGAN);
    }

    @Test
    @Transactional
    void getAllPenggajiansByPotonganIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        // Get all the penggajianList where potongan is greater than
        defaultPenggajianFiltering("potongan.greaterThan=" + SMALLER_POTONGAN, "potongan.greaterThan=" + DEFAULT_POTONGAN);
    }

    @Test
    @Transactional
    void getAllPenggajiansByTotalGajiIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        // Get all the penggajianList where totalGaji equals to
        defaultPenggajianFiltering("totalGaji.equals=" + DEFAULT_TOTAL_GAJI, "totalGaji.equals=" + UPDATED_TOTAL_GAJI);
    }

    @Test
    @Transactional
    void getAllPenggajiansByTotalGajiIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        // Get all the penggajianList where totalGaji in
        defaultPenggajianFiltering("totalGaji.in=" + DEFAULT_TOTAL_GAJI + "," + UPDATED_TOTAL_GAJI, "totalGaji.in=" + UPDATED_TOTAL_GAJI);
    }

    @Test
    @Transactional
    void getAllPenggajiansByTotalGajiIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        // Get all the penggajianList where totalGaji is not null
        defaultPenggajianFiltering("totalGaji.specified=true", "totalGaji.specified=false");
    }

    @Test
    @Transactional
    void getAllPenggajiansByTotalGajiIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        // Get all the penggajianList where totalGaji is greater than or equal to
        defaultPenggajianFiltering(
            "totalGaji.greaterThanOrEqual=" + DEFAULT_TOTAL_GAJI,
            "totalGaji.greaterThanOrEqual=" + UPDATED_TOTAL_GAJI
        );
    }

    @Test
    @Transactional
    void getAllPenggajiansByTotalGajiIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        // Get all the penggajianList where totalGaji is less than or equal to
        defaultPenggajianFiltering("totalGaji.lessThanOrEqual=" + DEFAULT_TOTAL_GAJI, "totalGaji.lessThanOrEqual=" + SMALLER_TOTAL_GAJI);
    }

    @Test
    @Transactional
    void getAllPenggajiansByTotalGajiIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        // Get all the penggajianList where totalGaji is less than
        defaultPenggajianFiltering("totalGaji.lessThan=" + UPDATED_TOTAL_GAJI, "totalGaji.lessThan=" + DEFAULT_TOTAL_GAJI);
    }

    @Test
    @Transactional
    void getAllPenggajiansByTotalGajiIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        // Get all the penggajianList where totalGaji is greater than
        defaultPenggajianFiltering("totalGaji.greaterThan=" + SMALLER_TOTAL_GAJI, "totalGaji.greaterThan=" + DEFAULT_TOTAL_GAJI);
    }

    @Test
    @Transactional
    void getAllPenggajiansByPegawaiIsEqualToSomething() throws Exception {
        Pegawai pegawai;
        if (TestUtil.findAll(em, Pegawai.class).isEmpty()) {
            penggajianRepository.saveAndFlush(penggajian);
            pegawai = PegawaiResourceIT.createEntity();
        } else {
            pegawai = TestUtil.findAll(em, Pegawai.class).get(0);
        }
        em.persist(pegawai);
        em.flush();
        penggajian.setPegawai(pegawai);
        penggajianRepository.saveAndFlush(penggajian);
        Long pegawaiId = pegawai.getId();
        // Get all the penggajianList where pegawai equals to pegawaiId
        defaultPenggajianShouldBeFound("pegawaiId.equals=" + pegawaiId);

        // Get all the penggajianList where pegawai equals to (pegawaiId + 1)
        defaultPenggajianShouldNotBeFound("pegawaiId.equals=" + (pegawaiId + 1));
    }

    private void defaultPenggajianFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPenggajianShouldBeFound(shouldBeFound);
        defaultPenggajianShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPenggajianShouldBeFound(String filter) throws Exception {
        restPenggajianMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(penggajian.getId().intValue())))
            .andExpect(jsonPath("$.[*].bulan").value(hasItem(DEFAULT_BULAN.toString())))
            .andExpect(jsonPath("$.[*].gajiPokok").value(hasItem(sameNumber(DEFAULT_GAJI_POKOK))))
            .andExpect(jsonPath("$.[*].bonus").value(hasItem(sameNumber(DEFAULT_BONUS))))
            .andExpect(jsonPath("$.[*].potongan").value(hasItem(sameNumber(DEFAULT_POTONGAN))))
            .andExpect(jsonPath("$.[*].totalGaji").value(hasItem(sameNumber(DEFAULT_TOTAL_GAJI))));

        // Check, that the count call also returns 1
        restPenggajianMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPenggajianShouldNotBeFound(String filter) throws Exception {
        restPenggajianMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPenggajianMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPenggajian() throws Exception {
        // Get the penggajian
        restPenggajianMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPenggajian() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the penggajian
        Penggajian updatedPenggajian = penggajianRepository.findById(penggajian.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPenggajian are not directly saved in db
        em.detach(updatedPenggajian);
        updatedPenggajian
            .bulan(UPDATED_BULAN)
            .gajiPokok(UPDATED_GAJI_POKOK)
            .bonus(UPDATED_BONUS)
            .potongan(UPDATED_POTONGAN)
            .totalGaji(UPDATED_TOTAL_GAJI);
        PenggajianDTO penggajianDTO = penggajianMapper.toDto(updatedPenggajian);

        restPenggajianMockMvc
            .perform(
                put(ENTITY_API_URL_ID, penggajianDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(penggajianDTO))
            )
            .andExpect(status().isOk());

        // Validate the Penggajian in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPenggajianToMatchAllProperties(updatedPenggajian);
    }

    @Test
    @Transactional
    void putNonExistingPenggajian() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        penggajian.setId(longCount.incrementAndGet());

        // Create the Penggajian
        PenggajianDTO penggajianDTO = penggajianMapper.toDto(penggajian);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPenggajianMockMvc
            .perform(
                put(ENTITY_API_URL_ID, penggajianDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(penggajianDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Penggajian in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPenggajian() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        penggajian.setId(longCount.incrementAndGet());

        // Create the Penggajian
        PenggajianDTO penggajianDTO = penggajianMapper.toDto(penggajian);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPenggajianMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(penggajianDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Penggajian in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPenggajian() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        penggajian.setId(longCount.incrementAndGet());

        // Create the Penggajian
        PenggajianDTO penggajianDTO = penggajianMapper.toDto(penggajian);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPenggajianMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(penggajianDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Penggajian in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePenggajianWithPatch() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the penggajian using partial update
        Penggajian partialUpdatedPenggajian = new Penggajian();
        partialUpdatedPenggajian.setId(penggajian.getId());

        partialUpdatedPenggajian.totalGaji(UPDATED_TOTAL_GAJI);

        restPenggajianMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPenggajian.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPenggajian))
            )
            .andExpect(status().isOk());

        // Validate the Penggajian in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPenggajianUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPenggajian, penggajian),
            getPersistedPenggajian(penggajian)
        );
    }

    @Test
    @Transactional
    void fullUpdatePenggajianWithPatch() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the penggajian using partial update
        Penggajian partialUpdatedPenggajian = new Penggajian();
        partialUpdatedPenggajian.setId(penggajian.getId());

        partialUpdatedPenggajian
            .bulan(UPDATED_BULAN)
            .gajiPokok(UPDATED_GAJI_POKOK)
            .bonus(UPDATED_BONUS)
            .potongan(UPDATED_POTONGAN)
            .totalGaji(UPDATED_TOTAL_GAJI);

        restPenggajianMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPenggajian.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPenggajian))
            )
            .andExpect(status().isOk());

        // Validate the Penggajian in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPenggajianUpdatableFieldsEquals(partialUpdatedPenggajian, getPersistedPenggajian(partialUpdatedPenggajian));
    }

    @Test
    @Transactional
    void patchNonExistingPenggajian() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        penggajian.setId(longCount.incrementAndGet());

        // Create the Penggajian
        PenggajianDTO penggajianDTO = penggajianMapper.toDto(penggajian);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPenggajianMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, penggajianDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(penggajianDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Penggajian in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPenggajian() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        penggajian.setId(longCount.incrementAndGet());

        // Create the Penggajian
        PenggajianDTO penggajianDTO = penggajianMapper.toDto(penggajian);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPenggajianMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(penggajianDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Penggajian in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPenggajian() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        penggajian.setId(longCount.incrementAndGet());

        // Create the Penggajian
        PenggajianDTO penggajianDTO = penggajianMapper.toDto(penggajian);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPenggajianMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(penggajianDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Penggajian in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePenggajian() throws Exception {
        // Initialize the database
        insertedPenggajian = penggajianRepository.saveAndFlush(penggajian);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the penggajian
        restPenggajianMockMvc
            .perform(delete(ENTITY_API_URL_ID, penggajian.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return penggajianRepository.count();
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

    protected Penggajian getPersistedPenggajian(Penggajian penggajian) {
        return penggajianRepository.findById(penggajian.getId()).orElseThrow();
    }

    protected void assertPersistedPenggajianToMatchAllProperties(Penggajian expectedPenggajian) {
        assertPenggajianAllPropertiesEquals(expectedPenggajian, getPersistedPenggajian(expectedPenggajian));
    }

    protected void assertPersistedPenggajianToMatchUpdatableProperties(Penggajian expectedPenggajian) {
        assertPenggajianAllUpdatablePropertiesEquals(expectedPenggajian, getPersistedPenggajian(expectedPenggajian));
    }
}
