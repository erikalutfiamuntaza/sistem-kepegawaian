package com.mycompany.sistemkepegawaian.service;

import com.mycompany.sistemkepegawaian.domain.*; // for static metamodels
import com.mycompany.sistemkepegawaian.domain.Absensi;
import com.mycompany.sistemkepegawaian.repository.AbsensiRepository;
import com.mycompany.sistemkepegawaian.service.criteria.AbsensiCriteria;
import com.mycompany.sistemkepegawaian.service.dto.AbsensiDTO;
import com.mycompany.sistemkepegawaian.service.mapper.AbsensiMapper;
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
 * Service for executing complex queries for {@link Absensi} entities in the database.
 * The main input is a {@link AbsensiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AbsensiDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AbsensiQueryService extends QueryService<Absensi> {

    private static final Logger LOG = LoggerFactory.getLogger(AbsensiQueryService.class);

    private final AbsensiRepository absensiRepository;

    private final AbsensiMapper absensiMapper;

    public AbsensiQueryService(AbsensiRepository absensiRepository, AbsensiMapper absensiMapper) {
        this.absensiRepository = absensiRepository;
        this.absensiMapper = absensiMapper;
    }

    /**
     * Return a {@link Page} of {@link AbsensiDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AbsensiDTO> findByCriteria(AbsensiCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Absensi> specification = createSpecification(criteria);
        return absensiRepository.findAll(specification, page).map(absensiMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AbsensiCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Absensi> specification = createSpecification(criteria);
        return absensiRepository.count(specification);
    }

    /**
     * Function to convert {@link AbsensiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Absensi> createSpecification(AbsensiCriteria criteria) {
        Specification<Absensi> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(Absensi_.pegawai, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), Absensi_.id),
                    buildRangeSpecification(criteria.getTanggal(), Absensi_.tanggal),
                    buildRangeSpecification(criteria.getJamMasuk(), Absensi_.jamMasuk),
                    buildRangeSpecification(criteria.getJamKeluar(), Absensi_.jamKeluar),
                    buildStringSpecification(criteria.getStatus(), Absensi_.status),
                    buildSpecification(criteria.getPegawaiId(), root -> root.join(Absensi_.pegawai, JoinType.LEFT).get(Pegawai_.id))
                )
            );
        }
        return specification;
    }
}
