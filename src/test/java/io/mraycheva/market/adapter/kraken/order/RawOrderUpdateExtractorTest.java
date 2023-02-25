package io.mraycheva.market.adapter.kraken.order;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import test.util.FileReader;
import test.util.MessageFilePath;
import test.util.UpdateFilePath;

@FieldDefaults(level = AccessLevel.PRIVATE)
class RawOrderUpdateExtractorTest {

  public static final TypeReference<List<RawOrderCollectionUpdate>> EXTRACTION_RESULT_TYPE_REFERENCE = new TypeReference<>() {};

  RawOrderUpdateExtractor extractor;

  @BeforeEach
  void setup() {
    extractor = new RawOrderUpdateExtractor();
  }

  @Test
  void givenNullPayloadElements_getAll_fails() {
    // THEN
    Assertions.assertThrows(
      IllegalArgumentException.class,
      // WHEN
      () -> extractor.getAll(null)
    );
  }

  @ParameterizedTest
  @MethodSource("getExtractionArguments")
  void givenNonNullPayloadElements_getAll_returnsMapOfRawUpdates(
    final String messageFilePath,
    final List<RawOrderCollectionUpdate> expectedUpdates
  ) {
    // GIVEN
    val payloadElements = FileReader.read(messageFilePath, Object[].class);
    // WHEN
    val result = extractor.getAll(payloadElements);
    // THEN
    Assertions.assertEquals(expectedUpdates, result);
  }

  private static Stream<Arguments> getExtractionArguments() {
    return Stream.of(
      getExtractionArguments(
        MessageFilePath.ASKS_AND_BIDS_UPDATE,
        UpdateFilePath.ASKS_AND_BIDS_UPDATE_RAW
      ),
      getExtractionArguments(
        MessageFilePath.ASKS_AND_BIDS_SNAPSHOT,
        UpdateFilePath.ASKS_AND_BIDS_SNAPSHOT_RAW
      )
    );
  }

  private static Arguments getExtractionArguments(
    final String inputMessageFilePath,
    final String outputMessageFilePath
  ) {
    return Arguments.of(
      inputMessageFilePath,
      FileReader.read(outputMessageFilePath, EXTRACTION_RESULT_TYPE_REFERENCE)
    );
  }
}
