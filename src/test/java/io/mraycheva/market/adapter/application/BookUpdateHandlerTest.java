package io.mraycheva.market.adapter.application;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import io.mraycheva.market.order.Book;
import io.mraycheva.market.order.BookService;
import io.mraycheva.market.order.BookUpdate;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class BookUpdateHandlerTest {

  private static final String FORMATTED_BOOK = "formatted book";

  @Mock
  BookUpdate update;

  @Mock
  BookService service;

  @Mock
  BookFormatter bookFormatter;

  @InjectMocks
  BookUpdateHandler handler;

  @Test
  void givenBookCanBeUpdated_handle_updatesBookAndLogsState() {
    // GIVEN
    val logAppender = new ListAppender<ILoggingEvent>();
    prepareTest(logAppender);

    // WHEN
    handler.handle(update);

    // THEN
    verifyLogs(logAppender);
  }

  private void prepareTest(final ListAppender<ILoggingEvent> logAppender) {
    mockDependencies();
    spyLogs(logAppender);
  }

  private void mockDependencies() {
    val book = Mockito.mock(Book.class);
    Mockito.when(service.apply(update)).thenReturn(Option.of(book));
    Mockito
      .when(bookFormatter.format(book))
      .thenReturn(BookUpdateHandlerTest.FORMATTED_BOOK);
  }

  private void spyLogs(final ListAppender<ILoggingEvent> logAppender) {
    logAppender.start();
    val handlerLogger = (Logger) LoggerFactory.getLogger(
      BookUpdateHandler.class
    );
    handlerLogger.addAppender(logAppender);
  }

  private void verifyLogs(final ListAppender<ILoggingEvent> logAppender) {
    val logEvent = logAppender.list.get(0);
    Assertions.assertEquals(FORMATTED_BOOK, logEvent.getFormattedMessage());
    Assertions.assertEquals(Level.INFO, logEvent.getLevel());
  }

  @Test
  void givenBookCannotBeUpdated_handle_doesNotThrowException() {
    // GIVEN
    Mockito
      .doThrow(new RuntimeException("exceptionMessage"))
      .when(service)
      .apply(update);

    // WHEN & THEN
    Assertions.assertDoesNotThrow(() -> handler.handle(update));
    Mockito.verify(bookFormatter, Mockito.never()).format(Mockito.any());
  }
}
