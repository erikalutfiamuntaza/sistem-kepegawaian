package com.mycompany.sistemkepegawaian.service.mapper;

import com.mycompany.sistemkepegawaian.domain.Pegawai;
import com.mycompany.sistemkepegawaian.domain.Penggajian;
import com.mycompany.sistemkepegawaian.service.dto.PegawaiDTO;
import com.mycompany.sistemkepegawaian.service.dto.PenggajianDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Penggajian} and its DTO {@link PenggajianDTO}.
 */
@Mapper(componentModel = "spring")
public interface PenggajianMapper extends EntityMapper<PenggajianDTO, Penggajian> {
    @Mapping(target = "pegawai", source = "pegawai", qualifiedByName = "pegawaiNama")
    PenggajianDTO toDto(Penggajian s);

    @Named("pegawaiNama")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nama", source = "nama")
    PegawaiDTO toDtoPegawaiNama(Pegawai pegawai);
}
