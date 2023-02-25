package io.mraycheva.market.adapter.kraken.order;

import io.mraycheva.market.order.OrderCollectionUpdate;
import io.mraycheva.market.order.OrderSide;
import io.vavr.control.Option;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class OrderUpdateExtractor {

  RawOrderUpdateExtractor rawExtractor;
  RawOrderUpdateMapper mapper;

  Map<OrderSide, OrderCollectionUpdate> get(final Object[] payloadElements) {
    return rawExtractor
        .getAll(payloadElements)
        .stream()
        .map(mapper::map)
        .filter(Option::isDefined)
        .map(Option::get)
        .collect(getMapCollector());
  }

  private Collector<OrderCollectionUpdate, ?, Map<OrderSide, OrderCollectionUpdate>> getMapCollector() {
    return Collectors.toMap(
        OrderCollectionUpdate::orderSide,
        Function.identity()
    );
  }
}
