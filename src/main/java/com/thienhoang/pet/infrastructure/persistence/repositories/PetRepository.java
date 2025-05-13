package com.thienhoang.pet.infrastructure.persistence.repositories;

import com.thienhoang.common.interfaces.repository.IBaseRepository;
import com.thienhoang.pet.domain.models.entities.Pet;

public interface PetRepository extends IBaseRepository<Pet, Long> {}
