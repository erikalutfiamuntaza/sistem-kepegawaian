package com.mycompany.sistemkepegawaian.domain;

import static com.mycompany.sistemkepegawaian.domain.AbsensiTestSamples.*;
import static com.mycompany.sistemkepegawaian.domain.PegawaiTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.sistemkepegawaian.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AbsensiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Absensi.class);
        Absensi absensi1 = getAbsensiSample1();
        Absensi absensi2 = new Absensi();
        assertThat(absensi1).isNotEqualTo(absensi2);

        absensi2.setId(absensi1.getId());
        assertThat(absensi1).isEqualTo(absensi2);

        absensi2 = getAbsensiSample2();
        assertThat(absensi1).isNotEqualTo(absensi2);
    }

    @Test
    void pegawaiTest() {
        Absensi absensi = getAbsensiRandomSampleGenerator();
        Pegawai pegawaiBack = getPegawaiRandomSampleGenerator();

        absensi.setPegawai(pegawaiBack);
        assertThat(absensi.getPegawai()).isEqualTo(pegawaiBack);

        absensi.pegawai(null);
        assertThat(absensi.getPegawai()).isNull();
    }
}
