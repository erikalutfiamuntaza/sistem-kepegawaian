package com.mycompany.sistemkepegawaian.web.rest;

import com.mycompany.sistemkepegawaian.repository.PenggajianRepository;
import com.mycompany.sistemkepegawaian.service.PenggajianQueryService;
import com.mycompany.sistemkepegawaian.service.PenggajianService;
import com.mycompany.sistemkepegawaian.service.criteria.PenggajianCriteria;
import com.mycompany.sistemkepegawaian.service.dto.PenggajianDTO;
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
 * REST controller for managing {@link com.mycompany.sistemkepegawaian.domain.Penggajian}.
 */
@RestController
@RequestMapping("/api/penggajians")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_HRD')")
public class PenggajianResource {

    private static final Logger LOG = LoggerFactory.getLogger(PenggajianResource.class);

    private static final String ENTITY_NAME = "penggajian";

    @Value("${jhipster.clientApp.name:sistemKepegawaian}")
    private String applicationName;

    private final PenggajianService penggajianService;

    private final PenggajianRepository penggajianRepository;

    private final PenggajianQueryService penggajianQueryService;

    public PenggajianResource(
        PenggajianService penggajianService,
        PenggajianRepository penggajianRepository,
        PenggajianQueryService penggajianQueryService
    ) {
        this.penggajianService = penggajianService;
        this.penggajianRepository = penggajianRepository;
        this.penggajianQueryService = penggajianQueryService;
    }

    /**
     * {@code POST  /penggajians} : Create a new penggajian.
     *
     * @param penggajianDTO the penggajianDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new penggajianDTO, or with status {@code 400 (Bad Request)} if the penggajian has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @PreAuthorize("denyAll()")
    public ResponseEntity<PenggajianDTO> createPenggajian(@Valid @RequestBody PenggajianDTO penggajianDTO) throws URISyntaxException {
        LOG.debug("REST request to save Penggajian : {}", penggajianDTO);
        if (penggajianDTO.getId() != null) {
            throw new BadRequestAlertException("A new penggajian cannot already have an ID", ENTITY_NAME, "idexists");
        }
        penggajianDTO = penggajianService.save(penggajianDTO);
        return ResponseEntity.created(new URI("/api/penggajians/" + penggajianDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, penggajianDTO.getId().toString()))
            .body(penggajianDTO);
    }

    /**
     * {@code PUT  /penggajians/:id} : Updates an existing penggajian.
     *
     * @param id the id of the penggajianDTO to save.
     * @param penggajianDTO the penggajianDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated penggajianDTO,
     * or with status {@code 400 (Bad Request)} if the penggajianDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the penggajianDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_HRD')")
    public ResponseEntity<PenggajianDTO> updatePenggajian(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PenggajianDTO penggajianDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Penggajian : {}, {}", id, penggajianDTO);
        if (penggajianDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, penggajianDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!penggajianRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        penggajianDTO = penggajianService.update(penggajianDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, penggajianDTO.getId().toString()))
            .body(penggajianDTO);
    }

    /**
     * {@code PATCH  /penggajians/:id} : Partial updates given fields of an existing penggajian, field will ignore if it is null
     *
     * @param id the id of the penggajianDTO to save.
     * @param penggajianDTO the penggajianDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated penggajianDTO,
     * or with status {@code 400 (Bad Request)} if the penggajianDTO is not valid,
     * or with status {@code 404 (Not Found)} if the penggajianDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the penggajianDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PenggajianDTO> partialUpdatePenggajian(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PenggajianDTO penggajianDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Penggajian partially : {}, {}", id, penggajianDTO);
        if (penggajianDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, penggajianDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!penggajianRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PenggajianDTO> result = penggajianService.partialUpdate(penggajianDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, penggajianDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /penggajians} : get all the Penggajians.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Penggajians in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PenggajianDTO>> getAllPenggajians(
        PenggajianCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Penggajians by criteria: {}", criteria);

        Page<PenggajianDTO> page = penggajianQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /penggajians/count} : count all the penggajians.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_HRD')")
    public ResponseEntity<Long> countPenggajians(PenggajianCriteria criteria) {
        LOG.debug("REST request to count Penggajians by criteria: {}", criteria);
        return ResponseEntity.ok().body(penggajianQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /penggajians/:id} : get the "id" penggajian.
     *
     * @param id the id of the penggajianDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the penggajianDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PenggajianDTO> getPenggajian(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Penggajian : {}", id);
        Optional<PenggajianDTO> penggajianDTO = penggajianService.findOne(id);
        return ResponseUtil.wrapOrNotFound(penggajianDTO);
    }

    /**
     * {@code DELETE  /penggajians/:id} : delete the "id" penggajian.
     *
     * @param id the id of the penggajianDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_HRD')")
    public ResponseEntity<Void> deletePenggajian(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Penggajian : {}", id);
        penggajianService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
