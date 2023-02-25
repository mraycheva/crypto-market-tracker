package io.mraycheva.market.adapter.kraken.order;

import jakarta.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
class RawOrderUpdateExtractor {

  List<RawOrderCollectionUpdate> getAll(
    final @NonNull Object[] payloadElements
  ) {
    val allUpdates = getAllUpdates(payloadElements);
    return OrderUpdateSide
      .getAll()
      .map(side -> getRawCollection(allUpdates, side))
      .filter(RawOrderCollectionUpdate::isDefined)
      .collect(Collectors.toList());
  }

  private Map<String, Object> getAllUpdates(final Object[] payloadElements) {
    return Arrays
      .stream(payloadElements)
      .filter(Map.class::isInstance)
      .flatMap(this::getEntries)
      .filter(this::isUpdate)
      .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
  }

  @SuppressWarnings("unchecked")
  private Stream<Entry<String, Object>> getEntries(final Object map) {
    return ((Map<String, Object>) map).entrySet().stream();
  }

  private boolean isUpdate(final Entry<String, Object> entry) {
    return entry.getValue() instanceof List;
  }

  private RawOrderCollectionUpdate getRawCollection(
    final Map<String, Object> updates,
    final OrderUpdateSide updateSide
  ) {
    return new RawOrderCollectionUpdate(
      updateSide.getOrderSide(),
      filter(updates, updateSide)
    );
  }

  private @Nullable Object filter(
    final Map<String, Object> updates,
    final OrderUpdateSide side
  ) {
    val updatesByMainKey = updates.get(side.getUpdateKey());
    return Objects.nonNull(updatesByMainKey)
      ? updatesByMainKey
      : updates.get(side.getSnapshotKey());
  }
}
