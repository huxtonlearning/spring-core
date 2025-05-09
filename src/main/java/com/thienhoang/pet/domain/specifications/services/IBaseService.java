package com.thienhoang.pet.domain.specifications.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IBaseService<E, ID, RES, REQ, PARAMS>
    extends ICrudService<E, ID, RES, REQ>, IGetAllService<E, RES, PARAMS> {
  JpaRepository<E, ID> getRepository();

  @Override
  default JpaRepository<E, ID> getJpaRepository() {
    return getRepository();
  }

  @Override
  @SuppressWarnings("unchecked")
  default JpaSpecificationExecutor<E> getSpecificationExecutor() {
    return (JpaSpecificationExecutor<E>) getRepository();
  }
}
