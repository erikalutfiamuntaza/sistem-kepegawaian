package com.mycompany.sistemkepegawaian.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.sistemkepegawaian.domain.Pegawai} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PegawaiDTO implements Serializable {

    private Long id;

    @NotNull
    private String nama;

    @NotNull
    private String jabatan;

    @NotNull
    private String departemen;

    @NotNull
    private BigDecimal gaji;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public String getDepartemen() {
        return departemen;
    }

    public void setDepartemen(String departemen) {
        this.departemen = departemen;
    }

    public BigDecimal getGaji() {
        return gaji;
    }

    public void setGaji(BigDecimal gaji) {
        this.gaji = gaji;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PegawaiDTO)) {
            return false;
        }

        PegawaiDTO pegawaiDTO = (PegawaiDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pegawaiDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PegawaiDTO{" +
            "id=" + getId() +
            ", nama='" + getNama() + "'" +
            ", jabatan='" + getJabatan() + "'" +
            ", departemen='" + getDepartemen() + "'" +
            ", gaji=" + getGaji() +
            "}";
    }
}
