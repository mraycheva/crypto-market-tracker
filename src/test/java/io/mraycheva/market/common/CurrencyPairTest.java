package io.mraycheva.market.common;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CurrencyPairTest {

  @ParameterizedTest
  @ValueSource(strings = { "ETH/USD", "XBT/USD" })
  void givenInputStrings_constructor_returnsCurrencyPair(
    final String pairString
  ) {
    // WHEN
    val currencyPair = new CurrencyPair(pairString);
    // THEN
    Assertions.assertNotNull(currencyPair);
  }

  @Test
  void toString_returnsCorrectRepresentation() {
    // GIVEN
    val pair = getCurrencyPair();

    // WHEN
    val pairString = pair.toString();

    // THEN
    Assertions.assertEquals("XBT/USD", pairString);
  }

  private CurrencyPair getCurrencyPair() {
    val baseCurrencyCode = CurrencyCode.XBT;
    val quoteCurrencyCode = CurrencyCode.USD;
    return new CurrencyPair(baseCurrencyCode, quoteCurrencyCode);
  }
}
