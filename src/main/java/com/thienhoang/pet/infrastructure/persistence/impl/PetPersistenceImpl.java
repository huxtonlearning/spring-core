package com.thienhoang.pet.infrastructure.persistence.impl;

import com.thienhoang.pet.domain.models.entities.Pet;
import com.thienhoang.pet.domain.services.persistence.PetPersistence;
import com.thienhoang.pet.infrastructure.persistence.entity.PetEntity;
import com.thienhoang.pet.infrastructure.persistence.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PetPersistenceImpl implements PetPersistence {

  private final PetRepository repository;

  @Override
  public Class<PetEntity> getEntityClass() {
    return PetEntity.class;
  }

  @Override
  public Class<Pet> getModelClass() {
    return Pet.class;
  }

  @Override
  public JpaRepository getJpaRepository() {
    return repository;
  }

  @Override
  public JpaSpecificationExecutor getJpaSpecificationExecutor() {
    return repository;
  }
}
