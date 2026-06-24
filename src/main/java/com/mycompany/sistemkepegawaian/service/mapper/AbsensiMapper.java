package com.mycompany.sistemkepegawaian.service.mapper;

import com.mycompany.sistemkepegawaian.domain.Absensi;
import com.mycompany.sistemkepegawaian.domain.Pegawai;
import com.mycompany.sistemkepegawaian.service.dto.AbsensiDTO;
import com.mycompany.sistemkepegawaian.service.dto.PegawaiDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Absensi} and its DTO {@link AbsensiDTO}.
 */
@Mapper(componentModel = "spring")
public interface AbsensiMapper extends EntityMapper<AbsensiDTO, Absensi> {
    @Mapping(target = "pegawai", source = "pegawai", qualifiedByName = "pegawaiNama")
    AbsensiDTO toDto(Absensi s);

    @Named("pegawaiNama")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nama", source = "nama")
    PegawaiDTO toDtoPegawaiNama(Pegawai pegawai);
}
