package io.mraycheva.market.order;

import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
@EqualsAndHashCode(of = "price")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Order implements Comparable<Order> {

  @NonNull
  BigDecimal price;

  @NonNull
  BigDecimal volume;

  public static Order get(final OrderUpdate update) {
    return Order
      .builder()
      .price(update.price())
      .volume(update.volume())
      .build();
  }

  boolean hasVolume() {
    return volume.compareTo(BigDecimal.ZERO) > 0;
  }

  @Override
  public int compareTo(final Order order) {
    return price.compareTo(order.price);
  }
}
