package io.mraycheva.market.order;

import io.mraycheva.market.common.CurrencyPair;
import io.vavr.control.Option;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
@Getter(AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BookUpdate {

  @NonNull
  Instant timestamp;

  @NonNull
  CurrencyPair currencyPair;

  OrderCollectionUpdate asks;
  OrderCollectionUpdate bids;

  void applyToBook(final Book book) {
    book.apply(this);
  }

  public Option<OrderCollectionUpdate> getAsks() {
    return Option.of(asks);
  }

  public Option<OrderCollectionUpdate> getBids() {
    return Option.of(bids);
  }
}
