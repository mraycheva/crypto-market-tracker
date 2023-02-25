package io.mraycheva.market.adapter.kraken.order;

import io.mraycheva.market.order.BookUpdate;
import io.mraycheva.market.order.OrderCollectionUpdate;
import io.mraycheva.market.order.OrderSide;
import io.mraycheva.market.adapter.application.BookUpdateParser;
import io.mraycheva.market.infrastructure.mapping.BaseMapper;
import io.vavr.control.Option;
import io.vavr.control.Try;
import java.time.Instant;
import java.util.Comparator;
import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class KrakenBookUpdateParser implements BookUpdateParser {

  BaseMapper baseMapper;
  CurrencyPairExtractor currencyPairExtractor;
  OrderUpdateExtractor orderUpdateExtractor;

  @Override
  public Option<BookUpdate> parse(final String payload) {
    return Try
      .of(() -> getPayloadElements(payload))
      .map(this::getBookUpdate)
      .map(Option::of)
      .onFailure(failure -> log(payload, failure))
      .recover(failure -> Option.none())
      .get();
  }

  private Object[] getPayloadElements(final String payload) {
    return baseMapper.map(payload, Object[].class);
  }

  private BookUpdate getBookUpdate(final Object[] payloadElements) {
    val currencyPair = currencyPairExtractor.get(payloadElements);
    val orderUpdates = orderUpdateExtractor.get(payloadElements);
    val timestamp = getLatestTimestamp(orderUpdates);
    return BookUpdate
      .builder()
      .currencyPair(currencyPair)
      .asks(orderUpdates.get(OrderSide.ASK))
      .bids(orderUpdates.get(OrderSide.BID))
      .timestamp(timestamp)
      .build();
  }

  private Instant getLatestTimestamp(
    final Map<OrderSide, OrderCollectionUpdate> orderUpdates
  ) {
    return orderUpdates
      .values()
      .stream()
      .map(OrderCollectionUpdate::timestamp)
      .max(Comparator.naturalOrder())
      .orElse(Instant.MIN);
  }

  private void log(final String payload, final Throwable failure) {
    log.error("Parsing failure for book update {}", payload, failure);
  }
}
