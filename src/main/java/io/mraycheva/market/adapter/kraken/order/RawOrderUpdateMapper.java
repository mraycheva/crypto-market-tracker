package io.mraycheva.market.adapter.kraken.order;

import com.fasterxml.jackson.core.type.TypeReference;
import io.mraycheva.market.order.OrderCollectionUpdate;
import io.mraycheva.market.order.OrderSide;
import io.mraycheva.market.order.OrderUpdate;
import io.mraycheva.market.infrastructure.mapping.BaseMapper;
import io.vavr.control.Option;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class RawOrderUpdateMapper {

  private static final TypeReference<List<List<String>>> ORDER_UPDATES_TYPE_REFERENCE = new TypeReference<>() {};

  BaseMapper baseMapper;

  Option<OrderCollectionUpdate> map(final RawOrderCollectionUpdate update) {
    return Option
      .of(update.orders())
      .map(this::getOrderUpdateList)
      .filter(Predicate.not(CollectionUtils::isEmpty))
      .map(Collections::unmodifiableList)
      .map(updates -> getCollectionUpdate(update.side(), updates));
  }

  private List<OrderUpdate> getOrderUpdateList(final Object updates) {
    return baseMapper
      .map(updates, ORDER_UPDATES_TYPE_REFERENCE)
      .stream()
      .map(this::getOrderUpdate)
      .toList();
  }

  private OrderCollectionUpdate getCollectionUpdate(
    final OrderSide orderSide,
    final List<OrderUpdate> updates
  ) {
    val timestamp = getLatestTimestamp(updates);
    return new OrderCollectionUpdate(timestamp, orderSide, updates);
  }

  private OrderUpdate getOrderUpdate(final List<String> updateParameters) {
    return OrderUpdate
      .builder()
      .price(getPrice(updateParameters))
      .volume(getVolume(updateParameters))
      .timestamp(getLatestTimestamp(updateParameters))
      .build();
  }

  private BigDecimal getPrice(final List<String> updateParameters) {
    return getBigDecimal(updateParameters, UpdateParameterIndex.PRICE);
  }

  private BigDecimal getVolume(final List<String> updateParameters) {
    return getBigDecimal(updateParameters, UpdateParameterIndex.VOLUME);
  }

  private Instant getLatestTimestamp(final List<String> updateParameters) {
    val epochSeconds = getBigDecimal(
      updateParameters,
      UpdateParameterIndex.TIMESTAMP
    )
      .longValue();
    return Instant.ofEpochSecond(epochSeconds);
  }

  private BigDecimal getBigDecimal(
    final List<String> updateParameters,
    final UpdateParameterIndex parameterIndex
  ) {
    val index = parameterIndex.getValue();
    val parameter = updateParameters.get(index);
    return new BigDecimal(parameter);
  }

  private Instant getLatestTimestamp(final Collection<OrderUpdate> orders) {
    return orders
      .stream()
      .map(OrderUpdate::timestamp)
      .max(Comparator.naturalOrder())
      .orElse(Instant.MIN);
  }
}
