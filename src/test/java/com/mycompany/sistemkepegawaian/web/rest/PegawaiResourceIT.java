package com.mycompany.sistemkepegawaian.web.rest;

import static com.mycompany.sistemkepegawaian.domain.PegawaiAsserts.*;
import static com.mycompany.sistemkepegawaian.web.rest.TestUtil.createUpdateProxyForBean;
import static com.mycompany.sistemkepegawaian.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.sistemkepegawaian.IntegrationTest;
import com.mycompany.sistemkepegawaian.domain.Pegawai;
import com.mycompany.sistemkepegawaian.repository.PegawaiRepository;
import com.mycompany.sistemkepegawaian.service.dto.PegawaiDTO;
import com.mycompany.sistemkepegawaian.service.mapper.PegawaiMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PegawaiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PegawaiResourceIT {

    private static final String DEFAULT_NAMA = "AAAAAAAAAA";
    private static final String UPDATED_NAMA = "BBBBBBBBBB";

    private static final String DEFAULT_JABATAN = "AAAAAAAAAA";
    private static final String UPDATED_JABATAN = "BBBBBBBBBB";

    private static final String DEFAULT_DEPARTEMEN = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTEMEN = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_GAJI = new BigDecimal(1);
    private static final BigDecimal UPDATED_GAJI = new BigDecimal(2);
    private static final BigDecimal SMALLER_GAJI = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/pegawais";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PegawaiRepository pegawaiRepository;

    @Autowired
    private PegawaiMapper pegawaiMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPegawaiMockMvc;

    private Pegawai pegawai;

    private Pegawai insertedPegawai;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pegawai createEntity() {
        return new Pegawai().nama(DEFAULT_NAMA).jabatan(DEFAULT_JABATAN).departemen(DEFAULT_DEPARTEMEN).gaji(DEFAULT_GAJI);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pegawai createUpdatedEntity() {
        return new Pegawai().nama(UPDATED_NAMA).jabatan(UPDATED_JABATAN).departemen(UPDATED_DEPARTEMEN).gaji(UPDATED_GAJI);
    }

    @BeforeEach
    void initTest() {
        pegawai = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPegawai != null) {
            pegawaiRepository.delete(insertedPegawai);
            insertedPegawai = null;
        }
    }

    @Test
    @Transactional
    void createPegawai() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Pegawai
        PegawaiDTO pegawaiDTO = pegawaiMapper.toDto(pegawai);
        var returnedPegawaiDTO = om.readValue(
            restPegawaiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pegawaiDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PegawaiDTO.class
        );

        // Validate the Pegawai in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPegawai = pegawaiMapper.toEntity(returnedPegawaiDTO);
        assertPegawaiUpdatableFieldsEquals(returnedPegawai, getPersistedPegawai(returnedPegawai));

        insertedPegawai = returnedPegawai;
    }

    @Test
    @Transactional
    void createPegawaiWithExistingId() throws Exception {
        // Create the Pegawai with an existing ID
        pegawai.setId(1L);
        PegawaiDTO pegawaiDTO = pegawaiMapper.toDto(pegawai);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPegawaiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pegawaiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pegawai in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNamaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pegawai.setNama(null);

        // Create the Pegawai, which fails.
        PegawaiDTO pegawaiDTO = pegawaiMapper.toDto(pegawai);

        restPegawaiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pegawaiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkJabatanIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pegawai.setJabatan(null);

        // Create the Pegawai, which fails.
        PegawaiDTO pegawaiDTO = pegawaiMapper.toDto(pegawai);

        restPegawaiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pegawaiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDepartemenIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pegawai.setDepartemen(null);

        // Create the Pegawai, which fails.
        PegawaiDTO pegawaiDTO = pegawaiMapper.toDto(pegawai);

        restPegawaiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pegawaiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGajiIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pegawai.setGaji(null);

        // Create the Pegawai, which fails.
        PegawaiDTO pegawaiDTO = pegawaiMapper.toDto(pegawai);

        restPegawaiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pegawaiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPegawais() throws Exception {
        // Initialize the database
        insertedPegawai = pegawaiRepository.saveAndFlush(pegawai);

        // Get all the pegawaiList
        restPegawaiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pegawai.getId().intValue())))
            .andExpect(jsonPath("$.[*].nama").value(hasItem(DEFAULT_NAMA)))
            .andExpect(jsonPath("$.[*].jabatan").value(hasItem(DEFAULT_JABATAN)))
            .andExpect(jsonPath("$.[*].departemen").value(hasItem(DEFAULT_DEPARTEMEN)))
            .andExpect(jsonPath("$.[*].gaji").value(hasItem(sameNumber(DEFAULT_GAJI))));
    }

    @Test
    @Transactional
    void getPegawai() throws Exception {
        // Initialize the database
        insertedPegawai = pegawaiRepository.saveAndFlush(pegawai);

        // Get the pegawai
        restPegawaiMockMvc
            .perform(get(ENTITY_API_URL_ID, pegawai.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pegawai.getId().intValue()))
            .andExpect(jsonPath("$.nama").value(DEFAULT_NAMA))
            .andExpect(jsonPath("$.jabatan").value(DEFAULT_JABATAN))
            .andExpect(jsonPath("$.departemen").value(DEFAULT_DEPARTEMEN))
            .andExpect(jsonPath("$.gaji").value(sameNumber(DEFAULT_GAJI)));
    }

    @Test
    @Transactional
    void getPegawaisByIdFiltering() throws Exception {
        // Initialize the database
        insertedPegawai = pegawaiRepository.saveAndFlush(pegawai);

        Long id = pegawai.getId();

        defaultPegawaiFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPegawaiFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPegawaiFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPegawaisByNamaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPegawai = pegawaiRepository.saveAndFlush(pegawai);

        // Get all the pegawaiList where nama equals to
        defaultPegawaiFiltering("nama.equals=" + DEFAULT_NAMA, "nama.equals=" + UPDATED_NAMA);
    }

    @Test
    @Transactional
    void getAllPegawaisByNamaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPegawai = pegawaiRepository.saveAndFlush(pegawai);

        // Get all the pegawaiList where nama in
        defaultPegawaiFiltering("nama.in=" + DEFAULT_NAMA + "," + UPDATED_NAMA, "nama.in=" + UPDATED_NAMA);
    }

    @Test
    @Transactional
    void getAllPegawaisByNamaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPegawai = pegawaiRepository.saveAndFlush(pegawai);

        // Get all the pegawaiList where nama is not null
        defaultPegawaiFiltering("nama.specified=true", "nama.specified=false");
    }

    @Test
    @Transactional
    void getAllPegawaisByNamaContainsSomething() throws Exception {
        // Initialize the database
        insertedPegawai = pegawaiRepository.saveAndFlush(pegawai);

        // Get all the pegawaiList where nama contains
        defaultPegawaiFiltering("nama.contains=" + DEFAULT_NAMA, "nama.contains=" + UPDATED_NAMA);
    }

    @Test
    @Transactional
    void getAllPegawaisByNamaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPegawai = pegawaiRepository.saveAndFlush(pegawai);

        // Get all the pegawaiList where nama does not contain
        defaultPegawaiFiltering("nama.doesNotContain=" + UPDATED_NAMA, "nama.doesNotContain=" + DEFAULT_NAMA);
    }

    @Test
    @Transactional
    void getAllPegawaisByJabatanIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPegawai = pegawaiRepository.saveAndFlush(pegawai);

        // Get all the pegawaiList where jabatan equals to
        defaultPegawaiFiltering("jabatan.equals=" + DEFAULT_JABATAN, "jabatan.equals=" + UPDATED_JABATAN);
    }

    @Test
    @Transactional
    void getAllPegawaisByJabatanIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPegawai = pegawaiRepository.saveAndFlush(pegawai);

        // Get all the pegawaiList where jabatan in
        defaultPegawaiFiltering("jabatan.in=" + DEFAULT_JABATAN + "," + UPDATED_JABATAN, "jabatan.in=" + UPDATED_JABATAN);
    }

    @Test
    @Transactional
    void getAllPegawaisByJabatanIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPegawai = pegawaiRepository.saveAndFlush(pegawai);

        // Get all the pegawaiList where jabatan is not null
        defaultPegawaiFiltering("jabatan.specified=true", "jabatan.specified=false");
    }

    @Test
    @Transactional
    void getAllPegawaisByJabatanContainsSomething() throws Exception {
        // Initialize the database
        insertedPegawai = pegawaiRepository.saveAndFlush(pegawai);

        // Get all the pegawaiList where jabatan contains
        defaultPegawaiFiltering("jabatan.contains=" + DEFAULT_JABATAN, "jabatan.contains=" + UPDATED_JABATAN);
    }

    @Test
    @Transactional
    void getAllPegawaisByJabatanNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPegawai = pegawaiRepository.saveAndFlush(pegawai);

        // Get all the pegawaiList where jabatan does not contain
        defaultPegawaiFiltering("jabatan.doesNotContain=" + UPDATED_JABATAN, "jabatan.doesNotContain=" + DEFAULT_JABATAN);
    }

    @Test
    @Transactional
    void getAllPegawaisByDepartemenIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPegawai = pegawaiRepository.saveAndFlush(pegawai);

        // Get all the pegawaiList where departemen equals to
        defaultPegawaiFiltering("departemen.equals=" + DEFAULT_DEPARTEMEN, "departemen.equals=" + UPDATED_DEPARTEMEN);
    }

    @Test
    @Transactional
    void getAllPegawaisByDepartemenIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPegawai = pegawaiRepository.saveAndFlush(pegawai);

        // Get all the pegawaiList where departemen in
        defaultPegawaiFiltering("departemen.in=" + DEFAULT_DEPARTEMEN + "," + UPDATED_DEPARTEMEN, "departemen.in=" + UPDATED_DEPARTEMEN);
    }

    @Test
    @Transactional
    void getAllPegawaisByDepartemenIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPegawai = pegawaiRepository.saveAndFlush(pegawai);

        // Get all the pegawaiList where departemen is not null
        defaultPegawaiFiltering("departemen.specified=true", "departemen.specified=false");
    }

    @Test
    @Transactional
    void getAllPegawaisByDepartemenContainsSomething() throws Exception {
        // Initialize the database
        insertedPegawai = pegawaiRepository.saveAndFlush(pegawai);

        // Get all the pegawaiList where departemen contains
        defaultPegawaiFiltering("departemen.contains=" + DEFAULT_DEPARTEMEN, "departemen.contains=" + UPDATED_DEPARTEMEN);
    }

    @Test
    @Transactional
    void getAllPegawaisByDepartemenNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPegawai = pegawaiRepository.saveAndFlush(pegawai);

        // Get all the pegawaiList where departemen does not contain
        defaultPegawaiFiltering("departemen.doesNotContain=" + UPDATED_DEPARTEMEN, "departemen.doesNotContain=" + DEFAULT_DEPARTEMEN);
    }

    @Test
    @Transactional
    void getAllPegawaisByGajiIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPegawai = pegawaiRepository.saveAndFlush(pegawai);

        // Get all the pegawaiList where gaji equals to
        defaultPegawaiFiltering("gaji.equals=" + DEFAULT_GAJI, "gaji.equals=" + UPDATED_GAJI);
    }

    @Test
    @Transactional
    void getAllPegawaisByGajiIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPegawai = pegawaiRepository.saveAndFlush(pegawai);

        // Get all the pegawaiList where gaji in
        defaultPegawaiFiltering("gaji.in=" + DEFAULT_GAJI + "," + UPDATED_GAJI, "gaji.in=" + UPDATED_GAJI);
    }

    @Test
    @Transactional
    void getAllPegawaisByGajiIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPegawai = pegawaiRepository.saveAndFlush(pegawai);

        // Get all the pegawaiList where gaji is not null
        defaultPegawaiFiltering("gaji.specified=true", "gaji.specified=false");
    }

    @Test
    @Transactional
    void getAllPegawaisByGajiIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPegawai = pegawaiRepository.saveAndFlush(pegawai);

        // Get all the pegawaiList where gaji is greater than or equal to
        defaultPegawaiFiltering("gaji.greaterThanOrEqual=" + DEFAULT_GAJI, "gaji.greaterThanOrEqual=" + UPDATED_GAJI);
    }

    @Test
    @Transactional
    void getAllPegawaisByGajiIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPegawai = pegawaiRepository.saveAndFlush(pegawai);

        // Get all the pegawaiList where gaji is less than or equal to
        defaultPegawaiFiltering("gaji.lessThanOrEqual=" + DEFAULT_GAJI, "gaji.lessThanOrEqual=" + SMALLER_GAJI);
    }

    @Test
    @Transactional
    void getAllPegawaisByGajiIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPegawai = pegawaiRepository.saveAndFlush(pegawai);

        // Get all the pegawaiList where gaji is less than
        defaultPegawaiFiltering("gaji.lessThan=" + UPDATED_GAJI, "gaji.lessThan=" + DEFAULT_GAJI);
    }

    @Test
    @Transactional
    void getAllPegawaisByGajiIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPegawai = pegawaiRepository.saveAndFlush(pegawai);

        // Get all the pegawaiList where gaji is greater than
        defaultPegawaiFiltering("gaji.greaterThan=" + SMALLER_GAJI, "gaji.greaterThan=" + DEFAULT_GAJI);
    }

    private void defaultPegawaiFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPegawaiShouldBeFound(shouldBeFound);
        defaultPegawaiShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPegawaiShouldBeFound(String filter) throws Exception {
        restPegawaiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pegawai.getId().intValue())))
            .andExpect(jsonPath("$.[*].nama").value(hasItem(DEFAULT_NAMA)))
            .andExpect(jsonPath("$.[*].jabatan").value(hasItem(DEFAULT_JABATAN)))
            .andExpect(jsonPath("$.[*].departemen").value(hasItem(DEFAULT_DEPARTEMEN)))
            .andExpect(jsonPath("$.[*].gaji").value(hasItem(sameNumber(DEFAULT_GAJI))));

        // Check, that the count call also returns 1
        restPegawaiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPegawaiShouldNotBeFound(String filter) throws Exception {
        restPegawaiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPegawaiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPegawai() throws Exception {
        // Get the pegawai
        restPegawaiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPegawai() throws Exception {
        // Initialize the database
        insertedPegawai = pegawaiRepository.saveAndFlush(pegawai);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pegawai
        Pegawai updatedPegawai = pegawaiRepository.findById(pegawai.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPegawai are not directly saved in db
        em.detach(updatedPegawai);
        updatedPegawai.nama(UPDATED_NAMA).jabatan(UPDATED_JABATAN).departemen(UPDATED_DEPARTEMEN).gaji(UPDATED_GAJI);
        PegawaiDTO pegawaiDTO = pegawaiMapper.toDto(updatedPegawai);

        restPegawaiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pegawaiDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pegawaiDTO))
            )
            .andExpect(status().isOk());

        // Validate the Pegawai in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPegawaiToMatchAllProperties(updatedPegawai);
    }

    @Test
    @Transactional
    void putNonExistingPegawai() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pegawai.setId(longCount.incrementAndGet());

        // Create the Pegawai
        PegawaiDTO pegawaiDTO = pegawaiMapper.toDto(pegawai);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPegawaiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pegawaiDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pegawaiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pegawai in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPegawai() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pegawai.setId(longCount.incrementAndGet());

        // Create the Pegawai
        PegawaiDTO pegawaiDTO = pegawaiMapper.toDto(pegawai);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPegawaiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pegawaiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pegawai in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPegawai() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pegawai.setId(longCount.incrementAndGet());

        // Create the Pegawai
        PegawaiDTO pegawaiDTO = pegawaiMapper.toDto(pegawai);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPegawaiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pegawaiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pegawai in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePegawaiWithPatch() throws Exception {
        // Initialize the database
        insertedPegawai = pegawaiRepository.saveAndFlush(pegawai);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pegawai using partial update
        Pegawai partialUpdatedPegawai = new Pegawai();
        partialUpdatedPegawai.setId(pegawai.getId());

        partialUpdatedPegawai.jabatan(UPDATED_JABATAN);

        restPegawaiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPegawai.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPegawai))
            )
            .andExpect(status().isOk());

        // Validate the Pegawai in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPegawaiUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPegawai, pegawai), getPersistedPegawai(pegawai));
    }

    @Test
    @Transactional
    void fullUpdatePegawaiWithPatch() throws Exception {
        // Initialize the database
        insertedPegawai = pegawaiRepository.saveAndFlush(pegawai);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pegawai using partial update
        Pegawai partialUpdatedPegawai = new Pegawai();
        partialUpdatedPegawai.setId(pegawai.getId());

        partialUpdatedPegawai.nama(UPDATED_NAMA).jabatan(UPDATED_JABATAN).departemen(UPDATED_DEPARTEMEN).gaji(UPDATED_GAJI);

        restPegawaiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPegawai.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPegawai))
            )
            .andExpect(status().isOk());

        // Validate the Pegawai in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPegawaiUpdatableFieldsEquals(partialUpdatedPegawai, getPersistedPegawai(partialUpdatedPegawai));
    }

    @Test
    @Transactional
    void patchNonExistingPegawai() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pegawai.setId(longCount.incrementAndGet());

        // Create the Pegawai
        PegawaiDTO pegawaiDTO = pegawaiMapper.toDto(pegawai);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPegawaiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pegawaiDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pegawaiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pegawai in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPegawai() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pegawai.setId(longCount.incrementAndGet());

        // Create the Pegawai
        PegawaiDTO pegawaiDTO = pegawaiMapper.toDto(pegawai);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPegawaiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pegawaiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pegawai in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPegawai() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pegawai.setId(longCount.incrementAndGet());

        // Create the Pegawai
        PegawaiDTO pegawaiDTO = pegawaiMapper.toDto(pegawai);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPegawaiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pegawaiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pegawai in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePegawai() throws Exception {
        // Initialize the database
        insertedPegawai = pegawaiRepository.saveAndFlush(pegawai);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pegawai
        restPegawaiMockMvc
            .perform(delete(ENTITY_API_URL_ID, pegawai.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pegawaiRepository.count();
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

    protected Pegawai getPersistedPegawai(Pegawai pegawai) {
        return pegawaiRepository.findById(pegawai.getId()).orElseThrow();
    }

    protected void assertPersistedPegawaiToMatchAllProperties(Pegawai expectedPegawai) {
        assertPegawaiAllPropertiesEquals(expectedPegawai, getPersistedPegawai(expectedPegawai));
    }

    protected void assertPersistedPegawaiToMatchUpdatableProperties(Pegawai expectedPegawai) {
        assertPegawaiAllUpdatablePropertiesEquals(expectedPegawai, getPersistedPegawai(expectedPegawai));
    }
}
