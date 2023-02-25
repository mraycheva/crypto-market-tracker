package io.mraycheva.market.infrastructure.mapping;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.CheckedFunction0;
import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BaseMapper {

  ObjectMapper delegate;

  public <T> String stringify(final T object) {
    return map(
      (() -> delegate.writeValueAsString(object)),
      object,
      String.class.toString()
    );
  }

  public <T> T map(final String object, final Class<T> type) {
    return map(
      () -> (delegate.readValue(object, type)),
      object,
      type.toString()
    );
  }

  public <T> T map(final Object object, final TypeReference<T> typeReference) {
    return map(
      () -> (mapWithTypeReference(object, typeReference)),
      object,
      typeReference.getType().toString()
    );
  }

  @SuppressWarnings("unchecked")
  private <T> T mapWithTypeReference(
    final Object object,
    final TypeReference<T> typeReference
  ) {
    return (T) delegate
      .getTypeFactory()
      .constructType(typeReference)
      .getRawClass()
      .cast(object);
  }

  private <T> T map(
    final CheckedFunction0<T> mapping,
    final Object object,
    final String typeName
  ) {
    return Try
      .of(mapping)
      .onFailure(failure -> log(object, typeName, failure))
      .get();
  }

  private void log(
    final Object object,
    final String typeName,
    final Throwable failure
  ) {
    log.error("Could not map object {} to type {}", object, typeName, failure);
  }
}
