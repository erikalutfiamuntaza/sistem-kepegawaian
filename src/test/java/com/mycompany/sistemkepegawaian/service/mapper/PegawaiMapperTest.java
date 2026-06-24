package com.mycompany.sistemkepegawaian.service.mapper;

import static com.mycompany.sistemkepegawaian.domain.PegawaiAsserts.*;
import static com.mycompany.sistemkepegawaian.domain.PegawaiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PegawaiMapperTest {

    private PegawaiMapper pegawaiMapper;

    @BeforeEach
    void setUp() {
        pegawaiMapper = new PegawaiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPegawaiSample1();
        var actual = pegawaiMapper.toEntity(pegawaiMapper.toDto(expected));
        assertPegawaiAllPropertiesEquals(expected, actual);
    }
}
