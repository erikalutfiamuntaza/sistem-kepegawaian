package com.mycompany.sistemkepegawaian.web.rest;

import com.mycompany.sistemkepegawaian.repository.AbsensiRepository;
import com.mycompany.sistemkepegawaian.service.AbsensiQueryService;
import com.mycompany.sistemkepegawaian.service.AbsensiService;
import com.mycompany.sistemkepegawaian.service.criteria.AbsensiCriteria;
import com.mycompany.sistemkepegawaian.service.dto.AbsensiDTO;
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
 * REST controller for managing {@link com.mycompany.sistemkepegawaian.domain.Absensi}.
 */
@RestController
@RequestMapping("/api/absensis")
public class AbsensiResource {

    private static final Logger LOG = LoggerFactory.getLogger(AbsensiResource.class);

    private static final String ENTITY_NAME = "absensi";

    @Value("${jhipster.clientApp.name:sistemKepegawaian}")
    private String applicationName;

    private final AbsensiService absensiService;

    private final AbsensiRepository absensiRepository;

    private final AbsensiQueryService absensiQueryService;

    public AbsensiResource(AbsensiService absensiService, AbsensiRepository absensiRepository, AbsensiQueryService absensiQueryService) {
        this.absensiService = absensiService;
        this.absensiRepository = absensiRepository;
        this.absensiQueryService = absensiQueryService;
    }

    /**
     * {@code POST  /absensis} : Create a new absensi.
     *
     * @param absensiDTO the absensiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new absensiDTO, or with status {@code 400 (Bad Request)} if the absensi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AbsensiDTO> createAbsensi(@Valid @RequestBody AbsensiDTO absensiDTO) throws URISyntaxException {
        LOG.debug("REST request to save Absensi : {}", absensiDTO);
        if (absensiDTO.getId() != null) {
            throw new BadRequestAlertException("A new absensi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        absensiDTO = absensiService.save(absensiDTO);
        return ResponseEntity.created(new URI("/api/absensis/" + absensiDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, absensiDTO.getId().toString()))
            .body(absensiDTO);
    }

    /**
     * {@code PUT  /absensis/:id} : Updates an existing absensi.
     *
     * @param id the id of the absensiDTO to save.
     * @param absensiDTO the absensiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated absensiDTO,
     * or with status {@code 400 (Bad Request)} if the absensiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the absensiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AbsensiDTO> updateAbsensi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AbsensiDTO absensiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Absensi : {}, {}", id, absensiDTO);
        if (absensiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, absensiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!absensiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        absensiDTO = absensiService.update(absensiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, absensiDTO.getId().toString()))
            .body(absensiDTO);
    }

    /**
     * {@code PATCH  /absensis/:id} : Partial updates given fields of an existing absensi, field will ignore if it is null
     *
     * @param id the id of the absensiDTO to save.
     * @param absensiDTO the absensiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated absensiDTO,
     * or with status {@code 400 (Bad Request)} if the absensiDTO is not valid,
     * or with status {@code 404 (Not Found)} if the absensiDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the absensiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AbsensiDTO> partialUpdateAbsensi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AbsensiDTO absensiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Absensi partially : {}, {}", id, absensiDTO);
        if (absensiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, absensiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!absensiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AbsensiDTO> result = absensiService.partialUpdate(absensiDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, absensiDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /absensis} : get all the Absensis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Absensis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AbsensiDTO>> getAllAbsensis(
        AbsensiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Absensis by criteria: {}", criteria);

        Page<AbsensiDTO> page = absensiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /absensis/count} : count all the absensis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAbsensis(AbsensiCriteria criteria) {
        LOG.debug("REST request to count Absensis by criteria: {}", criteria);
        return ResponseEntity.ok().body(absensiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /absensis/:id} : get the "id" absensi.
     *
     * @param id the id of the absensiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the absensiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AbsensiDTO> getAbsensi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Absensi : {}", id);
        Optional<AbsensiDTO> absensiDTO = absensiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(absensiDTO);
    }

    /**
     * {@code DELETE  /absensis/:id} : delete the "id" absensi.
     *
     * @param id the id of the absensiDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAbsensi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Absensi : {}", id);
        absensiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
