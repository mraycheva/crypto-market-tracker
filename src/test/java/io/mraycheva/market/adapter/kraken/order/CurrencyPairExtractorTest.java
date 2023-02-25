package io.mraycheva.market.adapter.kraken.order;

import io.mraycheva.market.common.CurrencyCode;
import io.mraycheva.market.common.CurrencyPair;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@FieldDefaults(level = AccessLevel.PRIVATE)
class CurrencyPairExtractorTest {

  private static final CurrencyCode BASE_CURRENCY_CODE = CurrencyCode.XBT;
  private static final CurrencyCode QUOTE_CURRENCY_CODE = CurrencyCode.USD;

  CurrencyPairExtractor mapper;

  @BeforeEach
  void setUp() {
    mapper = new CurrencyPairExtractor();
  }

  @Test
  void givenPayloadElementsInput_get_returnsCurrencyPair() {
    // GIVEN
    final Object[] payloadElements = getPayloadElements();
    // WHEN
    val actualPair = mapper.get(payloadElements);
    // THEN
    val expectedPair = new CurrencyPair(
      BASE_CURRENCY_CODE,
      QUOTE_CURRENCY_CODE
    );
    Assertions.assertEquals(expectedPair, actualPair);
  }

  private Object[] getPayloadElements() {
    val currencyPairString = String.format(
      "%s/%s",
      CurrencyPairExtractorTest.BASE_CURRENCY_CODE,
      CurrencyPairExtractorTest.QUOTE_CURRENCY_CODE
    );
    return new Object[] { "0", "1", "2", currencyPairString };
  }
}
