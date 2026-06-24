package com.mycompany.sistemkepegawaian.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AbsensiCriteriaTest {

    @Test
    void newAbsensiCriteriaHasAllFiltersNullTest() {
        var absensiCriteria = new AbsensiCriteria();
        assertThat(absensiCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void absensiCriteriaFluentMethodsCreatesFiltersTest() {
        var absensiCriteria = new AbsensiCriteria();

        setAllFilters(absensiCriteria);

        assertThat(absensiCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void absensiCriteriaCopyCreatesNullFilterTest() {
        var absensiCriteria = new AbsensiCriteria();
        var copy = absensiCriteria.copy();

        assertThat(absensiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(absensiCriteria)
        );
    }

    @Test
    void absensiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var absensiCriteria = new AbsensiCriteria();
        setAllFilters(absensiCriteria);

        var copy = absensiCriteria.copy();

        assertThat(absensiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(absensiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var absensiCriteria = new AbsensiCriteria();

        assertThat(absensiCriteria).hasToString("AbsensiCriteria{}");
    }

    private static void setAllFilters(AbsensiCriteria absensiCriteria) {
        absensiCriteria.id();
        absensiCriteria.tanggal();
        absensiCriteria.jamMasuk();
        absensiCriteria.jamKeluar();
        absensiCriteria.status();
        absensiCriteria.pegawaiId();
        absensiCriteria.distinct();
    }

    private static Condition<AbsensiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTanggal()) &&
                condition.apply(criteria.getJamMasuk()) &&
                condition.apply(criteria.getJamKeluar()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getPegawaiId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AbsensiCriteria> copyFiltersAre(AbsensiCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTanggal(), copy.getTanggal()) &&
                condition.apply(criteria.getJamMasuk(), copy.getJamMasuk()) &&
                condition.apply(criteria.getJamKeluar(), copy.getJamKeluar()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getPegawaiId(), copy.getPegawaiId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
