package com.thienhoang.pet.domain.services.repositories;

import com.thienhoang.pet.domain.models.entities.Pet;
import com.thienhoang.pet.domain.models.values.params.PetParams;
import com.thienhoang.pet.domain.specifications.models.values.HeaderContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface PetRepository extends JpaRepository<Pet, Long>, JpaSpecificationExecutor<Pet> {

  @Query(
      value =
          "select v  "
              + "from Pet v "
              + "where (:#{#params.type == null } = true or v.type = :#{#params.type})")
  Page<Pet> filter(HeaderContext context, PetParams params, Pageable pageable);
}
