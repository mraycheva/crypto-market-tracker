package io.mraycheva.market.infrastructure.mapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class BaseMapperTest {

  private static final String INPUT_VALUE = "input value";
  private static final String OUTPUT_VALUE = "output value";

  @Spy
  ObjectMapper delegate;

  @InjectMocks
  BaseMapper mapper;

  @Test
  void givenDelegateDoesNotThrowException_stringify_returnsObjectString()
    throws JsonProcessingException {
    // GIVEN
    Mockito
      .doReturn(OUTPUT_VALUE)
      .when(delegate)
      .writeValueAsString(INPUT_VALUE);

    // WHEN
    val result = mapper.stringify(INPUT_VALUE);

    // THEN
    Assertions.assertEquals(OUTPUT_VALUE, result);
  }

  @Test
  void givenClassInputAndDelegateNotThrowingException_map_returnsMappedObject()
    throws JsonProcessingException {
    // GIVEN
    Mockito
      .doReturn(OUTPUT_VALUE)
      .when(delegate)
      .readValue(INPUT_VALUE, String.class);

    // WHEN
    val result = mapper.map(INPUT_VALUE, String.class);

    // THEN
    Assertions.assertEquals(OUTPUT_VALUE, result);
  }

  @Nested
  class MapWithTypeReference {

    @Test
    void givenInvalidTypeReference_map_fails() {
      // GIVEN
      val stringValue = "string value";
      val type = new TypeReference<Integer>() {};

      // THEN
      Assertions.assertThrows(
        ClassCastException.class,
        // WHEN
        () -> mapper.map(stringValue, type)
      );
    }

    @Test
    void givenValidTypeReference_map_returnsMappedObject() {
      // GIVEN
      val stringValue = "string value";

      // WHEN
      val result = mapper.map(stringValue, new TypeReference<String>() {});

      // THEN
      Assertions.assertEquals(stringValue, result);
    }
  }
}
