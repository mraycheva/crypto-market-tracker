package test.util;

import io.mraycheva.market.order.OrderCollection;
import io.mraycheva.market.order.OrderCollectionUpdate;
import io.mraycheva.market.order.OrderSide;
import java.time.Instant;
import lombok.experimental.UtilityClass;
import lombok.val;

@UtilityClass
public class OrderCollectionProvider {

  public OrderCollection getOrderCollection(final OrderSide orderSide) {
    val orderCollection = new OrderCollection(orderSide);
    addOrders(orderCollection, orderSide);
    return orderCollection;
  }

  private void addOrders(
    final OrderCollection orders,
    final OrderSide orderSide
  ) {
    val collectionUpdate = getCollectionUpdate(orderSide);
    orders.apply(collectionUpdate);
  }

  private OrderCollectionUpdate getCollectionUpdate(final OrderSide orderSide) {
    val orderUpdates = OrderCollectionUpdateProvider.getAskUpdates();
    return new OrderCollectionUpdate(Instant.now(), orderSide, orderUpdates);
  }
}
