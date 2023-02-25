package io.mraycheva.market.adapter.kraken.subscription;

import io.mraycheva.market.common.CurrencyCode;
import io.mraycheva.market.common.CurrencyPair;
import io.mraycheva.market.common.CurrencyPairRepository;
import io.mraycheva.market.adapter.kraken.RequestType;
import io.mraycheva.market.infrastructure.websocket.WebSocketSubscriptionRequest;
import java.util.List;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class SubscriptionRequestProviderTest {

  private static final int MAX_ORDERS_COUNT = 10;

  List<CurrencyCode> baseCurrencyCodes;

  @Mock
  CurrencyPairRepository currencyPairRepository;

  SubscriptionRequestProvider provider;

  @BeforeEach
  void setup() {
    baseCurrencyCodes = getBaseCurrencyCodes();
    provider =
      new SubscriptionRequestProvider(MAX_ORDERS_COUNT, currencyPairRepository);
  }

  private List<CurrencyCode> getBaseCurrencyCodes() {
    return List.of(CurrencyCode.XBT, CurrencyCode.ETH);
  }

  @Test
  void get_constructsSubscriptionRequest() {
    // GIVEN
    val pairs = getPairs();
    Mockito.when(currencyPairRepository.getAll()).thenReturn(pairs);

    // WHEN
    val request = provider.get();

    // THEN
    verifyRequest(pairs, request);
  }

  private List<CurrencyPair> getPairs() {
    return baseCurrencyCodes.stream().map(this::getPair).toList();
  }

  private CurrencyPair getPair(final CurrencyCode base) {
    return new CurrencyPair(base, CurrencyCode.USD);
  }

  private void verifyRequest(
    final List<CurrencyPair> pairs,
    final WebSocketSubscriptionRequest request
  ) {
    Assertions.assertTrue(request instanceof SubscriptionRequest);
    val krakenRequest = (SubscriptionRequest) request;

    Assertions.assertEquals(
      RequestType.SUBSCRIBE,
      krakenRequest.getRequestType()
    );
    Assertions.assertEquals(
      new SubscriptionChannel(SubscriptionChannelName.BOOK, MAX_ORDERS_COUNT),
      krakenRequest.getChannel()
    );
    val expectedPairs = pairs.toArray(CurrencyPair[]::new);
    val actualPairs = krakenRequest.getCurrencyPairs();
    Assertions.assertArrayEquals(expectedPairs, actualPairs);
  }
}
