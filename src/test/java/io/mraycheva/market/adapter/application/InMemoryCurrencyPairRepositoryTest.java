package io.mraycheva.market.adapter.application;

import io.mraycheva.market.common.CurrencyCode;
import io.mraycheva.market.common.CurrencyPair;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InMemoryCurrencyPairRepositoryTest {

  @Test
  void givenInputCurrencies_constructor_updatesProviderCurrencies() {
    // GIVEN
    val pairs = getPairs();

    // WHEN
    val adapter = new InMemoryCurrencyPairRepository(pairs);

    // THEN
    Assertions.assertEquals(pairs, adapter.getAll());
  }

  private List<CurrencyPair> getPairs() {
    return List.of(
      new CurrencyPair(CurrencyCode.ETH, CurrencyCode.USD),
      new CurrencyPair(CurrencyCode.XBT, CurrencyCode.USD)
    );
  }
}
