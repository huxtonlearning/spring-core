package com.thienhoang.pet.domain.specifications.services;

import com.thienhoang.pet.domain.specifications.models.values.HeaderContext;
import com.thienhoang.pet.domain.utils.FnCommon;
import com.thienhoang.pet.domain.utils.GenericTypeUtils;
import com.thienhoang.pet.domain.utils.JsonParserUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.Attribute;
import java.util.*;
import java.util.function.BiFunction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.util.StringUtils;

public interface IGetAllService<E, RES> {

  default JpaSpecificationExecutor<E> getSpecificationExecutor() {

    return null;
  }

  default Specification<E> buildQuery(
      HeaderContext context, String search, Map<String, Object> filter) {

    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();
      List<Predicate> searchPredicates =
          buildSearchQuery(root.getModel().getAttributes(), root, query, cb, search);
      List<Predicate> entityPredicates =
          buildEntityQuery(root.getModel().getAttributes(), root, query, cb, filter);
      List<Predicate> filterPredicates = buildFilterQuery(root, query, cb, filter);

      predicates.addAll(searchPredicates);
      predicates.addAll(entityPredicates);
      predicates.addAll(filterPredicates);
      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }

  default List<Predicate> buildFilterQuery(
      Root<E> root, CriteriaQuery<?> query, CriteriaBuilder cb, Map<String, Object> filter) {
    return new ArrayList<>();
  }

  default List<Predicate> buildEntityQuery(
      Set<Attribute<? super E, ?>> fieldNames,
      Root<E> root,
      CriteriaQuery<?> query,
      CriteriaBuilder cb,
      Map<String, Object> filter) {

    List<Predicate> predicates = new ArrayList<>();
    fieldNames.forEach(
        attribute -> {
          String fieldName = attribute.getName();
          Object value = filter.get(fieldName);

          if (attribute.getJavaType().isEnum() && value instanceof String) {
            value =
                Enum.valueOf(
                    (Class<Enum>) attribute.getJavaType().asSubclass(Enum.class), value.toString());
          }

          if (value != null) {
            predicates.add(cb.equal(root.get(fieldName), value));
          }
        });
    return predicates;
  }

  default String[] getSearchFieldNames() {

    return new String[] {"name"};
  }

  default List<Predicate> buildSearchQuery(
      Set<Attribute<? super E, ?>> fieldNames,
      Root<E> root,
      CriteriaQuery<?> query,
      CriteriaBuilder cb,
      String search) {
    String[] searchFieldNames = getSearchFieldNames();
    List<Predicate> searchPredicates = new ArrayList<>();
    for (String fieldName : searchFieldNames) {
      boolean isSearch = fieldNames.contains(fieldName);
      if (isSearch && StringUtils.hasLength(search)) {
        Predicate searchPredicate =
            cb.like(cb.lower(root.get(fieldName)), "%" + search.toLowerCase() + "%");

        searchPredicates.add(searchPredicate);
      }
    }

    return searchPredicates;
  }

  @SuppressWarnings("unchecked")
  default Page<RES> getAll(
      HeaderContext context,
      String search,
      Pageable pageable,
      String filter,
      BiFunction<HeaderContext, E, RES> mappingResponseHandler) {
    Map<String, Object> filterMap = new HashMap<>();

    try {
      filterMap = JsonParserUtils.entity(filter, Map.class);
    } catch (Exception e) {
      e.printStackTrace();
    }
    Page<E> data =
        getSpecificationExecutor().findAll(buildQuery(context, search, filterMap), pageable);

    return data.map(item -> mappingResponseHandler.apply(context, item));
  }

  default Page<RES> getAll(HeaderContext context, String search, Pageable pageable, String filter) {

    return getAll(context, search, pageable, filter, this::mappingPageResponse);
  }

  default RES mappingPageResponse(HeaderContext context, E item) {
    RES resItem = GenericTypeUtils.getNewInstance(this);

    FnCommon.copyProperties(resItem, item);

    return resItem;
  }
}
