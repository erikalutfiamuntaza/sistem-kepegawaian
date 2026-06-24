package com.mycompany.sistemkepegawaian.web.rest;

import com.mycompany.sistemkepegawaian.repository.PegawaiRepository;
import com.mycompany.sistemkepegawaian.service.PegawaiQueryService;
import com.mycompany.sistemkepegawaian.service.PegawaiService;
import com.mycompany.sistemkepegawaian.service.criteria.PegawaiCriteria;
import com.mycompany.sistemkepegawaian.service.dto.PegawaiDTO;
import com.mycompany.sistemkepegawaian.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.sistemkepegawaian.domain.Pegawai}.
 */
@RestController
@RequestMapping("/api/pegawais")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class PegawaiResource {

    private static final Logger LOG = LoggerFactory.getLogger(PegawaiResource.class);

    private static final String ENTITY_NAME = "pegawai";

    @Value("${jhipster.clientApp.name:sistemKepegawaian}")
    private String applicationName;

    private final PegawaiService pegawaiService;

    private final PegawaiRepository pegawaiRepository;

    private final PegawaiQueryService pegawaiQueryService;

    public PegawaiResource(PegawaiService pegawaiService, PegawaiRepository pegawaiRepository, PegawaiQueryService pegawaiQueryService) {
        this.pegawaiService = pegawaiService;
        this.pegawaiRepository = pegawaiRepository;
        this.pegawaiQueryService = pegawaiQueryService;
    }

    /**
     * {@code POST  /pegawais} : Create a new pegawai.
     *
     * @param pegawaiDTO the pegawaiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pegawaiDTO, or with status {@code 400 (Bad Request)} if the pegawai has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_HRD')")
    public ResponseEntity<PegawaiDTO> createPegawai(@Valid @RequestBody PegawaiDTO pegawaiDTO) throws URISyntaxException {
        LOG.debug("REST request to save Pegawai : {}", pegawaiDTO);
        if (pegawaiDTO.getId() != null) {
            throw new BadRequestAlertException("A new pegawai cannot already have an ID", ENTITY_NAME, "idexists");
        }
        pegawaiDTO = pegawaiService.save(pegawaiDTO);
        return ResponseEntity.created(new URI("/api/pegawais/" + pegawaiDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, pegawaiDTO.getId().toString()))
            .body(pegawaiDTO);
    }

    /**
     * {@code PUT  /pegawais/:id} : Updates an existing pegawai.
     *
     * @param id the id of the pegawaiDTO to save.
     * @param pegawaiDTO the pegawaiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pegawaiDTO,
     * or with status {@code 400 (Bad Request)} if the pegawaiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pegawaiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_HRD')")
    public ResponseEntity<PegawaiDTO> updatePegawai(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PegawaiDTO pegawaiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Pegawai : {}, {}", id, pegawaiDTO);
        if (pegawaiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pegawaiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pegawaiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        pegawaiDTO = pegawaiService.update(pegawaiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pegawaiDTO.getId().toString()))
            .body(pegawaiDTO);
    }

    /**
     * {@code PATCH  /pegawais/:id} : Partial updates given fields of an existing pegawai, field will ignore if it is null
     *
     * @param id the id of the pegawaiDTO to save.
     * @param pegawaiDTO the pegawaiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pegawaiDTO,
     * or with status {@code 400 (Bad Request)} if the pegawaiDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pegawaiDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pegawaiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PegawaiDTO> partialUpdatePegawai(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PegawaiDTO pegawaiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Pegawai partially : {}, {}", id, pegawaiDTO);
        if (pegawaiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pegawaiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pegawaiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PegawaiDTO> result = pegawaiService.partialUpdate(pegawaiDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pegawaiDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /pegawais} : get all the Pegawais.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Pegawais in body.
     */
    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_HRD','ROLE_MANAGER','ROLE_EMPLOYEE')")
    public ResponseEntity<List<PegawaiDTO>> getAllPegawais(
        PegawaiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Pegawais by criteria: {}", criteria);

        Page<PegawaiDTO> page = pegawaiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pegawais/count} : count all the pegawais.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPegawais(PegawaiCriteria criteria) {
        LOG.debug("REST request to count Pegawais by criteria: {}", criteria);
        return ResponseEntity.ok().body(pegawaiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /pegawais/:id} : get the "id" pegawai.
     *
     * @param id the id of the pegawaiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pegawaiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PegawaiDTO> getPegawai(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Pegawai : {}", id);
        Optional<PegawaiDTO> pegawaiDTO = pegawaiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pegawaiDTO);
    }

    /**
     * {@code DELETE  /pegawais/:id} : delete the "id" pegawai.
     *
     * @param id the id of the pegawaiDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_HRD')")
    public ResponseEntity<Void> deletePegawai(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Pegawai : {}", id);
        pegawaiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
