package com.mycompany.sistemkepegawaian.service.mapper;

import static com.mycompany.sistemkepegawaian.domain.PenggajianAsserts.*;
import static com.mycompany.sistemkepegawaian.domain.PenggajianTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PenggajianMapperTest {

    private PenggajianMapper penggajianMapper;

    @BeforeEach
    void setUp() {
        penggajianMapper = new PenggajianMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPenggajianSample1();
        var actual = penggajianMapper.toEntity(penggajianMapper.toDto(expected));
        assertPenggajianAllPropertiesEquals(expected, actual);
    }
}
