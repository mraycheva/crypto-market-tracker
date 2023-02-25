package io.mraycheva.market.infrastructure.mapping;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@FieldDefaults(level = AccessLevel.PRIVATE)
class MappingConfigurationTest {

  MappingConfiguration configuration;

  @BeforeEach
  void setUp() {
    configuration = new MappingConfiguration();
  }

  @Test
  void objectMapper_returnsProperlyConfiguredMapper() {
    val mapper = configuration.objectMapper();
    val deserializationConfig = mapper.getDeserializationConfig();
    Assertions.assertFalse(
      deserializationConfig.isEnabled(FAIL_ON_UNKNOWN_PROPERTIES)
    );
  }
}
