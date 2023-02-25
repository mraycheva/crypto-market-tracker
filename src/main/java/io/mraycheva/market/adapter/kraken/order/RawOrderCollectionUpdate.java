package io.mraycheva.market.adapter.kraken.order;

import io.mraycheva.market.order.OrderSide;
import java.util.Objects;
import lombok.NonNull;

record RawOrderCollectionUpdate(@NonNull OrderSide side, Object orders) {
  boolean isDefined() {
    return Objects.nonNull(orders);
  }
}
