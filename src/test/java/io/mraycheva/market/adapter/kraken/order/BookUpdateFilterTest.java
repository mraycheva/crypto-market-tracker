package io.mraycheva.market.adapter.kraken.order;

import io.mraycheva.market.infrastructure.websocket.MessageFilter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.socket.WebSocketMessage;
import test.util.FileReader;
import test.util.MessageFilePath;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class BookUpdateFilterTest {

  MessageFilter filter;

  @BeforeEach
  void setup() {
    filter = new BookUpdateFilter();
  }

  @ParameterizedTest
  @ValueSource(
    strings = {
      MessageFilePath.SYSTEM_STATUS,
      MessageFilePath.SUBSCRIPTION_STATUS,
      MessageFilePath.HEARTBEAT,
    }
  )
  void givenStatusMessages_shouldKeep_returnsFalse(
    final String OriginalMessageFilePath
  ) {
    // GIVEN
    val webSocketMessage = getWebSocketMessageMock(OriginalMessageFilePath);
    // WHEN
    val result = filter.shouldKeep(webSocketMessage);
    // THEN
    Assertions.assertFalse(result);
  }

  @ParameterizedTest
  @ValueSource(
    strings = {
      MessageFilePath.ASKS_AND_BIDS_SNAPSHOT,
      MessageFilePath.ASKS_AND_BIDS_UPDATE,
      MessageFilePath.ASKS_UPDATE,
      MessageFilePath.BIDS_UPDATE,
    }
  )
  void givenUpdateMessages_shouldKeep_returnsTrue(
    final String OriginalMessageFilePath
  ) {
    // GIVEN
    val webSocketMessage = getWebSocketMessageMock(OriginalMessageFilePath);
    // WHEN
    val result = filter.shouldKeep(webSocketMessage);
    // THEN
    Assertions.assertTrue(result);
  }

  private WebSocketMessage getWebSocketMessageMock(
    final String OriginalMessageFilePath
  ) {
    val webSocketMessage = Mockito.mock(WebSocketMessage.class);
    Mockito
      .when(webSocketMessage.getPayloadAsText())
      .thenReturn(FileReader.read(OriginalMessageFilePath));
    return webSocketMessage;
  }
}
