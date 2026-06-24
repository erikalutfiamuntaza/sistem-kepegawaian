package com.mycompany.sistemkepegawaian.service;

import com.mycompany.sistemkepegawaian.domain.*; // for static metamodels
import com.mycompany.sistemkepegawaian.domain.Cuti;
import com.mycompany.sistemkepegawaian.repository.CutiRepository;
import com.mycompany.sistemkepegawaian.service.criteria.CutiCriteria;
import com.mycompany.sistemkepegawaian.service.dto.CutiDTO;
import com.mycompany.sistemkepegawaian.service.mapper.CutiMapper;
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
 * Service for executing complex queries for {@link Cuti} entities in the database.
 * The main input is a {@link CutiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CutiDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CutiQueryService extends QueryService<Cuti> {

    private static final Logger LOG = LoggerFactory.getLogger(CutiQueryService.class);

    private final CutiRepository cutiRepository;

    private final CutiMapper cutiMapper;

    public CutiQueryService(CutiRepository cutiRepository, CutiMapper cutiMapper) {
        this.cutiRepository = cutiRepository;
        this.cutiMapper = cutiMapper;
    }

    /**
     * Return a {@link Page} of {@link CutiDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CutiDTO> findByCriteria(CutiCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Cuti> specification = createSpecification(criteria);
        return cutiRepository.findAll(specification, page).map(cutiMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CutiCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Cuti> specification = createSpecification(criteria);
        return cutiRepository.count(specification);
    }

    /**
     * Function to convert {@link CutiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Cuti> createSpecification(CutiCriteria criteria) {
        Specification<Cuti> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(Cuti_.pegawai, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), Cuti_.id),
                    buildRangeSpecification(criteria.getTanggalMulai(), Cuti_.tanggalMulai),
                    buildRangeSpecification(criteria.getTanggalSelesai(), Cuti_.tanggalSelesai),
                    buildStringSpecification(criteria.getAlasan(), Cuti_.alasan),
                    buildStringSpecification(criteria.getStatus(), Cuti_.status),
                    buildSpecification(criteria.getPegawaiId(), root -> root.join(Cuti_.pegawai, JoinType.LEFT).get(Pegawai_.id))
                )
            );
        }
        return specification;
    }
}
