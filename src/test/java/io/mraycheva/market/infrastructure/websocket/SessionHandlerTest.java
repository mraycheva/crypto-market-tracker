package io.mraycheva.market.infrastructure.websocket;

import io.mraycheva.market.order.BookUpdate;
import io.vavr.control.Try;
import java.util.function.Predicate;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class SessionHandlerTest {

  @Mock
  MessageFilter messageFilter;

  @Mock
  MessageHandler<BookUpdate> messageHandler;

  @Mock
  MessageMapper<BookUpdate> messageMapper;

  @Mock
  SubscriptionRequestProvider subscriptionRequestProvider;

  @InjectMocks
  SessionHandler<BookUpdate> sessionHandler;

  @Test
  void givenDownStreamCallFails_handle_fails() {
    // GIVEN
    val session = Mockito.mock(WebSocketSession.class);
    val exception = new RuntimeException("error message");
    Mockito.when(subscriptionRequestProvider.get(session)).thenThrow(exception);

    // THEN
    Assertions.assertThrows(
      exception.getClass(),
      // WHEN
      () -> sessionHandler.handle(session),
      // THEN
      exception.getMessage()
    );
  }

  @Nested
  @FieldDefaults(level = AccessLevel.PRIVATE)
  class SuccessfulHandling {

    @Mock
    WebSocketMessage webSocketMessage;

    @Captor
    ArgumentCaptor<Flux<WebSocketMessage>> sentMessagesCaptor;

    @Captor
    ArgumentCaptor<Predicate<? super WebSocketMessage>> filterCaptor;

    @Test
    void givenNoExceptionIsThrown_handle_subscribesForUpdatesFiltersThemAndDelegatesHandling() {
      // GIVEN
      val session = Mockito.mock(WebSocketSession.class);
      val bookUpdate = Mockito.mock(BookUpdate.class);
      mockSuccessfulProcessing(session, webSocketMessage, bookUpdate);

      // WHEN
      sessionHandler.handle(session).subscribe();

      // THEN
      verifySubscription(webSocketMessage);
      Assertions.assertEquals(messageFilter, getAppliedMessageFilter());
      Mockito.verify(messageHandler).handle(bookUpdate);
    }

    private void mockSuccessfulProcessing(
      final WebSocketSession session,
      final WebSocketMessage webSocketMessage,
      final BookUpdate bookUpdate
    ) {
      mockSubscription(session, webSocketMessage);
      mockHandling(session, webSocketMessage, bookUpdate);
    }

    private void mockSubscription(
      final WebSocketSession session,
      final WebSocketMessage webSocketMessage
    ) {
      Mockito
        .when(subscriptionRequestProvider.get(session))
        .thenReturn(webSocketMessage);

      final Mono<Void> subscriptionResponse = Mockito.spy(Mono.empty());
      Mockito
        .when(session.send(sentMessagesCaptor.capture()))
        .thenReturn(subscriptionResponse);
    }

    @SuppressWarnings("unchecked")
    private void mockHandling(
      final WebSocketSession session,
      final WebSocketMessage webSocketMessage,
      final BookUpdate bookUpdate
    ) {
      val unfilteredMessages = Mockito.mock(Flux.class);
      Mockito.when(session.receive()).thenReturn(unfilteredMessages);
      val filteredMessages = Flux.just(webSocketMessage);
      mockFiltering(unfilteredMessages, filteredMessages);
      mockMapping(bookUpdate, filteredMessages);
    }

    private void mockFiltering(
      final Flux<WebSocketMessage> unfilteredMessages,
      final Flux<WebSocketMessage> filteredMessages
    ) {
      Mockito
        .when(unfilteredMessages.filter(filterCaptor.capture()))
        .thenReturn(filteredMessages);
    }

    private void mockMapping(
      final BookUpdate bookUpdate,
      final Flux<WebSocketMessage> filteredMessages
    ) {
      val bookUpdates = Flux.just(bookUpdate);
      Mockito.when(messageMapper.map(filteredMessages)).thenReturn(bookUpdates);
    }

    private void verifySubscription(final WebSocketMessage webSocketMessage) {
      val sentMessages = sentMessagesCaptor.getValue().collectList().block();
      Assertions.assertNotNull(sentMessages);
      Assertions.assertEquals(1, sentMessages.size());
      Assertions.assertEquals(webSocketMessage, sentMessages.get(0));
    }

    private Object getAppliedMessageFilter() {
      val appliedPredicate = filterCaptor.getValue();
      val predicateDelegateField = appliedPredicate
        .getClass()
        .getDeclaredFields()[0];
      predicateDelegateField.setAccessible(true);
      return Try.of(() -> predicateDelegateField.get(appliedPredicate)).get();
    }
  }
}
