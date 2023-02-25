package io.mraycheva.market.order;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record OrderUpdate(
  @NonNull BigDecimal price,
  @NonNull BigDecimal volume,
  @NonNull Instant timestamp
) {}
