package com.thienhoang.pet.business.services;

import com.thienhoang.pet.domain.models.entities.Pet;
import com.thienhoang.pet.domain.models.values.params.PetParams;
import com.thienhoang.pet.domain.models.values.requests.PetRequest;
import com.thienhoang.pet.domain.services.repositories.PetRepository;
import com.thienhoang.pet.domain.specifications.models.values.HeaderContext;
import com.thienhoang.pet.domain.specifications.services.IBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
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

  @Override
  public void mappingCreateEntity(HeaderContext context, Pet entity, PetRequest request) {
    IBaseService.super.mappingCreateEntity(context, entity, request);
    entity.setCreatorId(context.getUserId());
    entity.setModifierId(context.getUserId());
  }

  @Override
  public void mappingUpdateEntity(HeaderContext context, Pet entity, PetRequest request) {
    IBaseService.super.mappingUpdateEntity(context, entity, request);
    entity.setModifierId(context.getUserId());
  }

  @Override
  public Specification<Pet> buildQuery(HeaderContext context, PetParams params) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
  }
}
