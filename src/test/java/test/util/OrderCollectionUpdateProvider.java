package test.util;

import com.fasterxml.jackson.core.type.TypeReference;
import io.mraycheva.market.adapter.kraken.order.UpdateParameterIndex;
import io.mraycheva.market.order.OrderCollectionUpdate;
import io.mraycheva.market.order.OrderSide;
import io.mraycheva.market.order.OrderUpdate;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OrderCollectionUpdateProvider {

  public static final List<List<String>> ASK_UPDATES = FileReader.read(
    UpdateFilePath.ASKS_STRIPPED,
    new TypeReference<>() {}
  );

  private static final List<List<String>> BID_UPDATES = FileReader.read(
    UpdateFilePath.BIDS_STRIPPED,
    new TypeReference<>() {}
  );

  public OrderCollectionUpdate getAsksUpdate() {
    return getOrderCollectionUpdate(getAskUpdates(), OrderSide.ASK);
  }

  public OrderCollectionUpdate getBidsUpdate() {
    return getOrderCollectionUpdate(
      getOrderUpdates(BID_UPDATES),
      OrderSide.BID
    );
  }

  private OrderCollectionUpdate getOrderCollectionUpdate(
    final List<OrderUpdate> orderUpdates,
    final OrderSide side
  ) {
    return new OrderCollectionUpdate(
      orderUpdates.get(0).timestamp(),
      side,
      orderUpdates
    );
  }

  public List<OrderUpdate> getAskUpdates() {
    return getOrderUpdates(ASK_UPDATES);
  }

  private static List<OrderUpdate> getOrderUpdates(
    final List<List<String>> rawOrderUpdates
  ) {
    return rawOrderUpdates
      .stream()
      .map(OrderCollectionUpdateProvider::getOrderUpdate)
      .toList();
  }

  private OrderUpdate getOrderUpdate(final List<String> update) {
    return getOrderUpdate(
      update.get(UpdateParameterIndex.PRICE.getValue()),
      update.get(UpdateParameterIndex.VOLUME.getValue()),
      update.get(UpdateParameterIndex.TIMESTAMP.getValue())
    );
  }

  private OrderUpdate getOrderUpdate(
    final String price,
    final String volume,
    final String timestamp
  ) {
    return OrderUpdate
      .builder()
      .price(TestUtil.getBigDecimal(price))
      .volume(TestUtil.getBigDecimal(volume))
      .timestamp(TestUtil.getTimestamp(timestamp))
      .build();
  }
}
