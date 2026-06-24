package com.mycompany.sistemkepegawaian.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PegawaiCriteriaTest {

    @Test
    void newPegawaiCriteriaHasAllFiltersNullTest() {
        var pegawaiCriteria = new PegawaiCriteria();
        assertThat(pegawaiCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void pegawaiCriteriaFluentMethodsCreatesFiltersTest() {
        var pegawaiCriteria = new PegawaiCriteria();

        setAllFilters(pegawaiCriteria);

        assertThat(pegawaiCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void pegawaiCriteriaCopyCreatesNullFilterTest() {
        var pegawaiCriteria = new PegawaiCriteria();
        var copy = pegawaiCriteria.copy();

        assertThat(pegawaiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(pegawaiCriteria)
        );
    }

    @Test
    void pegawaiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var pegawaiCriteria = new PegawaiCriteria();
        setAllFilters(pegawaiCriteria);

        var copy = pegawaiCriteria.copy();

        assertThat(pegawaiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(pegawaiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var pegawaiCriteria = new PegawaiCriteria();

        assertThat(pegawaiCriteria).hasToString("PegawaiCriteria{}");
    }

    private static void setAllFilters(PegawaiCriteria pegawaiCriteria) {
        pegawaiCriteria.id();
        pegawaiCriteria.nama();
        pegawaiCriteria.jabatan();
        pegawaiCriteria.departemen();
        pegawaiCriteria.gaji();
        pegawaiCriteria.cutiId();
        pegawaiCriteria.penggajianId();
        pegawaiCriteria.absensiId();
        pegawaiCriteria.distinct();
    }

    private static Condition<PegawaiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNama()) &&
                condition.apply(criteria.getJabatan()) &&
                condition.apply(criteria.getDepartemen()) &&
                condition.apply(criteria.getGaji()) &&
                condition.apply(criteria.getCutiId()) &&
                condition.apply(criteria.getPenggajianId()) &&
                condition.apply(criteria.getAbsensiId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PegawaiCriteria> copyFiltersAre(PegawaiCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNama(), copy.getNama()) &&
                condition.apply(criteria.getJabatan(), copy.getJabatan()) &&
                condition.apply(criteria.getDepartemen(), copy.getDepartemen()) &&
                condition.apply(criteria.getGaji(), copy.getGaji()) &&
                condition.apply(criteria.getCutiId(), copy.getCutiId()) &&
                condition.apply(criteria.getPenggajianId(), copy.getPenggajianId()) &&
                condition.apply(criteria.getAbsensiId(), copy.getAbsensiId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
