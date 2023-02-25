package io.mraycheva.market.adapter.application;

import io.mraycheva.market.order.BookUpdate;
import io.vavr.control.Option;

public interface BookUpdateParser {
  Option<BookUpdate> parse(String payload);
}
