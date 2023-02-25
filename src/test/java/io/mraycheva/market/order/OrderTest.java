package io.mraycheva.market.order;

import java.math.BigDecimal;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderTest {

  @Test
  void givenVolumeIsZero_hasVolume_returnsFalse() {
    // GIVEN
    val order = Order
      .builder()
      .price(BigDecimal.TEN)
      .volume(BigDecimal.ZERO)
      .build();
    // WHEN
    val hasVolume = order.hasVolume();
    // THEN
    Assertions.assertFalse(hasVolume);
  }

  @Test
  void givenVolumeIsAPositiveNumber_hasVolume_returnsTrue() {
    // GIVEN
    val order = Order
      .builder()
      .price(BigDecimal.TEN)
      .volume(BigDecimal.TWO)
      .build();
    // WHEN
    val hasVolume = order.hasVolume();
    // THEN
    Assertions.assertTrue(hasVolume);
  }
}
