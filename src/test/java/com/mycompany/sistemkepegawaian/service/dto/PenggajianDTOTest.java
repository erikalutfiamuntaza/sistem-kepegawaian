package com.mycompany.sistemkepegawaian.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.sistemkepegawaian.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PenggajianDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PenggajianDTO.class);
        PenggajianDTO penggajianDTO1 = new PenggajianDTO();
        penggajianDTO1.setId(1L);
        PenggajianDTO penggajianDTO2 = new PenggajianDTO();
        assertThat(penggajianDTO1).isNotEqualTo(penggajianDTO2);
        penggajianDTO2.setId(penggajianDTO1.getId());
        assertThat(penggajianDTO1).isEqualTo(penggajianDTO2);
        penggajianDTO2.setId(2L);
        assertThat(penggajianDTO1).isNotEqualTo(penggajianDTO2);
        penggajianDTO1.setId(null);
        assertThat(penggajianDTO1).isNotEqualTo(penggajianDTO2);
    }
}
