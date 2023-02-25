package io.mraycheva.market.order;

import io.mraycheva.market.common.CurrencyPair;
import io.vavr.control.Option;

public interface BookRepository {
  Option<Book> getBook(CurrencyPair currencyPair);
}
