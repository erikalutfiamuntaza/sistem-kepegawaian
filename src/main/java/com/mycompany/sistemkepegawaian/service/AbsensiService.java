package com.mycompany.sistemkepegawaian.service;

import com.mycompany.sistemkepegawaian.domain.Absensi;
import com.mycompany.sistemkepegawaian.repository.AbsensiRepository;
import com.mycompany.sistemkepegawaian.service.dto.AbsensiDTO;
import com.mycompany.sistemkepegawaian.service.mapper.AbsensiMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.sistemkepegawaian.domain.Absensi}.
 */
@Service
@Transactional
public class AbsensiService {

    private static final Logger LOG = LoggerFactory.getLogger(AbsensiService.class);

    private final AbsensiRepository absensiRepository;

    private final AbsensiMapper absensiMapper;

    public AbsensiService(AbsensiRepository absensiRepository, AbsensiMapper absensiMapper) {
        this.absensiRepository = absensiRepository;
        this.absensiMapper = absensiMapper;
    }

    /**
     * Save a absensi.
     *
     * @param absensiDTO the entity to save.
     * @return the persisted entity.
     */
    public AbsensiDTO save(AbsensiDTO absensiDTO) {
        LOG.debug("Request to save Absensi : {}", absensiDTO);
        Absensi absensi = absensiMapper.toEntity(absensiDTO);
        absensi = absensiRepository.save(absensi);
        return absensiMapper.toDto(absensi);
    }

    /**
     * Update a absensi.
     *
     * @param absensiDTO the entity to save.
     * @return the persisted entity.
     */
    public AbsensiDTO update(AbsensiDTO absensiDTO) {
        LOG.debug("Request to update Absensi : {}", absensiDTO);
        Absensi absensi = absensiMapper.toEntity(absensiDTO);
        absensi = absensiRepository.save(absensi);
        return absensiMapper.toDto(absensi);
    }

    /**
     * Partially update a absensi.
     *
     * @param absensiDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AbsensiDTO> partialUpdate(AbsensiDTO absensiDTO) {
        LOG.debug("Request to partially update Absensi : {}", absensiDTO);

        return absensiRepository
            .findById(absensiDTO.getId())
            .map(existingAbsensi -> {
                absensiMapper.partialUpdate(existingAbsensi, absensiDTO);

                return existingAbsensi;
            })
            .map(absensiRepository::save)
            .map(absensiMapper::toDto);
    }

    /**
     * Get all the absensis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<AbsensiDTO> findAllWithEagerRelationships(Pageable pageable) {
        return absensiRepository.findAllWithEagerRelationships(pageable).map(absensiMapper::toDto);
    }

    /**
     * Get one absensi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AbsensiDTO> findOne(Long id) {
        LOG.debug("Request to get Absensi : {}", id);
        return absensiRepository.findOneWithEagerRelationships(id).map(absensiMapper::toDto);
    }

    /**
     * Delete the absensi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Absensi : {}", id);
        absensiRepository.deleteById(id);
    }
}
