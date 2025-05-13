package com.thienhoang.pet.infrastructure.persistence;

import com.thienhoang.common.interfaces.repository.IBaseRepository;
import com.thienhoang.pet.domain.models.entities.Pet;
import com.thienhoang.pet.domain.services.persistence.PetPersistence;
import com.thienhoang.pet.infrastructure.persistence.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JpaPetPersistence implements PetPersistence {

  private final PetRepository repository;

  @Override
  public IBaseRepository<Pet, Long> getIBaseRepository() {
    return repository;
  }
}
