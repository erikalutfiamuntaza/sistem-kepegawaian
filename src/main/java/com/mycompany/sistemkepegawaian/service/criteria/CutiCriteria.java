package com.mycompany.sistemkepegawaian.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.sistemkepegawaian.domain.Cuti} entity. This class is used
 * in {@link com.mycompany.sistemkepegawaian.web.rest.CutiResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cutis?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CutiCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter tanggalMulai;

    private LocalDateFilter tanggalSelesai;

    private StringFilter alasan;

    private StringFilter status;

    private LongFilter pegawaiId;

    private Boolean distinct;

    public CutiCriteria() {}

    public CutiCriteria(CutiCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.tanggalMulai = other.optionalTanggalMulai().map(LocalDateFilter::copy).orElse(null);
        this.tanggalSelesai = other.optionalTanggalSelesai().map(LocalDateFilter::copy).orElse(null);
        this.alasan = other.optionalAlasan().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(StringFilter::copy).orElse(null);
        this.pegawaiId = other.optionalPegawaiId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CutiCriteria copy() {
        return new CutiCriteria(this);
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

    public LocalDateFilter getTanggalMulai() {
        return tanggalMulai;
    }

    public Optional<LocalDateFilter> optionalTanggalMulai() {
        return Optional.ofNullable(tanggalMulai);
    }

    public LocalDateFilter tanggalMulai() {
        if (tanggalMulai == null) {
            setTanggalMulai(new LocalDateFilter());
        }
        return tanggalMulai;
    }

    public void setTanggalMulai(LocalDateFilter tanggalMulai) {
        this.tanggalMulai = tanggalMulai;
    }

    public LocalDateFilter getTanggalSelesai() {
        return tanggalSelesai;
    }

    public Optional<LocalDateFilter> optionalTanggalSelesai() {
        return Optional.ofNullable(tanggalSelesai);
    }

    public LocalDateFilter tanggalSelesai() {
        if (tanggalSelesai == null) {
            setTanggalSelesai(new LocalDateFilter());
        }
        return tanggalSelesai;
    }

    public void setTanggalSelesai(LocalDateFilter tanggalSelesai) {
        this.tanggalSelesai = tanggalSelesai;
    }

    public StringFilter getAlasan() {
        return alasan;
    }

    public Optional<StringFilter> optionalAlasan() {
        return Optional.ofNullable(alasan);
    }

    public StringFilter alasan() {
        if (alasan == null) {
            setAlasan(new StringFilter());
        }
        return alasan;
    }

    public void setAlasan(StringFilter alasan) {
        this.alasan = alasan;
    }

    public StringFilter getStatus() {
        return status;
    }

    public Optional<StringFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public StringFilter status() {
        if (status == null) {
            setStatus(new StringFilter());
        }
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }

    public LongFilter getPegawaiId() {
        return pegawaiId;
    }

    public Optional<LongFilter> optionalPegawaiId() {
        return Optional.ofNullable(pegawaiId);
    }

    public LongFilter pegawaiId() {
        if (pegawaiId == null) {
            setPegawaiId(new LongFilter());
        }
        return pegawaiId;
    }

    public void setPegawaiId(LongFilter pegawaiId) {
        this.pegawaiId = pegawaiId;
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
        final CutiCriteria that = (CutiCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tanggalMulai, that.tanggalMulai) &&
            Objects.equals(tanggalSelesai, that.tanggalSelesai) &&
            Objects.equals(alasan, that.alasan) &&
            Objects.equals(status, that.status) &&
            Objects.equals(pegawaiId, that.pegawaiId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tanggalMulai, tanggalSelesai, alasan, status, pegawaiId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CutiCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTanggalMulai().map(f -> "tanggalMulai=" + f + ", ").orElse("") +
            optionalTanggalSelesai().map(f -> "tanggalSelesai=" + f + ", ").orElse("") +
            optionalAlasan().map(f -> "alasan=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalPegawaiId().map(f -> "pegawaiId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
