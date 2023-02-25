package io.mraycheva.market.order;

import io.mraycheva.market.common.CurrencyCode;
import io.mraycheva.market.common.CurrencyPair;
import io.vavr.control.Option;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import test.util.OrderCollectionUpdateProvider;

@FieldDefaults(level = AccessLevel.PRIVATE)
class BookTest {

  Book book;
  CurrencyPair currencyPair;

  @BeforeEach
  void setup() {
    currencyPair = new CurrencyPair(CurrencyCode.ETH, CurrencyCode.USD);
    book = new Book(currencyPair);
    spyOrders();
  }

  private void spyOrders() {
    spyOrders("asks");
    spyOrders("bids");
  }

  private void spyOrders(final String fieldName) {
    val field = ReflectionTestUtils.getField(book, fieldName);
    val fieldSpy = Mockito.spy(field);
    ReflectionTestUtils.setField(book, fieldName, fieldSpy);
  }

  @Test
  void givenBookUpdate_apply_updatesCollection() {
    // GIVEN
    val bookUpdate = getBookUpdate();

    // WHEN
    book.apply(bookUpdate);

    // THEN
    verifyBookUpdate(bookUpdate);
  }

  private BookUpdate getBookUpdate() {
    return BookUpdate
      .builder()
      .asks(OrderCollectionUpdateProvider.getAsksUpdate())
      .bids(OrderCollectionUpdateProvider.getBidsUpdate())
      .currencyPair(currencyPair)
      .timestamp(Instant.now())
      .build();
  }

  private void verifyBookUpdate(final BookUpdate update) {
    Assertions.assertEquals(update.getTimestamp(), book.getLastUpdate());
    verifyOrdersUpdate(update.getAsks(), book.getAsks());
    verifyOrdersUpdate(update.getBids(), book.getBids());
  }

  private void verifyOrdersUpdate(
    final Option<OrderCollectionUpdate> updateOption,
    final OrderCollection bookOrders
  ) {
    updateOption
      .peek(update -> Mockito.verify(bookOrders).apply(update))
      .onEmpty(() -> verifyUpdateNotAttempted(bookOrders));
  }

  private void verifyUpdateNotAttempted(final OrderCollection orders) {
    Mockito
      .verify(orders, Mockito.never())
      .apply(Mockito.any(OrderCollectionUpdate.class));
  }
}
