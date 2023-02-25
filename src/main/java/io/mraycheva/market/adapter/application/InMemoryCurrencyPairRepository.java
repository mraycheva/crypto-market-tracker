package io.mraycheva.market.adapter.application;

import io.mraycheva.market.common.CurrencyPair;
import io.mraycheva.market.common.CurrencyPairRepository;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

@RequiredArgsConstructor
@ConfigurationProperties(prefix = "currency")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InMemoryCurrencyPairRepository implements CurrencyPairRepository {

  List<CurrencyPair> pairs;

  @Override
  public List<CurrencyPair> getAll() {
    return pairs;
  }
}
