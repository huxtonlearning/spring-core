package com.thienhoang.pet.domain.specifications.services;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IJpaRepositoryProvider<E, ID> {

  JpaRepository<E, ID> getJpaRepository();
}
