package com.mycompany.sistemkepegawaian.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Absensi.
 */
@Entity
@Table(name = "absensi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Absensi implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "tanggal", nullable = false)
    private LocalDate tanggal;

    @NotNull
    @Column(name = "jam_masuk", nullable = false)
    private Instant jamMasuk;

    @NotNull
    @Column(name = "jam_keluar", nullable = false)
    private Instant jamKeluar;

    @NotNull
    @Column(name = "status", nullable = false)
    private String status;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "cutis", "penggajians", "absensis" }, allowSetters = true)
    private Pegawai pegawai;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Absensi id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getTanggal() {
        return this.tanggal;
    }

    public Absensi tanggal(LocalDate tanggal) {
        this.setTanggal(tanggal);
        return this;
    }

    public void setTanggal(LocalDate tanggal) {
        this.tanggal = tanggal;
    }

    public Instant getJamMasuk() {
        return this.jamMasuk;
    }

    public Absensi jamMasuk(Instant jamMasuk) {
        this.setJamMasuk(jamMasuk);
        return this;
    }

    public void setJamMasuk(Instant jamMasuk) {
        this.jamMasuk = jamMasuk;
    }

    public Instant getJamKeluar() {
        return this.jamKeluar;
    }

    public Absensi jamKeluar(Instant jamKeluar) {
        this.setJamKeluar(jamKeluar);
        return this;
    }

    public void setJamKeluar(Instant jamKeluar) {
        this.jamKeluar = jamKeluar;
    }

    public String getStatus() {
        return this.status;
    }

    public Absensi status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Pegawai getPegawai() {
        return this.pegawai;
    }

    public void setPegawai(Pegawai pegawai) {
        this.pegawai = pegawai;
    }

    public Absensi pegawai(Pegawai pegawai) {
        this.setPegawai(pegawai);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Absensi)) {
            return false;
        }
        return getId() != null && getId().equals(((Absensi) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Absensi{" +
            "id=" + getId() +
            ", tanggal='" + getTanggal() + "'" +
            ", jamMasuk='" + getJamMasuk() + "'" +
            ", jamKeluar='" + getJamKeluar() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
