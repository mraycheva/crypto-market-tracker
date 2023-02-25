package io.mraycheva.market.adapter.kraken.order;

import io.mraycheva.market.order.OrderCollectionUpdate;
import io.mraycheva.market.order.OrderSide;
import io.vavr.control.Option;
import java.time.Instant;
import java.util.List;
import java.util.Map;
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

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class OrderUpdateExtractorTest {

  @Mock
  RawOrderUpdateExtractor extractor;

  @Mock
  RawOrderUpdateMapper delegate;

  @InjectMocks
  OrderUpdateExtractor delegateMapper;

  @Test
  void givenPayloadElementsInput_get_returnsOrderCollectionUpdates() {
    // GIVEN
    final Object[] payloadElements = { "1", "2", "3" };
    val updates = getMappedUpdates();
    mockDependencies(payloadElements, updates);

    // WHEN
    val result = delegateMapper.get(payloadElements);

    // THEN
    Assertions.assertEquals(updates, result);
  }

  private Map<OrderSide, OrderCollectionUpdate> getMappedUpdates() {
    val askUpdate = getCollectionUpdate(OrderSide.ASK);
    val bidUpdate = getCollectionUpdate(OrderSide.BID);
    return Map.of(OrderSide.ASK, askUpdate, OrderSide.BID, bidUpdate);
  }

  private OrderCollectionUpdate getCollectionUpdate(final OrderSide ask) {
    return new OrderCollectionUpdate(Instant.now(), ask, List.of());
  }

  private void mockDependencies(
    final Object[] payloadElements,
    final Map<OrderSide, OrderCollectionUpdate> mappedUpdates
  ) {
    val rawUpdates = getRaw();
    Mockito.when(extractor.getAll(payloadElements)).thenReturn(rawUpdates);
    rawUpdates.forEach(update -> mockUpdateMapping(mappedUpdates, update));
  }

  private List<RawOrderCollectionUpdate> getRaw() {
    return List.of(
      new RawOrderCollectionUpdate(OrderSide.ASK, getRawAskUpdates()),
      new RawOrderCollectionUpdate(OrderSide.BID, getRawBidUpdates())
    );
  }

  private List<List<String>> getRawAskUpdates() {
    return List.of(List.of("asks"));
  }

  public List<List<String>> getRawBidUpdates() {
    return List.of();
  }

  private void mockUpdateMapping(
    final Map<OrderSide, OrderCollectionUpdate> mappedUpdates,
    final RawOrderCollectionUpdate rawUpdate
  ) {
    val mappedUpdate = mappedUpdates.get(rawUpdate.side());
    Mockito.when(delegate.map(rawUpdate)).thenReturn(Option.of(mappedUpdate));
  }
}
