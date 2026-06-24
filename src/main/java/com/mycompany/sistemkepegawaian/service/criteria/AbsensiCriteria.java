package com.mycompany.sistemkepegawaian.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.sistemkepegawaian.domain.Absensi} entity. This class is used
 * in {@link com.mycompany.sistemkepegawaian.web.rest.AbsensiResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /absensis?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AbsensiCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter tanggal;

    private InstantFilter jamMasuk;

    private InstantFilter jamKeluar;

    private StringFilter status;

    private LongFilter pegawaiId;

    private Boolean distinct;

    public AbsensiCriteria() {}

    public AbsensiCriteria(AbsensiCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.tanggal = other.optionalTanggal().map(LocalDateFilter::copy).orElse(null);
        this.jamMasuk = other.optionalJamMasuk().map(InstantFilter::copy).orElse(null);
        this.jamKeluar = other.optionalJamKeluar().map(InstantFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(StringFilter::copy).orElse(null);
        this.pegawaiId = other.optionalPegawaiId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AbsensiCriteria copy() {
        return new AbsensiCriteria(this);
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

    public LocalDateFilter getTanggal() {
        return tanggal;
    }

    public Optional<LocalDateFilter> optionalTanggal() {
        return Optional.ofNullable(tanggal);
    }

    public LocalDateFilter tanggal() {
        if (tanggal == null) {
            setTanggal(new LocalDateFilter());
        }
        return tanggal;
    }

    public void setTanggal(LocalDateFilter tanggal) {
        this.tanggal = tanggal;
    }

    public InstantFilter getJamMasuk() {
        return jamMasuk;
    }

    public Optional<InstantFilter> optionalJamMasuk() {
        return Optional.ofNullable(jamMasuk);
    }

    public InstantFilter jamMasuk() {
        if (jamMasuk == null) {
            setJamMasuk(new InstantFilter());
        }
        return jamMasuk;
    }

    public void setJamMasuk(InstantFilter jamMasuk) {
        this.jamMasuk = jamMasuk;
    }

    public InstantFilter getJamKeluar() {
        return jamKeluar;
    }

    public Optional<InstantFilter> optionalJamKeluar() {
        return Optional.ofNullable(jamKeluar);
    }

    public InstantFilter jamKeluar() {
        if (jamKeluar == null) {
            setJamKeluar(new InstantFilter());
        }
        return jamKeluar;
    }

    public void setJamKeluar(InstantFilter jamKeluar) {
        this.jamKeluar = jamKeluar;
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
        final AbsensiCriteria that = (AbsensiCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tanggal, that.tanggal) &&
            Objects.equals(jamMasuk, that.jamMasuk) &&
            Objects.equals(jamKeluar, that.jamKeluar) &&
            Objects.equals(status, that.status) &&
            Objects.equals(pegawaiId, that.pegawaiId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tanggal, jamMasuk, jamKeluar, status, pegawaiId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AbsensiCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTanggal().map(f -> "tanggal=" + f + ", ").orElse("") +
            optionalJamMasuk().map(f -> "jamMasuk=" + f + ", ").orElse("") +
            optionalJamKeluar().map(f -> "jamKeluar=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalPegawaiId().map(f -> "pegawaiId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
