package com.thienhoang.pet.domain.models.enums;

import lombok.Getter;

@Getter
public enum PetType {
  BIRD((byte) 0),
  ANIMAL((byte) 1);

  private final byte ordinal;

  PetType(byte ordinal) {
    this.ordinal = ordinal;
  }

  public static PetType fromValue(byte value) {

    return PetType.values()[value];
  }
}
