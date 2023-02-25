package io.mraycheva.market.adapter.kraken.order;

import io.mraycheva.market.order.OrderSide;
import io.mraycheva.market.infrastructure.mapping.BaseMapper;
import io.vavr.control.Option;
import java.util.List;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import test.util.OrderCollectionUpdateProvider;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class RawOrderUpdateMapperTest {

  @Mock
  BaseMapper baseMapper;

  @InjectMocks
  RawOrderUpdateMapper mapper;

  @Test
  void givenNullRawUpdates_map_returnsNoUpdate() {
    val update = new RawOrderCollectionUpdate(OrderSide.ASK, null);
    val map = mapper.map(update);
    Assertions.assertTrue(map.isEmpty());
  }

  @Test
  void givenNonNullRawUpdates_map_returnsOrderCollectionUpdate() {
    // GIVEN
    val rawAskUpdates = OrderCollectionUpdateProvider.ASK_UPDATES;
    mockMapping(rawAskUpdates);
    val update = new RawOrderCollectionUpdate(OrderSide.ASK, rawAskUpdates);

    // WHEN
    val mappedUpdate = mapper.map(update);

    // THEN
    val expectedUpdate = OrderCollectionUpdateProvider.getAsksUpdate();
    Assertions.assertEquals(Option.of(expectedUpdate), mappedUpdate);
  }

  private void mockMapping(final List<List<String>> rawAskUpdates) {
    Mockito
      .when(baseMapper.map(Mockito.eq(rawAskUpdates), Mockito.any()))
      .thenReturn(rawAskUpdates);
  }
}
