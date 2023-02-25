package io.mraycheva.market.adapter.kraken.subscription;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
enum SubscriptionChannelName {
  BOOK("book");

  @JsonValue
  String serialisationValue;
}
