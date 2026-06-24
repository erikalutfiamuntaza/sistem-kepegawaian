package com.mycompany.sistemkepegawaian.service;

import com.mycompany.sistemkepegawaian.domain.*; // for static metamodels
import com.mycompany.sistemkepegawaian.domain.Penggajian;
import com.mycompany.sistemkepegawaian.repository.PenggajianRepository;
import com.mycompany.sistemkepegawaian.service.criteria.PenggajianCriteria;
import com.mycompany.sistemkepegawaian.service.dto.PenggajianDTO;
import com.mycompany.sistemkepegawaian.service.mapper.PenggajianMapper;
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
 * Service for executing complex queries for {@link Penggajian} entities in the database.
 * The main input is a {@link PenggajianCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PenggajianDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PenggajianQueryService extends QueryService<Penggajian> {

    private static final Logger LOG = LoggerFactory.getLogger(PenggajianQueryService.class);

    private final PenggajianRepository penggajianRepository;

    private final PenggajianMapper penggajianMapper;

    public PenggajianQueryService(PenggajianRepository penggajianRepository, PenggajianMapper penggajianMapper) {
        this.penggajianRepository = penggajianRepository;
        this.penggajianMapper = penggajianMapper;
    }

    /**
     * Return a {@link Page} of {@link PenggajianDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PenggajianDTO> findByCriteria(PenggajianCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Penggajian> specification = createSpecification(criteria);
        return penggajianRepository.findAll(specification, page).map(penggajianMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PenggajianCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Penggajian> specification = createSpecification(criteria);
        return penggajianRepository.count(specification);
    }

    /**
     * Function to convert {@link PenggajianCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Penggajian> createSpecification(PenggajianCriteria criteria) {
        Specification<Penggajian> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(Penggajian_.pegawai, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), Penggajian_.id),
                    buildRangeSpecification(criteria.getBulan(), Penggajian_.bulan),
                    buildRangeSpecification(criteria.getGajiPokok(), Penggajian_.gajiPokok),
                    buildRangeSpecification(criteria.getBonus(), Penggajian_.bonus),
                    buildRangeSpecification(criteria.getPotongan(), Penggajian_.potongan),
                    buildRangeSpecification(criteria.getTotalGaji(), Penggajian_.totalGaji),
                    buildSpecification(criteria.getPegawaiId(), root -> root.join(Penggajian_.pegawai, JoinType.LEFT).get(Pegawai_.id))
                )
            );
        }
        return specification;
    }
}
