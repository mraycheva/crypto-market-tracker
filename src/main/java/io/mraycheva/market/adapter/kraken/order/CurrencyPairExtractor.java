package io.mraycheva.market.adapter.kraken.order;

import io.mraycheva.market.common.CurrencyPair;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class CurrencyPairExtractor {

  CurrencyPair get(final Object[] payloadElements) {
    val currencyPairIndex = payloadElements.length - 1;
    val currencies = (String) (payloadElements[currencyPairIndex]);
    return new CurrencyPair(currencies);
  }
}
