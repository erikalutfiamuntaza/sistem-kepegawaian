package com.mycompany.sistemkepegawaian.repository;

import com.mycompany.sistemkepegawaian.domain.Cuti;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Cuti entity.
 */
@Repository
public interface CutiRepository extends JpaRepository<Cuti, Long>, JpaSpecificationExecutor<Cuti> {
    default Optional<Cuti> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Cuti> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Cuti> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select cuti from Cuti cuti left join fetch cuti.pegawai", countQuery = "select count(cuti) from Cuti cuti")
    Page<Cuti> findAllWithToOneRelationships(Pageable pageable);

    @Query("select cuti from Cuti cuti left join fetch cuti.pegawai")
    List<Cuti> findAllWithToOneRelationships();

    @Query("select cuti from Cuti cuti left join fetch cuti.pegawai where cuti.id =:id")
    Optional<Cuti> findOneWithToOneRelationships(@Param("id") Long id);
}
