package com.mycompany.sistemkepegawaian.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.sistemkepegawaian.domain.Cuti} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CutiDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate tanggalMulai;

    @NotNull
    private LocalDate tanggalSelesai;

    @NotNull
    private String alasan;

    @NotNull
    private String status;

    @NotNull
    private PegawaiDTO pegawai;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getTanggalMulai() {
        return tanggalMulai;
    }

    public void setTanggalMulai(LocalDate tanggalMulai) {
        this.tanggalMulai = tanggalMulai;
    }

    public LocalDate getTanggalSelesai() {
        return tanggalSelesai;
    }

    public void setTanggalSelesai(LocalDate tanggalSelesai) {
        this.tanggalSelesai = tanggalSelesai;
    }

    public String getAlasan() {
        return alasan;
    }

    public void setAlasan(String alasan) {
        this.alasan = alasan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PegawaiDTO getPegawai() {
        return pegawai;
    }

    public void setPegawai(PegawaiDTO pegawai) {
        this.pegawai = pegawai;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CutiDTO)) {
            return false;
        }

        CutiDTO cutiDTO = (CutiDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cutiDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CutiDTO{" +
            "id=" + getId() +
            ", tanggalMulai='" + getTanggalMulai() + "'" +
            ", tanggalSelesai='" + getTanggalSelesai() + "'" +
            ", alasan='" + getAlasan() + "'" +
            ", status='" + getStatus() + "'" +
            ", pegawai=" + getPegawai() +
            "}";
    }
}
