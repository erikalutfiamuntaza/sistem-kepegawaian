package com.mycompany.sistemkepegawaian.service;

import com.mycompany.sistemkepegawaian.domain.*; // for static metamodels
import com.mycompany.sistemkepegawaian.domain.Pegawai;
import com.mycompany.sistemkepegawaian.repository.PegawaiRepository;
import com.mycompany.sistemkepegawaian.service.criteria.PegawaiCriteria;
import com.mycompany.sistemkepegawaian.service.dto.PegawaiDTO;
import com.mycompany.sistemkepegawaian.service.mapper.PegawaiMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Pegawai} entities in the database.
 * The main input is a {@link PegawaiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PegawaiDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PegawaiQueryService extends QueryService<Pegawai> {

    private static final Logger LOG = LoggerFactory.getLogger(PegawaiQueryService.class);

    private final PegawaiRepository pegawaiRepository;

    private final PegawaiMapper pegawaiMapper;

    public PegawaiQueryService(PegawaiRepository pegawaiRepository, PegawaiMapper pegawaiMapper) {
        this.pegawaiRepository = pegawaiRepository;
        this.pegawaiMapper = pegawaiMapper;
    }

    /**
     * Return a {@link Page} of {@link PegawaiDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PegawaiDTO> findByCriteria(PegawaiCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Pegawai> specification = createSpecification(criteria);
        return pegawaiRepository.findAll(specification, page).map(pegawaiMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PegawaiCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Pegawai> specification = createSpecification(criteria);
        return pegawaiRepository.count(specification);
    }

    /**
     * Function to convert {@link PegawaiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Pegawai> createSpecification(PegawaiCriteria criteria) {
        Specification<Pegawai> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), Pegawai_.id),
                    buildStringSpecification(criteria.getNama(), Pegawai_.nama),
                    buildStringSpecification(criteria.getJabatan(), Pegawai_.jabatan),
                    buildStringSpecification(criteria.getDepartemen(), Pegawai_.departemen),
                    buildRangeSpecification(criteria.getGaji(), Pegawai_.gaji),
                    buildSpecification(criteria.getCutiId(), root -> root.join(Pegawai_.cutis, JoinType.LEFT).get(Cuti_.id)),
                    buildSpecification(criteria.getPenggajianId(), root ->
                        root.join(Pegawai_.penggajians, JoinType.LEFT).get(Penggajian_.id)
                    ),
                    buildSpecification(criteria.getAbsensiId(), root -> root.join(Pegawai_.absensis, JoinType.LEFT).get(Absensi_.id))
                )
            );
        }
        return specification;
    }
}
