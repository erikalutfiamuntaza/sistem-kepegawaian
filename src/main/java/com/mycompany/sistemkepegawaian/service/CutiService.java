package com.mycompany.sistemkepegawaian.service;

import com.mycompany.sistemkepegawaian.domain.Cuti;
import com.mycompany.sistemkepegawaian.repository.CutiRepository;
import com.mycompany.sistemkepegawaian.service.dto.CutiDTO;
import com.mycompany.sistemkepegawaian.service.mapper.CutiMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.sistemkepegawaian.domain.Cuti}.
 */
@Service
@Transactional
public class CutiService {

    private static final Logger LOG = LoggerFactory.getLogger(CutiService.class);

    private final CutiRepository cutiRepository;

    private final CutiMapper cutiMapper;

    public CutiService(CutiRepository cutiRepository, CutiMapper cutiMapper) {
        this.cutiRepository = cutiRepository;
        this.cutiMapper = cutiMapper;
    }

    /**
     * Save a cuti.
     *
     * @param cutiDTO the entity to save.
     * @return the persisted entity.
     */
    public CutiDTO save(CutiDTO cutiDTO) {
        LOG.debug("Request to save Cuti : {}", cutiDTO);
        Cuti cuti = cutiMapper.toEntity(cutiDTO);
        cuti = cutiRepository.save(cuti);
        return cutiMapper.toDto(cuti);
    }

    /**
     * Update a cuti.
     *
     * @param cutiDTO the entity to save.
     * @return the persisted entity.
     */
    public CutiDTO update(CutiDTO cutiDTO) {
        LOG.debug("Request to update Cuti : {}", cutiDTO);
        Cuti cuti = cutiMapper.toEntity(cutiDTO);
        cuti = cutiRepository.save(cuti);
        return cutiMapper.toDto(cuti);
    }

    /**
     * Partially update a cuti.
     *
     * @param cutiDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CutiDTO> partialUpdate(CutiDTO cutiDTO) {
        LOG.debug("Request to partially update Cuti : {}", cutiDTO);

        return cutiRepository
            .findById(cutiDTO.getId())
            .map(existingCuti -> {
                cutiMapper.partialUpdate(existingCuti, cutiDTO);

                return existingCuti;
            })
            .map(cutiRepository::save)
            .map(cutiMapper::toDto);
    }

    /**
     * Get all the cutis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CutiDTO> findAllWithEagerRelationships(Pageable pageable) {
        return cutiRepository.findAllWithEagerRelationships(pageable).map(cutiMapper::toDto);
    }

    /**
     * Get one cuti by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CutiDTO> findOne(Long id) {
        LOG.debug("Request to get Cuti : {}", id);
        return cutiRepository.findOneWithEagerRelationships(id).map(cutiMapper::toDto);
    }

    /**
     * Delete the cuti by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Cuti : {}", id);
        cutiRepository.deleteById(id);
    }
}
