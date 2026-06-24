package com.mycompany.sistemkepegawaian.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PenggajianCriteriaTest {

    @Test
    void newPenggajianCriteriaHasAllFiltersNullTest() {
        var penggajianCriteria = new PenggajianCriteria();
        assertThat(penggajianCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void penggajianCriteriaFluentMethodsCreatesFiltersTest() {
        var penggajianCriteria = new PenggajianCriteria();

        setAllFilters(penggajianCriteria);

        assertThat(penggajianCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void penggajianCriteriaCopyCreatesNullFilterTest() {
        var penggajianCriteria = new PenggajianCriteria();
        var copy = penggajianCriteria.copy();

        assertThat(penggajianCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(penggajianCriteria)
        );
    }

    @Test
    void penggajianCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var penggajianCriteria = new PenggajianCriteria();
        setAllFilters(penggajianCriteria);

        var copy = penggajianCriteria.copy();

        assertThat(penggajianCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(penggajianCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var penggajianCriteria = new PenggajianCriteria();

        assertThat(penggajianCriteria).hasToString("PenggajianCriteria{}");
    }

    private static void setAllFilters(PenggajianCriteria penggajianCriteria) {
        penggajianCriteria.id();
        penggajianCriteria.bulan();
        penggajianCriteria.gajiPokok();
        penggajianCriteria.bonus();
        penggajianCriteria.potongan();
        penggajianCriteria.totalGaji();
        penggajianCriteria.pegawaiId();
        penggajianCriteria.distinct();
    }

    private static Condition<PenggajianCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getBulan()) &&
                condition.apply(criteria.getGajiPokok()) &&
                condition.apply(criteria.getBonus()) &&
                condition.apply(criteria.getPotongan()) &&
                condition.apply(criteria.getTotalGaji()) &&
                condition.apply(criteria.getPegawaiId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PenggajianCriteria> copyFiltersAre(PenggajianCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getBulan(), copy.getBulan()) &&
                condition.apply(criteria.getGajiPokok(), copy.getGajiPokok()) &&
                condition.apply(criteria.getBonus(), copy.getBonus()) &&
                condition.apply(criteria.getPotongan(), copy.getPotongan()) &&
                condition.apply(criteria.getTotalGaji(), copy.getTotalGaji()) &&
                condition.apply(criteria.getPegawaiId(), copy.getPegawaiId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
