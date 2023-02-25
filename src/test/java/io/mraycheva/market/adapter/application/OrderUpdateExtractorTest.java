package io.mraycheva.market.adapter.application;

import io.mraycheva.market.order.BookUpdate;
import io.vavr.control.Option;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.socket.WebSocketMessage;
import reactor.core.publisher.Flux;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class OrderUpdateExtractorTest {

  @Mock
  BookUpdateParser parser;

  @InjectMocks
  BookUpdateMapper bookUpdateMapper;

  @Nested
  class SingleMessage {

    private static final String MESSAGE_PAYLOAD = "MESSAGE_PAYLOAD";

    @Test
    void givenNoMappedUpdate_map_alsoReturnsNothing() {
      // GIVEN
      Mockito.when(parser.parse(MESSAGE_PAYLOAD)).thenReturn(Option.none());
      // WHEN
      val result = bookUpdateMapper.map(getMessages());
      // THEN
      Assertions.assertEquals(0, result.count().block());
    }

    @Test
    void givenMappedUpdate_map_returnsUpdate() {
      // GIVEN
      val update = Mockito.mock(BookUpdate.class);
      Mockito
        .when(parser.parse(MESSAGE_PAYLOAD))
        .thenReturn(Option.some(update));
      // WHEN
      val result = bookUpdateMapper.map(getMessages());
      // THEN
      verifySingleMappedUpdate(update, result);
    }

    private Flux<WebSocketMessage> getMessages() {
      return Flux.just(getUpdateMessage(MESSAGE_PAYLOAD));
    }
  }

  @Nested
  class MultipleMessages {

    public static final BookUpdate BOOK_UPDATE = Mockito.mock(BookUpdate.class);
    private static final Map<String, Option<BookUpdate>> BOOK_UPDATES = Map.of(
      "VALID_MESSAGE_PAYLOAD",
      Option.some(BOOK_UPDATE),
      "INVALID_MESSAGE_PAYLOAD",
      Option.none()
    );

    @Test
    void givenMappedAndUnmappedMessages_map_returnsMappedUpdatesOnly() {
      // GIVEN
      BOOK_UPDATES.forEach((payload, update) ->
        Mockito.when(parser.parse(payload)).thenReturn(update)
      );
      val messages = Flux.just(getUpdateMessages());
      // WHEN
      val result = bookUpdateMapper.map(messages);
      // THEN
      verifySingleMappedUpdate(BOOK_UPDATE, result);
    }

    private WebSocketMessage[] getUpdateMessages() {
      return BOOK_UPDATES
        .keySet()
        .stream()
        .map(OrderUpdateExtractorTest::getUpdateMessage)
        .toList()
        .toArray(WebSocketMessage[]::new);
    }
  }

  private static WebSocketMessage getUpdateMessage(final String update) {
    val webSocketMessage = Mockito.mock(WebSocketMessage.class);
    Mockito.when(webSocketMessage.getPayloadAsText()).thenReturn(update);
    return webSocketMessage;
  }

  private void verifySingleMappedUpdate(
    final BookUpdate update,
    final Flux<BookUpdate> result
  ) {
    val resultUpdates = result.collect(Collectors.toList()).block();
    Assertions.assertNotNull(resultUpdates);
    Assertions.assertEquals(1, resultUpdates.size());
    Assertions.assertTrue(resultUpdates.contains(update));
  }
}
