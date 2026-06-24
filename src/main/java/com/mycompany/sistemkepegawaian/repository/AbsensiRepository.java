package com.mycompany.sistemkepegawaian.repository;

import com.mycompany.sistemkepegawaian.domain.Absensi;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Absensi entity.
 */
@Repository
public interface AbsensiRepository extends JpaRepository<Absensi, Long>, JpaSpecificationExecutor<Absensi> {
    default Optional<Absensi> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Absensi> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Absensi> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select absensi from Absensi absensi left join fetch absensi.pegawai",
        countQuery = "select count(absensi) from Absensi absensi"
    )
    Page<Absensi> findAllWithToOneRelationships(Pageable pageable);

    @Query("select absensi from Absensi absensi left join fetch absensi.pegawai")
    List<Absensi> findAllWithToOneRelationships();

    @Query("select absensi from Absensi absensi left join fetch absensi.pegawai where absensi.id =:id")
    Optional<Absensi> findOneWithToOneRelationships(@Param("id") Long id);
}
