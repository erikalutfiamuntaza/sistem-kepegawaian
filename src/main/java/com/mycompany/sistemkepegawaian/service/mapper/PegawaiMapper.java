package com.mycompany.sistemkepegawaian.service.mapper;

import com.mycompany.sistemkepegawaian.domain.Pegawai;
import com.mycompany.sistemkepegawaian.service.dto.PegawaiDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pegawai} and its DTO {@link PegawaiDTO}.
 */
@Mapper(componentModel = "spring")
public interface PegawaiMapper extends EntityMapper<PegawaiDTO, Pegawai> {}
