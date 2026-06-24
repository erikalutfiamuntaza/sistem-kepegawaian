package com.mycompany.sistemkepegawaian.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AbsensiTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Absensi getAbsensiSample1() {
        return new Absensi().id(1L).status("status1");
    }

    public static Absensi getAbsensiSample2() {
        return new Absensi().id(2L).status("status2");
    }

    public static Absensi getAbsensiRandomSampleGenerator() {
        return new Absensi().id(longCount.incrementAndGet()).status(UUID.randomUUID().toString());
    }
}
