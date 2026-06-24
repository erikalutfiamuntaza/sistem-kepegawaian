package com.mycompany.sistemkepegawaian.service.mapper;

import static com.mycompany.sistemkepegawaian.domain.CutiAsserts.*;
import static com.mycompany.sistemkepegawaian.domain.CutiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CutiMapperTest {

    private CutiMapper cutiMapper;

    @BeforeEach
    void setUp() {
        cutiMapper = new CutiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCutiSample1();
        var actual = cutiMapper.toEntity(cutiMapper.toDto(expected));
        assertCutiAllPropertiesEquals(expected, actual);
    }
}
