package com.mycompany.sistemkepegawaian.repository;

import com.mycompany.sistemkepegawaian.domain.Penggajian;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Penggajian entity.
 */
@Repository
public interface PenggajianRepository extends JpaRepository<Penggajian, Long>, JpaSpecificationExecutor<Penggajian> {
    default Optional<Penggajian> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Penggajian> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Penggajian> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select penggajian from Penggajian penggajian left join fetch penggajian.pegawai",
        countQuery = "select count(penggajian) from Penggajian penggajian"
    )
    Page<Penggajian> findAllWithToOneRelationships(Pageable pageable);

    @Query("select penggajian from Penggajian penggajian left join fetch penggajian.pegawai")
    List<Penggajian> findAllWithToOneRelationships();

    @Query("select penggajian from Penggajian penggajian left join fetch penggajian.pegawai where penggajian.id =:id")
    Optional<Penggajian> findOneWithToOneRelationships(@Param("id") Long id);
}
