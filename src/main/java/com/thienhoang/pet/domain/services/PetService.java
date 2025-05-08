package com.thienhoang.pet.domain.services;

import com.thienhoang.pet.domain.models.entities.Pet;
import com.thienhoang.pet.domain.models.values.params.PetParams;
import com.thienhoang.pet.domain.models.values.requests.PetRequest;
import com.thienhoang.pet.domain.services.repositories.PetRepository;
import com.thienhoang.pet.domain.specifications.models.values.HeaderContext;
import com.thienhoang.pet.domain.specifications.services.CrudServiceSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PetService implements CrudServiceSpecification<Pet, Long, Pet, PetRequest> {

  private final PetRepository repository;

  @Override
  public JpaRepository<Pet, Long> getRepository() {
    return repository;
  }

  @Override
  public Pet mappingResponse(HeaderContext context, Pet entity) {
    return entity;
  }

  @Override
  public void validateUpdateRequest(
      HeaderContext context, Long aLong, PetRequest request, Pet entity) {
    CrudServiceSpecification.super.validateUpdateRequest(context, aLong, request, entity);
  }

  @Override
  public void validateCreateRequest(HeaderContext context, PetRequest request, Pet entity) {
    CrudServiceSpecification.super.validateCreateRequest(context, request, entity);
  }

  @Override
  public void mappingCreateEntity(HeaderContext context, PetRequest request, Pet entity) {
    entity.setCreatorId(context.getUserId());
    entity.setModifierId(context.getUserId());
  }

  @Override
  public void mappingUpdateEntity(HeaderContext context, PetRequest request, Pet entity) {
    entity.setModifierId(context.getUserId());
  }

  public Page<Pet> filter(HeaderContext context, PetParams params, Pageable pageable) {

    return repository.filter(context, params, pageable);
  }
}
