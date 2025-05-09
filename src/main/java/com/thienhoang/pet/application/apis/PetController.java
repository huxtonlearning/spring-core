package com.thienhoang.pet.application.apis;

import com.thienhoang.pet.business.services.PetService;
import com.thienhoang.pet.domain.models.entities.Pet;
import com.thienhoang.pet.domain.models.values.params.PetParams;
import com.thienhoang.pet.domain.models.values.requests.PetRequest;
import com.thienhoang.pet.domain.specifications.api.IBaseController;
import com.thienhoang.pet.domain.specifications.services.IBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/pets")
@RequiredArgsConstructor
public class PetController implements IBaseController<Pet, Long, Pet, PetRequest, PetParams> {

  private final PetService service;

  @Override
  public IBaseService<Pet, Long, Pet, PetRequest, PetParams> getService() {
    return service;
  }
}
