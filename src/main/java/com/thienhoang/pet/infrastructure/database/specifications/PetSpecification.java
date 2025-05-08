package com.thienhoang.pet.infrastructure.database.specifications;

import com.thienhoang.pet.domain.models.entities.Pet;
import com.thienhoang.pet.domain.models.values.params.PetParams;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.Objects;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class PetSpecification implements Specification<Pet> {

  private PetParams params;

  public PetSpecification(PetParams params) {
    this.params = params;
  }

  @Override
  public Predicate toPredicate(
      Root<Pet> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    Predicate predicate = criteriaBuilder.conjunction(); // Khởi tạo một AND condition

    if (StringUtils.hasLength(params.getKeyword())) {
      Predicate keywordPredicate =
          criteriaBuilder.like(root.get("name"), "%" + params.getKeyword() + "%");
      predicate = criteriaBuilder.and(predicate, keywordPredicate);
    }

    if (Objects.nonNull(params.getType())) {
      Predicate typePredicate = criteriaBuilder.equal(root.get("type"), params.getType());
      predicate = criteriaBuilder.and(predicate, typePredicate);
    }

    if (Objects.nonNull(params.getFromCreatedAt())) {
      Predicate fromCreatedAtPredicate =
          criteriaBuilder.ge(root.get("createdAt"), params.getFromCreatedAt().getTime());
      predicate = criteriaBuilder.and(predicate, fromCreatedAtPredicate);
    }

    if (Objects.nonNull(params.getToCreatedAt())) {
      Predicate toCreatedAtPredicate =
          criteriaBuilder.ge(root.get("createdAt"), params.getToCreatedAt().getTime());
      predicate = criteriaBuilder.and(predicate, toCreatedAtPredicate);
    }

    return predicate;
  }
}
