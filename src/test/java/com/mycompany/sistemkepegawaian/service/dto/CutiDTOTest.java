package com.mycompany.sistemkepegawaian.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.sistemkepegawaian.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CutiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CutiDTO.class);
        CutiDTO cutiDTO1 = new CutiDTO();
        cutiDTO1.setId(1L);
        CutiDTO cutiDTO2 = new CutiDTO();
        assertThat(cutiDTO1).isNotEqualTo(cutiDTO2);
        cutiDTO2.setId(cutiDTO1.getId());
        assertThat(cutiDTO1).isEqualTo(cutiDTO2);
        cutiDTO2.setId(2L);
        assertThat(cutiDTO1).isNotEqualTo(cutiDTO2);
        cutiDTO1.setId(null);
        assertThat(cutiDTO1).isNotEqualTo(cutiDTO2);
    }
}
