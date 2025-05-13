package com.thienhoang.pet.adapter.apis;

import com.thienhoang.common.interfaces.api.IBaseController;
import com.thienhoang.common.interfaces.services.IBaseService;
import com.thienhoang.pet.application.requests.PetRequest;
import com.thienhoang.pet.application.usecases.PetService;
import com.thienhoang.pet.domain.models.entities.Pet;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/pets")
@RequiredArgsConstructor
public class PetApi implements IBaseController<Pet, Long, Pet, PetRequest> {

  private final PetService service;

  @Override
  public IBaseService<Pet, Long, Pet, PetRequest> getService() {
    return service;
  }
}
