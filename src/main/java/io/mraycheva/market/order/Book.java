package io.mraycheva.market.order;

import io.mraycheva.market.common.CurrencyPair;
import java.time.Instant;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@RequiredArgsConstructor
@EqualsAndHashCode(of = "currencyPair")
public class Book {

  OrderCollection asks = new OrderCollection(OrderSide.ASK);
  OrderCollection bids = new OrderCollection(OrderSide.BID);

  CurrencyPair currencyPair;

  @NonFinal
  Instant lastUpdate;

  public void apply(final BookUpdate update) {
    update.getAsks().peek(askUpdates -> apply(askUpdates, asks));
    update.getBids().peek(bidUpdates -> apply(bidUpdates, bids));
    lastUpdate = update.getTimestamp();
  }

  private void apply(
    final OrderCollectionUpdate updates,
    final OrderCollection orders
  ) {
    orders.apply(updates);
  }
}
