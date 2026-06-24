package com.mycompany.sistemkepegawaian.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.sistemkepegawaian.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PegawaiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PegawaiDTO.class);
        PegawaiDTO pegawaiDTO1 = new PegawaiDTO();
        pegawaiDTO1.setId(1L);
        PegawaiDTO pegawaiDTO2 = new PegawaiDTO();
        assertThat(pegawaiDTO1).isNotEqualTo(pegawaiDTO2);
        pegawaiDTO2.setId(pegawaiDTO1.getId());
        assertThat(pegawaiDTO1).isEqualTo(pegawaiDTO2);
        pegawaiDTO2.setId(2L);
        assertThat(pegawaiDTO1).isNotEqualTo(pegawaiDTO2);
        pegawaiDTO1.setId(null);
        assertThat(pegawaiDTO1).isNotEqualTo(pegawaiDTO2);
    }
}
