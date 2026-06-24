package com.mycompany.sistemkepegawaian.service.mapper;

import static com.mycompany.sistemkepegawaian.domain.AbsensiAsserts.*;
import static com.mycompany.sistemkepegawaian.domain.AbsensiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AbsensiMapperTest {

    private AbsensiMapper absensiMapper;

    @BeforeEach
    void setUp() {
        absensiMapper = new AbsensiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAbsensiSample1();
        var actual = absensiMapper.toEntity(absensiMapper.toDto(expected));
        assertAbsensiAllPropertiesEquals(expected, actual);
    }
}
