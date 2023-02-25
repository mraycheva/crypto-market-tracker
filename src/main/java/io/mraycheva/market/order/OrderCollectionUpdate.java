package io.mraycheva.market.order;

import java.time.Instant;
import java.util.Collection;
import lombok.NonNull;

public record OrderCollectionUpdate(
  @NonNull Instant timestamp,
  @NonNull OrderSide orderSide,
  @NonNull Collection<OrderUpdate> orders
) {}
