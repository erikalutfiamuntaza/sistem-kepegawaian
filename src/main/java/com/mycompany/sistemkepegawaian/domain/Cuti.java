package com.mycompany.sistemkepegawaian.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Cuti.
 */
@Entity
@Table(name = "cuti")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cuti implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "tanggal_mulai", nullable = false)
    private LocalDate tanggalMulai;

    @NotNull
    @Column(name = "tanggal_selesai", nullable = false)
    private LocalDate tanggalSelesai;

    @NotNull
    @Column(name = "alasan", nullable = false)
    private String alasan;

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

    public Cuti id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getTanggalMulai() {
        return this.tanggalMulai;
    }

    public Cuti tanggalMulai(LocalDate tanggalMulai) {
        this.setTanggalMulai(tanggalMulai);
        return this;
    }

    public void setTanggalMulai(LocalDate tanggalMulai) {
        this.tanggalMulai = tanggalMulai;
    }

    public LocalDate getTanggalSelesai() {
        return this.tanggalSelesai;
    }

    public Cuti tanggalSelesai(LocalDate tanggalSelesai) {
        this.setTanggalSelesai(tanggalSelesai);
        return this;
    }

    public void setTanggalSelesai(LocalDate tanggalSelesai) {
        this.tanggalSelesai = tanggalSelesai;
    }

    public String getAlasan() {
        return this.alasan;
    }

    public Cuti alasan(String alasan) {
        this.setAlasan(alasan);
        return this;
    }

    public void setAlasan(String alasan) {
        this.alasan = alasan;
    }

    public String getStatus() {
        return this.status;
    }

    public Cuti status(String status) {
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

    public Cuti pegawai(Pegawai pegawai) {
        this.setPegawai(pegawai);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cuti)) {
            return false;
        }
        return getId() != null && getId().equals(((Cuti) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cuti{" +
            "id=" + getId() +
            ", tanggalMulai='" + getTanggalMulai() + "'" +
            ", tanggalSelesai='" + getTanggalSelesai() + "'" +
            ", alasan='" + getAlasan() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
