package com.mycompany.sistemkepegawaian.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CutiTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Cuti getCutiSample1() {
        return new Cuti().id(1L).alasan("alasan1").status("status1");
    }

    public static Cuti getCutiSample2() {
        return new Cuti().id(2L).alasan("alasan2").status("status2");
    }

    public static Cuti getCutiRandomSampleGenerator() {
        return new Cuti().id(longCount.incrementAndGet()).alasan(UUID.randomUUID().toString()).status(UUID.randomUUID().toString());
    }
}
