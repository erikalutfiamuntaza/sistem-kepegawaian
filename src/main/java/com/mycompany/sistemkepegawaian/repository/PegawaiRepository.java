package com.mycompany.sistemkepegawaian.repository;

import com.mycompany.sistemkepegawaian.domain.Pegawai;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Pegawai entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PegawaiRepository extends JpaRepository<Pegawai, Long>, JpaSpecificationExecutor<Pegawai> {}
