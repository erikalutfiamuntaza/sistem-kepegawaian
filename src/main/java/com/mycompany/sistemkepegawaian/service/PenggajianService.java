package com.mycompany.sistemkepegawaian.service;

import com.mycompany.sistemkepegawaian.domain.Penggajian;
import com.mycompany.sistemkepegawaian.repository.PenggajianRepository;
import com.mycompany.sistemkepegawaian.service.dto.PenggajianDTO;
import com.mycompany.sistemkepegawaian.service.mapper.PenggajianMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.sistemkepegawaian.domain.Penggajian}.
 */
@Service
@Transactional
public class PenggajianService {

    private static final Logger LOG = LoggerFactory.getLogger(PenggajianService.class);

    private final PenggajianRepository penggajianRepository;

    private final PenggajianMapper penggajianMapper;

    public PenggajianService(PenggajianRepository penggajianRepository, PenggajianMapper penggajianMapper) {
        this.penggajianRepository = penggajianRepository;
        this.penggajianMapper = penggajianMapper;
    }

    /**
     * Save a penggajian.
     *
     * @param penggajianDTO the entity to save.
     * @return the persisted entity.
     */
    public PenggajianDTO save(PenggajianDTO penggajianDTO) {
        LOG.debug("Request to save Penggajian : {}", penggajianDTO);
        Penggajian penggajian = penggajianMapper.toEntity(penggajianDTO);
        penggajian = penggajianRepository.save(penggajian);
        return penggajianMapper.toDto(penggajian);
    }

    /**
     * Update a penggajian.
     *
     * @param penggajianDTO the entity to save.
     * @return the persisted entity.
     */
    public PenggajianDTO update(PenggajianDTO penggajianDTO) {
        LOG.debug("Request to update Penggajian : {}", penggajianDTO);
        Penggajian penggajian = penggajianMapper.toEntity(penggajianDTO);
        penggajian = penggajianRepository.save(penggajian);
        return penggajianMapper.toDto(penggajian);
    }

    /**
     * Partially update a penggajian.
     *
     * @param penggajianDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PenggajianDTO> partialUpdate(PenggajianDTO penggajianDTO) {
        LOG.debug("Request to partially update Penggajian : {}", penggajianDTO);

        return penggajianRepository
            .findById(penggajianDTO.getId())
            .map(existingPenggajian -> {
                penggajianMapper.partialUpdate(existingPenggajian, penggajianDTO);

                return existingPenggajian;
            })
            .map(penggajianRepository::save)
            .map(penggajianMapper::toDto);
    }

    /**
     * Get all the penggajians with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PenggajianDTO> findAllWithEagerRelationships(Pageable pageable) {
        return penggajianRepository.findAllWithEagerRelationships(pageable).map(penggajianMapper::toDto);
    }

    /**
     * Get one penggajian by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PenggajianDTO> findOne(Long id) {
        LOG.debug("Request to get Penggajian : {}", id);
        return penggajianRepository.findOneWithEagerRelationships(id).map(penggajianMapper::toDto);
    }

    /**
     * Delete the penggajian by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Penggajian : {}", id);
        penggajianRepository.deleteById(id);
    }
}
