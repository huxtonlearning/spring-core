package com.thienhoang.pet.business.services;

import com.thienhoang.pet.domain.models.entities.Pet;
import com.thienhoang.pet.domain.models.values.params.PetParams;
import com.thienhoang.pet.domain.models.values.requests.PetRequest;
import com.thienhoang.pet.domain.services.repositories.PetRepository;
import com.thienhoang.pet.domain.specifications.services.IBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PetService implements IBaseService<Pet, Long, Pet, PetRequest, PetParams> {

  private final PetRepository repository;

  @Override
  public JpaRepository<Pet, Long> getRepository() {
    return repository;
  }
}
