package io.mraycheva.market.adapter.kraken.order;

import io.mraycheva.market.order.OrderSide;
import java.util.Arrays;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter(AccessLevel.PACKAGE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
enum OrderUpdateSide {
  ASK("a", "as", OrderSide.ASK),
  BID("b", "bs", OrderSide.BID);

  String updateKey;
  String snapshotKey;
  OrderSide orderSide;

  static Stream<OrderUpdateSide> getAll() {
    return Arrays.stream(OrderUpdateSide.values());
  }
}
