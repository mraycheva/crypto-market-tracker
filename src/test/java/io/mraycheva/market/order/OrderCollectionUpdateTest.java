package io.mraycheva.market.order;

import java.time.Instant;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderCollectionUpdateTest {

  @Test
  void givenNullOrderType_constructor_fails() {
    // GIVEN
    val timestamp = Instant.now();
    // THEN
    final List<OrderUpdate> updatedOrders = List.of();
    Assertions.assertThrows(
      IllegalArgumentException.class,
      // WHEN
      () -> new OrderCollectionUpdate(timestamp, null, updatedOrders)
    );
  }

  @Test
  void givenNullUpdatedOrders_constructor_fails() {
    // GIVEN
    val timestamp = Instant.now();
    // THEN
    Assertions.assertThrows(
      IllegalArgumentException.class,
      // WHEN
      () -> new OrderCollectionUpdate(timestamp, OrderSide.ASK, null)
    );
  }

  @Test
  void givenNonNullParameters_constructor_doesNotThrowException() {
    // GIVEN
    val timestamp = Instant.now();
    // WHEN & THEN
    Assertions.assertDoesNotThrow(() ->
      new OrderCollectionUpdate(timestamp, OrderSide.ASK, List.of())
    );
  }
}
