package com.mycompany.sistemkepegawaian.web.rest;

import com.mycompany.sistemkepegawaian.repository.CutiRepository;
import com.mycompany.sistemkepegawaian.service.CutiQueryService;
import com.mycompany.sistemkepegawaian.service.CutiService;
import com.mycompany.sistemkepegawaian.service.criteria.CutiCriteria;
import com.mycompany.sistemkepegawaian.service.dto.CutiDTO;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.sistemkepegawaian.domain.Cuti}.
 */
@RestController
@RequestMapping("/api/cutis")
public class CutiResource {

    private static final Logger LOG = LoggerFactory.getLogger(CutiResource.class);

    private static final String ENTITY_NAME = "cuti";

    @Value("${jhipster.clientApp.name:sistemKepegawaian}")
    private String applicationName;

    private final CutiService cutiService;

    private final CutiRepository cutiRepository;

    private final CutiQueryService cutiQueryService;

    public CutiResource(CutiService cutiService, CutiRepository cutiRepository, CutiQueryService cutiQueryService) {
        this.cutiService = cutiService;
        this.cutiRepository = cutiRepository;
        this.cutiQueryService = cutiQueryService;
    }

    /**
     * {@code POST  /cutis} : Create a new cuti.
     *
     * @param cutiDTO the cutiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cutiDTO, or with status {@code 400 (Bad Request)} if the cuti has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CutiDTO> createCuti(@Valid @RequestBody CutiDTO cutiDTO) throws URISyntaxException {
        LOG.debug("REST request to save Cuti : {}", cutiDTO);
        if (cutiDTO.getId() != null) {
            throw new BadRequestAlertException("A new cuti cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cutiDTO = cutiService.save(cutiDTO);
        return ResponseEntity.created(new URI("/api/cutis/" + cutiDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cutiDTO.getId().toString()))
            .body(cutiDTO);
    }

    /**
     * {@code PUT  /cutis/:id} : Updates an existing cuti.
     *
     * @param id the id of the cutiDTO to save.
     * @param cutiDTO the cutiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cutiDTO,
     * or with status {@code 400 (Bad Request)} if the cutiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cutiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CutiDTO> updateCuti(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CutiDTO cutiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Cuti : {}, {}", id, cutiDTO);
        if (cutiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cutiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cutiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cutiDTO = cutiService.update(cutiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cutiDTO.getId().toString()))
            .body(cutiDTO);
    }

    /**
     * {@code PATCH  /cutis/:id} : Partial updates given fields of an existing cuti, field will ignore if it is null
     *
     * @param id the id of the cutiDTO to save.
     * @param cutiDTO the cutiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cutiDTO,
     * or with status {@code 400 (Bad Request)} if the cutiDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cutiDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cutiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CutiDTO> partialUpdateCuti(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CutiDTO cutiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Cuti partially : {}, {}", id, cutiDTO);
        if (cutiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cutiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cutiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CutiDTO> result = cutiService.partialUpdate(cutiDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cutiDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cutis} : get all the Cutis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Cutis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CutiDTO>> getAllCutis(
        CutiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Cutis by criteria: {}", criteria);

        Page<CutiDTO> page = cutiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cutis/count} : count all the cutis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCutis(CutiCriteria criteria) {
        LOG.debug("REST request to count Cutis by criteria: {}", criteria);
        return ResponseEntity.ok().body(cutiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cutis/:id} : get the "id" cuti.
     *
     * @param id the id of the cutiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cutiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CutiDTO> getCuti(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Cuti : {}", id);
        Optional<CutiDTO> cutiDTO = cutiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cutiDTO);
    }

    /**
     * {@code DELETE  /cutis/:id} : delete the "id" cuti.
     *
     * @param id the id of the cutiDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCuti(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Cuti : {}", id);
        cutiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
