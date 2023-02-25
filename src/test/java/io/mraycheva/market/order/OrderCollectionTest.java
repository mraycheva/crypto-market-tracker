package io.mraycheva.market.order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.util.CollectionUtils;
import test.util.OrderCollectionProvider;
import test.util.OrderCollectionUpdateProvider;

class OrderCollectionTest {

  @Test
  void givenNullOrderType_creation_fails() {
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> new OrderCollection(null)
    );
  }

  @Nested
  @FieldDefaults(level = AccessLevel.PRIVATE)
  class Apply {

    OrderCollection collection;

    @BeforeEach
    void setUp() {
      collection = new OrderCollection(OrderSide.ASK);
    }

    @Nested
    class WithNoPreviousRecordForPrice {

      @Test
      void givenZeroVolumeUpdate_apply_doesNotStoreOrder() {
        testZeroVolumeUpdate();
      }

      @Test
      void givenPositiveVolumeUpdate_apply_storesOrder() {
        testPositiveVolumeUpdate(BigDecimal.TWO);
      }
    }

    @Nested
    class WithPreviousRecordForPrice {

      private static final BigDecimal INITIAL_PRICE_VOLUME = BigDecimal.TWO;

      @BeforeEach
      void setup() {
        val orderUpdate = getOrderUpdate(INITIAL_PRICE_VOLUME);
        val collectionUpdate = getCollectionUpdate(orderUpdate);
        collection.apply(collectionUpdate);
      }

      @Test
      void givenZeroVolumeUpdate_apply_removesOrderRecord() {
        testZeroVolumeUpdate();
      }

      @Test
      void givenNewPositiveVolumeUpdate_apply_updatesStoredVolume() {
        val changedVolume = INITIAL_PRICE_VOLUME.add(BigDecimal.TWO);
        testPositiveVolumeUpdate(changedVolume);
      }
    }

    private OrderUpdate getOrderUpdate(final BigDecimal volume) {
      return OrderUpdate
          .builder()
          .volume(volume)
          .price(BigDecimal.TEN)
          .timestamp(Instant.now())
          .build();
    }

    private OrderCollectionUpdate getCollectionUpdate(
        final OrderUpdate orderUpdate
    ) {
      return new OrderCollectionUpdate(
          Instant.now(),
          OrderSide.ASK,
          List.of(orderUpdate)
      );
    }

    private void testZeroVolumeUpdate() {
      // GIVEN
      val orderUpdate = getOrderUpdate(BigDecimal.ZERO);
      // WHEN
      collection.apply(getCollectionUpdate(orderUpdate));
      // THEN
      Assertions.assertTrue(CollectionUtils.isEmpty(collection.getOrders()));
    }

    private void testPositiveVolumeUpdate(final BigDecimal volume) {
      // GIVEN
      val orderUpdate = getOrderUpdate(volume);

      // WHEN
      collection.apply(getCollectionUpdate(orderUpdate));

      // THEN
      val askOrders = collection.getOrders();
      Assertions.assertEquals(1, askOrders.size());
      val order = askOrders.first();
      Assertions.assertEquals(Order.get(orderUpdate), order);
    }
  }

  @Nested
  class GetBest {

    @ParameterizedTest
    @EnumSource(OrderSide.class)
    void givenCollectionType_getBest_returnsBestOrder(
        final OrderSide orderSide
    ) {
      // GIVEN
      val collection = OrderCollectionProvider.getOrderCollection(orderSide);
      // WHEN
      val bestOrder = collection.getBest();
      // THEN
      Assertions.assertTrue(bestOrder.isDefined());
      Assertions.assertEquals(getExpectedBestOrder(orderSide), bestOrder.get());
    }

    public Order getExpectedBestOrder(final OrderSide orderSide) {
      val bestToWorstPriceComparator = getBestToWorstPriceComparator(orderSide);
      return OrderCollectionUpdateProvider
          .getAskUpdates()
          .stream()
          .map(Order::get)
          .min(bestToWorstPriceComparator)
          .orElseThrow();
    }

    private Comparator<? super Order> getBestToWorstPriceComparator(
        final OrderSide orderSide
    ) {
      return OrderSide.ASK == orderSide
          ? Comparator.naturalOrder()
          : Comparator.reverseOrder();
    }
  }
}
