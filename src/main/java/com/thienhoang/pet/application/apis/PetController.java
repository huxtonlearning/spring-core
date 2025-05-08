package com.thienhoang.pet.application.apis;

import com.thienhoang.pet.domain.models.entities.Pet;
import com.thienhoang.pet.domain.models.values.params.PetParams;
import com.thienhoang.pet.domain.models.values.requests.PetRequest;
import com.thienhoang.pet.domain.services.PetService;
import com.thienhoang.pet.domain.specifications.api.CrudControllerSpecification;
import com.thienhoang.pet.domain.specifications.models.values.HeaderContext;
import com.thienhoang.pet.domain.specifications.models.values.response.ResponsePage;
import com.thienhoang.pet.domain.specifications.services.CrudServiceSpecification;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/pets")
@RequiredArgsConstructor
public class PetController implements CrudControllerSpecification<Pet, Long, Pet, PetRequest> {

  private final PetService service;

  @Override
  public CrudServiceSpecification<Pet, Long, Pet, PetRequest> getService() {
    return service;
  }

  @Override
  public Pet create(HeaderContext context, PetRequest request) {
    return CrudControllerSpecification.super.create(context, request);
  }

  @Override
  public Pet update(HeaderContext context, Long id, PetRequest request) {
    return CrudControllerSpecification.super.update(context, id, request);
  }

  @Override
  public void delete(HeaderContext context, Long id) {
    CrudControllerSpecification.super.delete(context, id);
  }

  @Override
  public Pet getById(HeaderContext context, Long id) {
    return CrudControllerSpecification.super.getById(context, id);
  }

  @GetMapping
  public ResponsePage<Pet> filter(
      @Parameter(hidden = true) HeaderContext context, PetParams params, Pageable pageable) {

    return new ResponsePage<>(service.filter(context, params, pageable));
  }
}
