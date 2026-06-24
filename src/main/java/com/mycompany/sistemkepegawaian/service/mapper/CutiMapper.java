package com.mycompany.sistemkepegawaian.service.mapper;

import com.mycompany.sistemkepegawaian.domain.Cuti;
import com.mycompany.sistemkepegawaian.domain.Pegawai;
import com.mycompany.sistemkepegawaian.service.dto.CutiDTO;
import com.mycompany.sistemkepegawaian.service.dto.PegawaiDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cuti} and its DTO {@link CutiDTO}.
 */
@Mapper(componentModel = "spring")
public interface CutiMapper extends EntityMapper<CutiDTO, Cuti> {
    @Mapping(target = "pegawai", source = "pegawai", qualifiedByName = "pegawaiNama")
    CutiDTO toDto(Cuti s);

    @Named("pegawaiNama")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nama", source = "nama")
    PegawaiDTO toDtoPegawaiNama(Pegawai pegawai);
}
