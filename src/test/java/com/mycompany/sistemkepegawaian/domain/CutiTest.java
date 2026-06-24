package com.mycompany.sistemkepegawaian.domain;

import static com.mycompany.sistemkepegawaian.domain.CutiTestSamples.*;
import static com.mycompany.sistemkepegawaian.domain.PegawaiTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.sistemkepegawaian.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CutiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cuti.class);
        Cuti cuti1 = getCutiSample1();
        Cuti cuti2 = new Cuti();
        assertThat(cuti1).isNotEqualTo(cuti2);

        cuti2.setId(cuti1.getId());
        assertThat(cuti1).isEqualTo(cuti2);

        cuti2 = getCutiSample2();
        assertThat(cuti1).isNotEqualTo(cuti2);
    }

    @Test
    void pegawaiTest() {
        Cuti cuti = getCutiRandomSampleGenerator();
        Pegawai pegawaiBack = getPegawaiRandomSampleGenerator();

        cuti.setPegawai(pegawaiBack);
        assertThat(cuti.getPegawai()).isEqualTo(pegawaiBack);

        cuti.pegawai(null);
        assertThat(cuti.getPegawai()).isNull();
    }
}
