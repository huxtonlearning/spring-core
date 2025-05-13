package com.thienhoang.pet.application.apis;

import com.thienhoang.common.interfaces.api.IBaseController;
import com.thienhoang.common.interfaces.services.IBaseService;
import com.thienhoang.pet.business.services.PetService;
import com.thienhoang.pet.domain.models.entities.Pet;
import com.thienhoang.pet.domain.models.values.requests.PetRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/pets")
@RequiredArgsConstructor
public class PetController implements IBaseController<Pet, Long, Pet, PetRequest> {

  private final PetService service;

  @Override
  public IBaseService<Pet, Long, Pet, PetRequest> getService() {
    return service;
  }
}
