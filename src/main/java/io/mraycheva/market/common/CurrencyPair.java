package io.mraycheva.market.common;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.val;

@Value
@RequiredArgsConstructor
@Getter(AccessLevel.NONE)
public class CurrencyPair {

  private static final String CURRENCY_CODES_DELIMITER = "/";
  CurrencyCode base;
  CurrencyCode quote;

  public CurrencyPair(final String pairString) {
    val currencies = getCurrencies(pairString);
    this.base = currencies.get(0);
    this.quote = currencies.get(1);
  }

  private static List<CurrencyCode> getCurrencies(final String pairString) {
    return Arrays
      .stream(pairString.split(CURRENCY_CODES_DELIMITER))
      .map(CurrencyCode::valueOf)
      .toList();
  }

  @Override
  @JsonValue
  public String toString() {
    return String.format("%s%s%s", base, CURRENCY_CODES_DELIMITER, quote);
  }
}
