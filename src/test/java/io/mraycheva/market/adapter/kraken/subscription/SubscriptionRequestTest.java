package io.mraycheva.market.adapter.kraken.subscription;

import io.mraycheva.market.common.CurrencyCode;
import io.mraycheva.market.common.CurrencyPair;
import io.mraycheva.market.adapter.kraken.RequestType;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SubscriptionRequestTest {

  private static final int MAX_ITEMS_COUNT = 10;

  @Test
  void givenInputParameters_get_initialisesRequestWithCorrectValues() {
    // GIVEN
    val channelName = SubscriptionChannelName.BOOK;
    val currencyPairs = getCurrencyPairs();

    // WHEN
    val request = SubscriptionRequest.get(
      channelName,
      currencyPairs,
      MAX_ITEMS_COUNT
    );

    // THEN
    verifyRequest(channelName, currencyPairs, request);
  }

  private CurrencyPair[] getCurrencyPairs() {
    return new CurrencyPair[] { getKrakenCurrencyPair() };
  }

  private CurrencyPair getKrakenCurrencyPair() {
    return new CurrencyPair(CurrencyCode.ETH, CurrencyCode.USD);
  }

  private void verifyRequest(
    final SubscriptionChannelName inputChannelName,
    final CurrencyPair[] inputCurrencyPairs,
    final SubscriptionRequest request
  ) {
    Assertions.assertEquals(RequestType.SUBSCRIBE, request.getRequestType());
    Assertions.assertEquals(
      getExpectedChannel(inputChannelName),
      request.getChannel()
    );
    Assertions.assertEquals(inputCurrencyPairs, request.getCurrencyPairs());
  }

  private SubscriptionChannel getExpectedChannel(
    final SubscriptionChannelName channel
  ) {
    return new SubscriptionChannel(channel, MAX_ITEMS_COUNT);
  }
}
