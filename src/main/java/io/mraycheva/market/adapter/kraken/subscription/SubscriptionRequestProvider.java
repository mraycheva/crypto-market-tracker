package io.mraycheva.market.adapter.kraken.subscription;

import io.mraycheva.market.common.CurrencyPair;
import io.mraycheva.market.common.CurrencyPairRepository;
import io.mraycheva.market.infrastructure.websocket.WebSocketSubscriptionRequest;
import java.util.function.Supplier;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("krakenSubscriptionRequestProvider")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class SubscriptionRequestProvider
  implements Supplier<WebSocketSubscriptionRequest> {

  @Value("${kraken.order.count.max}")
  int maxOrdersCount;

  CurrencyPairRepository currencyPairRepository;

  @Override
  public WebSocketSubscriptionRequest get() {
    return SubscriptionRequest.get(
      SubscriptionChannelName.BOOK,
      getCurrencyPairs(),
      maxOrdersCount
    );
  }

  private CurrencyPair[] getCurrencyPairs() {
    return currencyPairRepository.getAll().toArray(CurrencyPair[]::new);
  }
}
