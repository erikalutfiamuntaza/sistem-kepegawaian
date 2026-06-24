package com.mycompany.sistemkepegawaian.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.sistemkepegawaian.domain.Pegawai} entity. This class is used
 * in {@link com.mycompany.sistemkepegawaian.web.rest.PegawaiResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /pegawais?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PegawaiCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nama;

    private StringFilter jabatan;

    private StringFilter departemen;

    private BigDecimalFilter gaji;

    private LongFilter cutiId;

    private LongFilter penggajianId;

    private LongFilter absensiId;

    private Boolean distinct;

    public PegawaiCriteria() {}

    public PegawaiCriteria(PegawaiCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nama = other.optionalNama().map(StringFilter::copy).orElse(null);
        this.jabatan = other.optionalJabatan().map(StringFilter::copy).orElse(null);
        this.departemen = other.optionalDepartemen().map(StringFilter::copy).orElse(null);
        this.gaji = other.optionalGaji().map(BigDecimalFilter::copy).orElse(null);
        this.cutiId = other.optionalCutiId().map(LongFilter::copy).orElse(null);
        this.penggajianId = other.optionalPenggajianId().map(LongFilter::copy).orElse(null);
        this.absensiId = other.optionalAbsensiId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PegawaiCriteria copy() {
        return new PegawaiCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNama() {
        return nama;
    }

    public Optional<StringFilter> optionalNama() {
        return Optional.ofNullable(nama);
    }

    public StringFilter nama() {
        if (nama == null) {
            setNama(new StringFilter());
        }
        return nama;
    }

    public void setNama(StringFilter nama) {
        this.nama = nama;
    }

    public StringFilter getJabatan() {
        return jabatan;
    }

    public Optional<StringFilter> optionalJabatan() {
        return Optional.ofNullable(jabatan);
    }

    public StringFilter jabatan() {
        if (jabatan == null) {
            setJabatan(new StringFilter());
        }
        return jabatan;
    }

    public void setJabatan(StringFilter jabatan) {
        this.jabatan = jabatan;
    }

    public StringFilter getDepartemen() {
        return departemen;
    }

    public Optional<StringFilter> optionalDepartemen() {
        return Optional.ofNullable(departemen);
    }

    public StringFilter departemen() {
        if (departemen == null) {
            setDepartemen(new StringFilter());
        }
        return departemen;
    }

    public void setDepartemen(StringFilter departemen) {
        this.departemen = departemen;
    }

    public BigDecimalFilter getGaji() {
        return gaji;
    }

    public Optional<BigDecimalFilter> optionalGaji() {
        return Optional.ofNullable(gaji);
    }

    public BigDecimalFilter gaji() {
        if (gaji == null) {
            setGaji(new BigDecimalFilter());
        }
        return gaji;
    }

    public void setGaji(BigDecimalFilter gaji) {
        this.gaji = gaji;
    }

    public LongFilter getCutiId() {
        return cutiId;
    }

    public Optional<LongFilter> optionalCutiId() {
        return Optional.ofNullable(cutiId);
    }

    public LongFilter cutiId() {
        if (cutiId == null) {
            setCutiId(new LongFilter());
        }
        return cutiId;
    }

    public void setCutiId(LongFilter cutiId) {
        this.cutiId = cutiId;
    }

    public LongFilter getPenggajianId() {
        return penggajianId;
    }

    public Optional<LongFilter> optionalPenggajianId() {
        return Optional.ofNullable(penggajianId);
    }

    public LongFilter penggajianId() {
        if (penggajianId == null) {
            setPenggajianId(new LongFilter());
        }
        return penggajianId;
    }

    public void setPenggajianId(LongFilter penggajianId) {
        this.penggajianId = penggajianId;
    }

    public LongFilter getAbsensiId() {
        return absensiId;
    }

    public Optional<LongFilter> optionalAbsensiId() {
        return Optional.ofNullable(absensiId);
    }

    public LongFilter absensiId() {
        if (absensiId == null) {
            setAbsensiId(new LongFilter());
        }
        return absensiId;
    }

    public void setAbsensiId(LongFilter absensiId) {
        this.absensiId = absensiId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PegawaiCriteria that = (PegawaiCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nama, that.nama) &&
            Objects.equals(jabatan, that.jabatan) &&
            Objects.equals(departemen, that.departemen) &&
            Objects.equals(gaji, that.gaji) &&
            Objects.equals(cutiId, that.cutiId) &&
            Objects.equals(penggajianId, that.penggajianId) &&
            Objects.equals(absensiId, that.absensiId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nama, jabatan, departemen, gaji, cutiId, penggajianId, absensiId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PegawaiCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNama().map(f -> "nama=" + f + ", ").orElse("") +
            optionalJabatan().map(f -> "jabatan=" + f + ", ").orElse("") +
            optionalDepartemen().map(f -> "departemen=" + f + ", ").orElse("") +
            optionalGaji().map(f -> "gaji=" + f + ", ").orElse("") +
            optionalCutiId().map(f -> "cutiId=" + f + ", ").orElse("") +
            optionalPenggajianId().map(f -> "penggajianId=" + f + ", ").orElse("") +
            optionalAbsensiId().map(f -> "absensiId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
