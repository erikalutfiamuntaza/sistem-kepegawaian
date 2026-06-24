package com.mycompany.sistemkepegawaian.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.sistemkepegawaian.domain.Penggajian} entity. This class is used
 * in {@link com.mycompany.sistemkepegawaian.web.rest.PenggajianResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /penggajians?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PenggajianCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter bulan;

    private BigDecimalFilter gajiPokok;

    private BigDecimalFilter bonus;

    private BigDecimalFilter potongan;

    private BigDecimalFilter totalGaji;

    private LongFilter pegawaiId;

    private Boolean distinct;

    public PenggajianCriteria() {}

    public PenggajianCriteria(PenggajianCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.bulan = other.optionalBulan().map(LocalDateFilter::copy).orElse(null);
        this.gajiPokok = other.optionalGajiPokok().map(BigDecimalFilter::copy).orElse(null);
        this.bonus = other.optionalBonus().map(BigDecimalFilter::copy).orElse(null);
        this.potongan = other.optionalPotongan().map(BigDecimalFilter::copy).orElse(null);
        this.totalGaji = other.optionalTotalGaji().map(BigDecimalFilter::copy).orElse(null);
        this.pegawaiId = other.optionalPegawaiId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PenggajianCriteria copy() {
        return new PenggajianCriteria(this);
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

    public LocalDateFilter getBulan() {
        return bulan;
    }

    public Optional<LocalDateFilter> optionalBulan() {
        return Optional.ofNullable(bulan);
    }

    public LocalDateFilter bulan() {
        if (bulan == null) {
            setBulan(new LocalDateFilter());
        }
        return bulan;
    }

    public void setBulan(LocalDateFilter bulan) {
        this.bulan = bulan;
    }

    public BigDecimalFilter getGajiPokok() {
        return gajiPokok;
    }

    public Optional<BigDecimalFilter> optionalGajiPokok() {
        return Optional.ofNullable(gajiPokok);
    }

    public BigDecimalFilter gajiPokok() {
        if (gajiPokok == null) {
            setGajiPokok(new BigDecimalFilter());
        }
        return gajiPokok;
    }

    public void setGajiPokok(BigDecimalFilter gajiPokok) {
        this.gajiPokok = gajiPokok;
    }

    public BigDecimalFilter getBonus() {
        return bonus;
    }

    public Optional<BigDecimalFilter> optionalBonus() {
        return Optional.ofNullable(bonus);
    }

    public BigDecimalFilter bonus() {
        if (bonus == null) {
            setBonus(new BigDecimalFilter());
        }
        return bonus;
    }

    public void setBonus(BigDecimalFilter bonus) {
        this.bonus = bonus;
    }

    public BigDecimalFilter getPotongan() {
        return potongan;
    }

    public Optional<BigDecimalFilter> optionalPotongan() {
        return Optional.ofNullable(potongan);
    }

    public BigDecimalFilter potongan() {
        if (potongan == null) {
            setPotongan(new BigDecimalFilter());
        }
        return potongan;
    }

    public void setPotongan(BigDecimalFilter potongan) {
        this.potongan = potongan;
    }

    public BigDecimalFilter getTotalGaji() {
        return totalGaji;
    }

    public Optional<BigDecimalFilter> optionalTotalGaji() {
        return Optional.ofNullable(totalGaji);
    }

    public BigDecimalFilter totalGaji() {
        if (totalGaji == null) {
            setTotalGaji(new BigDecimalFilter());
        }
        return totalGaji;
    }

    public void setTotalGaji(BigDecimalFilter totalGaji) {
        this.totalGaji = totalGaji;
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
        final PenggajianCriteria that = (PenggajianCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(bulan, that.bulan) &&
            Objects.equals(gajiPokok, that.gajiPokok) &&
            Objects.equals(bonus, that.bonus) &&
            Objects.equals(potongan, that.potongan) &&
            Objects.equals(totalGaji, that.totalGaji) &&
            Objects.equals(pegawaiId, that.pegawaiId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bulan, gajiPokok, bonus, potongan, totalGaji, pegawaiId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PenggajianCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalBulan().map(f -> "bulan=" + f + ", ").orElse("") +
            optionalGajiPokok().map(f -> "gajiPokok=" + f + ", ").orElse("") +
            optionalBonus().map(f -> "bonus=" + f + ", ").orElse("") +
            optionalPotongan().map(f -> "potongan=" + f + ", ").orElse("") +
            optionalTotalGaji().map(f -> "totalGaji=" + f + ", ").orElse("") +
            optionalPegawaiId().map(f -> "pegawaiId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
