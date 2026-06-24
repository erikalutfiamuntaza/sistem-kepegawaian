package com.mycompany.sistemkepegawaian.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.sistemkepegawaian.domain.Absensi} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AbsensiDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate tanggal;

    @NotNull
    private Instant jamMasuk;

    @NotNull
    private Instant jamKeluar;

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

    public LocalDate getTanggal() {
        return tanggal;
    }

    public void setTanggal(LocalDate tanggal) {
        this.tanggal = tanggal;
    }

    public Instant getJamMasuk() {
        return jamMasuk;
    }

    public void setJamMasuk(Instant jamMasuk) {
        this.jamMasuk = jamMasuk;
    }

    public Instant getJamKeluar() {
        return jamKeluar;
    }

    public void setJamKeluar(Instant jamKeluar) {
        this.jamKeluar = jamKeluar;
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
        if (!(o instanceof AbsensiDTO)) {
            return false;
        }

        AbsensiDTO absensiDTO = (AbsensiDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, absensiDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AbsensiDTO{" +
            "id=" + getId() +
            ", tanggal='" + getTanggal() + "'" +
            ", jamMasuk='" + getJamMasuk() + "'" +
            ", jamKeluar='" + getJamKeluar() + "'" +
            ", status='" + getStatus() + "'" +
            ", pegawai=" + getPegawai() +
            "}";
    }
}
