package com.mycompany.sistemkepegawaian.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Pegawai.
 */
@Entity
@Table(name = "pegawai")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Pegawai implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nama", nullable = false)
    private String nama;

    @NotNull
    @Column(name = "jabatan", nullable = false)
    private String jabatan;

    @NotNull
    @Column(name = "departemen", nullable = false)
    private String departemen;

    @NotNull
    @Column(name = "gaji", precision = 21, scale = 2, nullable = false)
    private BigDecimal gaji;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pegawai")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pegawai" }, allowSetters = true)
    private Set<Cuti> cutis = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pegawai")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pegawai" }, allowSetters = true)
    private Set<Penggajian> penggajians = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pegawai")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pegawai" }, allowSetters = true)
    private Set<Absensi> absensis = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pegawai id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNama() {
        return this.nama;
    }

    public Pegawai nama(String nama) {
        this.setNama(nama);
        return this;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJabatan() {
        return this.jabatan;
    }

    public Pegawai jabatan(String jabatan) {
        this.setJabatan(jabatan);
        return this;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public String getDepartemen() {
        return this.departemen;
    }

    public Pegawai departemen(String departemen) {
        this.setDepartemen(departemen);
        return this;
    }

    public void setDepartemen(String departemen) {
        this.departemen = departemen;
    }

    public BigDecimal getGaji() {
        return this.gaji;
    }

    public Pegawai gaji(BigDecimal gaji) {
        this.setGaji(gaji);
        return this;
    }

    public void setGaji(BigDecimal gaji) {
        this.gaji = gaji;
    }

    public Set<Cuti> getCutis() {
        return this.cutis;
    }

    public void setCutis(Set<Cuti> cutis) {
        if (this.cutis != null) {
            this.cutis.forEach(i -> i.setPegawai(null));
        }
        if (cutis != null) {
            cutis.forEach(i -> i.setPegawai(this));
        }
        this.cutis = cutis;
    }

    public Pegawai cutis(Set<Cuti> cutis) {
        this.setCutis(cutis);
        return this;
    }

    public Pegawai addCuti(Cuti cuti) {
        this.cutis.add(cuti);
        cuti.setPegawai(this);
        return this;
    }

    public Pegawai removeCuti(Cuti cuti) {
        this.cutis.remove(cuti);
        cuti.setPegawai(null);
        return this;
    }

    public Set<Penggajian> getPenggajians() {
        return this.penggajians;
    }

    public void setPenggajians(Set<Penggajian> penggajians) {
        if (this.penggajians != null) {
            this.penggajians.forEach(i -> i.setPegawai(null));
        }
        if (penggajians != null) {
            penggajians.forEach(i -> i.setPegawai(this));
        }
        this.penggajians = penggajians;
    }

    public Pegawai penggajians(Set<Penggajian> penggajians) {
        this.setPenggajians(penggajians);
        return this;
    }

    public Pegawai addPenggajian(Penggajian penggajian) {
        this.penggajians.add(penggajian);
        penggajian.setPegawai(this);
        return this;
    }

    public Pegawai removePenggajian(Penggajian penggajian) {
        this.penggajians.remove(penggajian);
        penggajian.setPegawai(null);
        return this;
    }

    public Set<Absensi> getAbsensis() {
        return this.absensis;
    }

    public void setAbsensis(Set<Absensi> absensis) {
        if (this.absensis != null) {
            this.absensis.forEach(i -> i.setPegawai(null));
        }
        if (absensis != null) {
            absensis.forEach(i -> i.setPegawai(this));
        }
        this.absensis = absensis;
    }

    public Pegawai absensis(Set<Absensi> absensis) {
        this.setAbsensis(absensis);
        return this;
    }

    public Pegawai addAbsensi(Absensi absensi) {
        this.absensis.add(absensi);
        absensi.setPegawai(this);
        return this;
    }

    public Pegawai removeAbsensi(Absensi absensi) {
        this.absensis.remove(absensi);
        absensi.setPegawai(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pegawai)) {
            return false;
        }
        return getId() != null && getId().equals(((Pegawai) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pegawai{" +
            "id=" + getId() +
            ", nama='" + getNama() + "'" +
            ", jabatan='" + getJabatan() + "'" +
            ", departemen='" + getDepartemen() + "'" +
            ", gaji=" + getGaji() +
            "}";
    }
}
