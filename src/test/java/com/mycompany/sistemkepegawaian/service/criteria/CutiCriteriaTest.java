package com.mycompany.sistemkepegawaian.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CutiCriteriaTest {

    @Test
    void newCutiCriteriaHasAllFiltersNullTest() {
        var cutiCriteria = new CutiCriteria();
        assertThat(cutiCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void cutiCriteriaFluentMethodsCreatesFiltersTest() {
        var cutiCriteria = new CutiCriteria();

        setAllFilters(cutiCriteria);

        assertThat(cutiCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void cutiCriteriaCopyCreatesNullFilterTest() {
        var cutiCriteria = new CutiCriteria();
        var copy = cutiCriteria.copy();

        assertThat(cutiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(cutiCriteria)
        );
    }

    @Test
    void cutiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cutiCriteria = new CutiCriteria();
        setAllFilters(cutiCriteria);

        var copy = cutiCriteria.copy();

        assertThat(cutiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(cutiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cutiCriteria = new CutiCriteria();

        assertThat(cutiCriteria).hasToString("CutiCriteria{}");
    }

    private static void setAllFilters(CutiCriteria cutiCriteria) {
        cutiCriteria.id();
        cutiCriteria.tanggalMulai();
        cutiCriteria.tanggalSelesai();
        cutiCriteria.alasan();
        cutiCriteria.status();
        cutiCriteria.pegawaiId();
        cutiCriteria.distinct();
    }

    private static Condition<CutiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTanggalMulai()) &&
                condition.apply(criteria.getTanggalSelesai()) &&
                condition.apply(criteria.getAlasan()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getPegawaiId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CutiCriteria> copyFiltersAre(CutiCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTanggalMulai(), copy.getTanggalMulai()) &&
                condition.apply(criteria.getTanggalSelesai(), copy.getTanggalSelesai()) &&
                condition.apply(criteria.getAlasan(), copy.getAlasan()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getPegawaiId(), copy.getPegawaiId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
