package io.mraycheva.market.common;

import java.util.List;

public interface CurrencyPairRepository {
  List<CurrencyPair> getAll();
}
