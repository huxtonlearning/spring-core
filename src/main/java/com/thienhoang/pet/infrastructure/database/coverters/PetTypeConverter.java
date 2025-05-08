package com.thienhoang.pet.infrastructure.database.coverters;

import com.thienhoang.pet.domain.models.enums.PetType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PetTypeConverter implements AttributeConverter<PetType, Byte> {

  @Override
  public Byte convertToDatabaseColumn(PetType petType) {
    if (petType == null) {
      return null;
    }
    return petType.getOrdinal();
  }

  @Override
  public PetType convertToEntityAttribute(Byte aByte) {
    if (aByte == null) {
      return null;
    }
    return PetType.fromValue(aByte);
  }
}
