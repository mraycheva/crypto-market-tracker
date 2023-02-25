package io.mraycheva.market.adapter.kraken.subscription;

import com.fasterxml.jackson.annotation.JsonProperty;

record SubscriptionChannel(
  SubscriptionChannelName name,
  @JsonProperty("depth") int maxItemsCount
) {}
