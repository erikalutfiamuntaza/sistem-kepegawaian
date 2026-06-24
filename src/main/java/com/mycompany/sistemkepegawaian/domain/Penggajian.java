package com.mycompany.sistemkepegawaian.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Penggajian.
 */
@Entity
@Table(name = "penggajian")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Penggajian implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "bulan", nullable = false)
    private LocalDate bulan;

    @NotNull
    @Column(name = "gaji_pokok", precision = 21, scale = 2, nullable = false)
    private BigDecimal gajiPokok;

    @NotNull
    @Column(name = "bonus", precision = 21, scale = 2, nullable = false)
    private BigDecimal bonus;

    @NotNull
    @Column(name = "potongan", precision = 21, scale = 2, nullable = false)
    private BigDecimal potongan;

    @NotNull
    @Column(name = "total_gaji", precision = 21, scale = 2, nullable = false)
    private BigDecimal totalGaji;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "cutis", "penggajians", "absensis" }, allowSetters = true)
    private Pegawai pegawai;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Penggajian id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getBulan() {
        return this.bulan;
    }

    public Penggajian bulan(LocalDate bulan) {
        this.setBulan(bulan);
        return this;
    }

    public void setBulan(LocalDate bulan) {
        this.bulan = bulan;
    }

    public BigDecimal getGajiPokok() {
        return this.gajiPokok;
    }

    public Penggajian gajiPokok(BigDecimal gajiPokok) {
        this.setGajiPokok(gajiPokok);
        return this;
    }

    public void setGajiPokok(BigDecimal gajiPokok) {
        this.gajiPokok = gajiPokok;
    }

    public BigDecimal getBonus() {
        return this.bonus;
    }

    public Penggajian bonus(BigDecimal bonus) {
        this.setBonus(bonus);
        return this;
    }

    public void setBonus(BigDecimal bonus) {
        this.bonus = bonus;
    }

    public BigDecimal getPotongan() {
        return this.potongan;
    }

    public Penggajian potongan(BigDecimal potongan) {
        this.setPotongan(potongan);
        return this;
    }

    public void setPotongan(BigDecimal potongan) {
        this.potongan = potongan;
    }

    public BigDecimal getTotalGaji() {
        return this.totalGaji;
    }

    public Penggajian totalGaji(BigDecimal totalGaji) {
        this.setTotalGaji(totalGaji);
        return this;
    }

    public void setTotalGaji(BigDecimal totalGaji) {
        this.totalGaji = totalGaji;
    }

    public Pegawai getPegawai() {
        return this.pegawai;
    }

    public void setPegawai(Pegawai pegawai) {
        this.pegawai = pegawai;
    }

    public Penggajian pegawai(Pegawai pegawai) {
        this.setPegawai(pegawai);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Penggajian)) {
            return false;
        }
        return getId() != null && getId().equals(((Penggajian) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Penggajian{" +
            "id=" + getId() +
            ", bulan='" + getBulan() + "'" +
            ", gajiPokok=" + getGajiPokok() +
            ", bonus=" + getBonus() +
            ", potongan=" + getPotongan() +
            ", totalGaji=" + getTotalGaji() +
            "}";
    }
}
