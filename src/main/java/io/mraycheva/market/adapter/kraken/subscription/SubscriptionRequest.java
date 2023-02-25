package io.mraycheva.market.adapter.kraken.subscription;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mraycheva.market.common.CurrencyPair;
import io.mraycheva.market.adapter.kraken.RequestType;
import io.mraycheva.market.infrastructure.websocket.WebSocketSubscriptionRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;
import lombok.val;

@Value
@Getter(AccessLevel.PACKAGE)
class SubscriptionRequest implements WebSocketSubscriptionRequest {

  @JsonProperty("event")
  RequestType requestType = RequestType.SUBSCRIBE;

  @JsonProperty("subscription")
  SubscriptionChannel channel;

  @JsonProperty("pair")
  CurrencyPair[] currencyPairs;

  static SubscriptionRequest get(
    final SubscriptionChannelName channelName,
    final CurrencyPair[] currencyPairs,
    final int maxItemsCount
  ) {
    val channel = new SubscriptionChannel(channelName, maxItemsCount);
    return new SubscriptionRequest(channel, currencyPairs);
  }
}
