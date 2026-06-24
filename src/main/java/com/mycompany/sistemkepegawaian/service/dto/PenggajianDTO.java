package com.mycompany.sistemkepegawaian.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.sistemkepegawaian.domain.Penggajian} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PenggajianDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate bulan;

    @NotNull
    private BigDecimal gajiPokok;

    @NotNull
    private BigDecimal bonus;

    @NotNull
    private BigDecimal potongan;

    @NotNull
    private BigDecimal totalGaji;

    @NotNull
    private PegawaiDTO pegawai;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getBulan() {
        return bulan;
    }

    public void setBulan(LocalDate bulan) {
        this.bulan = bulan;
    }

    public BigDecimal getGajiPokok() {
        return gajiPokok;
    }

    public void setGajiPokok(BigDecimal gajiPokok) {
        this.gajiPokok = gajiPokok;
    }

    public BigDecimal getBonus() {
        return bonus;
    }

    public void setBonus(BigDecimal bonus) {
        this.bonus = bonus;
    }

    public BigDecimal getPotongan() {
        return potongan;
    }

    public void setPotongan(BigDecimal potongan) {
        this.potongan = potongan;
    }

    public BigDecimal getTotalGaji() {
        return totalGaji;
    }

    public void setTotalGaji(BigDecimal totalGaji) {
        this.totalGaji = totalGaji;
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
        if (!(o instanceof PenggajianDTO)) {
            return false;
        }

        PenggajianDTO penggajianDTO = (PenggajianDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, penggajianDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PenggajianDTO{" +
            "id=" + getId() +
            ", bulan='" + getBulan() + "'" +
            ", gajiPokok=" + getGajiPokok() +
            ", bonus=" + getBonus() +
            ", potongan=" + getPotongan() +
            ", totalGaji=" + getTotalGaji() +
            ", pegawai=" + getPegawai() +
            "}";
    }
}
