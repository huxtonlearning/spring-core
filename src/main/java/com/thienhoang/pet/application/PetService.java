package com.thienhoang.pet.application;

import com.thienhoang.common.interfaces.persistence.IBasePersistence;
import com.thienhoang.common.interfaces.persistence.IJpaGetAllPersistence;
import com.thienhoang.common.interfaces.services.IBaseService;
import com.thienhoang.pet.domain.models.entities.Pet;
import com.thienhoang.pet.domain.models.values.requests.PetRequest;
import com.thienhoang.pet.domain.services.persistence.PetPersistence;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PetService implements IBaseService<Pet, Long, Pet, PetRequest> {

  private final PetPersistence persistence;

  @Override
  public IBasePersistence<Pet, Long> getPersistence() {
    return persistence;
  }

  @Override
  public IJpaGetAllPersistence<Pet> getJpaGetAllPersistence() {
    return persistence;
  }
}
