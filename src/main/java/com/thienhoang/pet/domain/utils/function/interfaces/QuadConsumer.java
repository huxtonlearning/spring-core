package com.thienhoang.pet.domain.utils.function.interfaces;

@FunctionalInterface
public interface QuadConsumer<A, B, C, D> {

  void accept(A a, B b, C c, D d);

  default QuadConsumer<A, B, C, D> andThen(
      QuadConsumer<? super A, ? super B, ? super C, ? super D> after) {
    return (a, b, c, d) -> {
      accept(a, b, c, d);
      after.accept(a, b, c, d);
    };
  }
}
