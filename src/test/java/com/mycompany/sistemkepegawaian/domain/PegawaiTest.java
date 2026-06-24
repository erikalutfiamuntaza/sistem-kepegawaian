package com.mycompany.sistemkepegawaian.domain;

import static com.mycompany.sistemkepegawaian.domain.AbsensiTestSamples.*;
import static com.mycompany.sistemkepegawaian.domain.CutiTestSamples.*;
import static com.mycompany.sistemkepegawaian.domain.PegawaiTestSamples.*;
import static com.mycompany.sistemkepegawaian.domain.PenggajianTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.sistemkepegawaian.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PegawaiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pegawai.class);
        Pegawai pegawai1 = getPegawaiSample1();
        Pegawai pegawai2 = new Pegawai();
        assertThat(pegawai1).isNotEqualTo(pegawai2);

        pegawai2.setId(pegawai1.getId());
        assertThat(pegawai1).isEqualTo(pegawai2);

        pegawai2 = getPegawaiSample2();
        assertThat(pegawai1).isNotEqualTo(pegawai2);
    }

    @Test
    void cutiTest() {
        Pegawai pegawai = getPegawaiRandomSampleGenerator();
        Cuti cutiBack = getCutiRandomSampleGenerator();

        pegawai.addCuti(cutiBack);
        assertThat(pegawai.getCutis()).containsOnly(cutiBack);
        assertThat(cutiBack.getPegawai()).isEqualTo(pegawai);

        pegawai.removeCuti(cutiBack);
        assertThat(pegawai.getCutis()).doesNotContain(cutiBack);
        assertThat(cutiBack.getPegawai()).isNull();

        pegawai.cutis(new HashSet<>(Set.of(cutiBack)));
        assertThat(pegawai.getCutis()).containsOnly(cutiBack);
        assertThat(cutiBack.getPegawai()).isEqualTo(pegawai);

        pegawai.setCutis(new HashSet<>());
        assertThat(pegawai.getCutis()).doesNotContain(cutiBack);
        assertThat(cutiBack.getPegawai()).isNull();
    }

    @Test
    void penggajianTest() {
        Pegawai pegawai = getPegawaiRandomSampleGenerator();
        Penggajian penggajianBack = getPenggajianRandomSampleGenerator();

        pegawai.addPenggajian(penggajianBack);
        assertThat(pegawai.getPenggajians()).containsOnly(penggajianBack);
        assertThat(penggajianBack.getPegawai()).isEqualTo(pegawai);

        pegawai.removePenggajian(penggajianBack);
        assertThat(pegawai.getPenggajians()).doesNotContain(penggajianBack);
        assertThat(penggajianBack.getPegawai()).isNull();

        pegawai.penggajians(new HashSet<>(Set.of(penggajianBack)));
        assertThat(pegawai.getPenggajians()).containsOnly(penggajianBack);
        assertThat(penggajianBack.getPegawai()).isEqualTo(pegawai);

        pegawai.setPenggajians(new HashSet<>());
        assertThat(pegawai.getPenggajians()).doesNotContain(penggajianBack);
        assertThat(penggajianBack.getPegawai()).isNull();
    }

    @Test
    void absensiTest() {
        Pegawai pegawai = getPegawaiRandomSampleGenerator();
        Absensi absensiBack = getAbsensiRandomSampleGenerator();

        pegawai.addAbsensi(absensiBack);
        assertThat(pegawai.getAbsensis()).containsOnly(absensiBack);
        assertThat(absensiBack.getPegawai()).isEqualTo(pegawai);

        pegawai.removeAbsensi(absensiBack);
        assertThat(pegawai.getAbsensis()).doesNotContain(absensiBack);
        assertThat(absensiBack.getPegawai()).isNull();

        pegawai.absensis(new HashSet<>(Set.of(absensiBack)));
        assertThat(pegawai.getAbsensis()).containsOnly(absensiBack);
        assertThat(absensiBack.getPegawai()).isEqualTo(pegawai);

        pegawai.setAbsensis(new HashSet<>());
        assertThat(pegawai.getAbsensis()).doesNotContain(absensiBack);
        assertThat(absensiBack.getPegawai()).isNull();
    }
}
