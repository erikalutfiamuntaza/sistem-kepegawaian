package com.mycompany.sistemkepegawaian.service;

import com.mycompany.sistemkepegawaian.domain.Pegawai;
import com.mycompany.sistemkepegawaian.repository.PegawaiRepository;
import com.mycompany.sistemkepegawaian.service.dto.PegawaiDTO;
import com.mycompany.sistemkepegawaian.service.mapper.PegawaiMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.sistemkepegawaian.domain.Pegawai}.
 */
@Service
@Transactional
public class PegawaiService {

    private static final Logger LOG = LoggerFactory.getLogger(PegawaiService.class);

    private final PegawaiRepository pegawaiRepository;

    private final PegawaiMapper pegawaiMapper;

    public PegawaiService(PegawaiRepository pegawaiRepository, PegawaiMapper pegawaiMapper) {
        this.pegawaiRepository = pegawaiRepository;
        this.pegawaiMapper = pegawaiMapper;
    }

    /**
     * Save a pegawai.
     *
     * @param pegawaiDTO the entity to save.
     * @return the persisted entity.
     */
    public PegawaiDTO save(PegawaiDTO pegawaiDTO) {
        LOG.debug("Request to save Pegawai : {}", pegawaiDTO);
        Pegawai pegawai = pegawaiMapper.toEntity(pegawaiDTO);
        pegawai = pegawaiRepository.save(pegawai);
        return pegawaiMapper.toDto(pegawai);
    }

    /**
     * Update a pegawai.
     *
     * @param pegawaiDTO the entity to save.
     * @return the persisted entity.
     */
    public PegawaiDTO update(PegawaiDTO pegawaiDTO) {
        LOG.debug("Request to update Pegawai : {}", pegawaiDTO);
        Pegawai pegawai = pegawaiMapper.toEntity(pegawaiDTO);
        pegawai = pegawaiRepository.save(pegawai);
        return pegawaiMapper.toDto(pegawai);
    }

    /**
     * Partially update a pegawai.
     *
     * @param pegawaiDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PegawaiDTO> partialUpdate(PegawaiDTO pegawaiDTO) {
        LOG.debug("Request to partially update Pegawai : {}", pegawaiDTO);

        return pegawaiRepository
            .findById(pegawaiDTO.getId())
            .map(existingPegawai -> {
                pegawaiMapper.partialUpdate(existingPegawai, pegawaiDTO);

                return existingPegawai;
            })
            .map(pegawaiRepository::save)
            .map(pegawaiMapper::toDto);
    }

    /**
     * Get one pegawai by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PegawaiDTO> findOne(Long id) {
        LOG.debug("Request to get Pegawai : {}", id);
        return pegawaiRepository.findById(id).map(pegawaiMapper::toDto);
    }

    /**
     * Delete the pegawai by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Pegawai : {}", id);
        pegawaiRepository.deleteById(id);
    }
}
