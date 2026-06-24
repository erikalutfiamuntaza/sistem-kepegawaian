package com.mycompany.sistemkepegawaian.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.sistemkepegawaian.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AbsensiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AbsensiDTO.class);
        AbsensiDTO absensiDTO1 = new AbsensiDTO();
        absensiDTO1.setId(1L);
        AbsensiDTO absensiDTO2 = new AbsensiDTO();
        assertThat(absensiDTO1).isNotEqualTo(absensiDTO2);
        absensiDTO2.setId(absensiDTO1.getId());
        assertThat(absensiDTO1).isEqualTo(absensiDTO2);
        absensiDTO2.setId(2L);
        assertThat(absensiDTO1).isNotEqualTo(absensiDTO2);
        absensiDTO1.setId(null);
        assertThat(absensiDTO1).isNotEqualTo(absensiDTO2);
    }
}
