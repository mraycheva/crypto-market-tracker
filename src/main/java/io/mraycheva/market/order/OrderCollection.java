package io.mraycheva.market.order;

import io.vavr.control.Option;
import io.vavr.control.Try;
import java.util.Collections;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Supplier;
import lombok.NonNull;
import lombok.Value;

@Value
public class OrderCollection {

  SortedSet<Order> orders;
  Supplier<Order> bestOrderSupplier;

  public OrderCollection(@NonNull final OrderSide side) {
    orders = new TreeSet<>(Comparator.reverseOrder());
    bestOrderSupplier =
      () -> OrderSide.BID == side ? orders.first() : orders.last();
  }

  public void apply(final OrderCollectionUpdate update) {
    update.orders().stream().map(Order::get).forEach(this::merge);
  }

  private void merge(final Order updatedOrder) {
    Option
      .of(updatedOrder)
      .peek(orders::remove)
      .filter(Order::hasVolume)
      .peek(orders::add);
  }

  public Option<Order> getBest() {
    return Try
      .of(bestOrderSupplier::get)
      .map(Option::of)
      .recover(failure -> Option.none())
      .get();
  }

  public SortedSet<Order> getOrders() {
    return Collections.unmodifiableSortedSet(orders);
  }
}
