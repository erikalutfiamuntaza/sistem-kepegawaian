package com.mycompany.sistemkepegawaian.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PenggajianTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Penggajian getPenggajianSample1() {
        return new Penggajian().id(1L);
    }

    public static Penggajian getPenggajianSample2() {
        return new Penggajian().id(2L);
    }

    public static Penggajian getPenggajianRandomSampleGenerator() {
        return new Penggajian().id(longCount.incrementAndGet());
    }
}
