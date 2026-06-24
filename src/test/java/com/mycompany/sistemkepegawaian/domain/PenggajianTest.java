package com.mycompany.sistemkepegawaian.domain;

import static com.mycompany.sistemkepegawaian.domain.PegawaiTestSamples.*;
import static com.mycompany.sistemkepegawaian.domain.PenggajianTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.sistemkepegawaian.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PenggajianTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Penggajian.class);
        Penggajian penggajian1 = getPenggajianSample1();
        Penggajian penggajian2 = new Penggajian();
        assertThat(penggajian1).isNotEqualTo(penggajian2);

        penggajian2.setId(penggajian1.getId());
        assertThat(penggajian1).isEqualTo(penggajian2);

        penggajian2 = getPenggajianSample2();
        assertThat(penggajian1).isNotEqualTo(penggajian2);
    }

    @Test
    void pegawaiTest() {
        Penggajian penggajian = getPenggajianRandomSampleGenerator();
        Pegawai pegawaiBack = getPegawaiRandomSampleGenerator();

        penggajian.setPegawai(pegawaiBack);
        assertThat(penggajian.getPegawai()).isEqualTo(pegawaiBack);

        penggajian.pegawai(null);
        assertThat(penggajian.getPegawai()).isNull();
    }
}
