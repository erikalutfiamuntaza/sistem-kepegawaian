package com.mycompany.sistemkepegawaian.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PegawaiTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Pegawai getPegawaiSample1() {
        return new Pegawai().id(1L).nama("nama1").jabatan("jabatan1").departemen("departemen1");
    }

    public static Pegawai getPegawaiSample2() {
        return new Pegawai().id(2L).nama("nama2").jabatan("jabatan2").departemen("departemen2");
    }

    public static Pegawai getPegawaiRandomSampleGenerator() {
        return new Pegawai()
            .id(longCount.incrementAndGet())
            .nama(UUID.randomUUID().toString())
            .jabatan(UUID.randomUUID().toString())
            .departemen(UUID.randomUUID().toString());
    }
}
