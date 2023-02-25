package io.mraycheva.market.adapter.kraken.order;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum UpdateParameterIndex {
  PRICE(0),
  VOLUME(1),
  TIMESTAMP(2);

  int value;
}
