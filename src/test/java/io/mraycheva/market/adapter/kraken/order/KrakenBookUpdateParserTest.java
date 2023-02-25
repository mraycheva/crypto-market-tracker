package io.mraycheva.market.adapter.kraken.order;

import io.mraycheva.market.common.CurrencyPair;
import io.mraycheva.market.order.BookUpdate;
import io.mraycheva.market.order.OrderCollectionUpdate;
import io.mraycheva.market.order.OrderSide;
import io.mraycheva.market.infrastructure.mapping.BaseMapper;
import java.util.Map;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import test.util.OrderCollectionUpdateProvider;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class KrakenBookUpdateParserTest {

  private static final String UPDATE_MESSAGE_PAYLOAD = "update";

  @Mock
  BaseMapper baseMapper;

  @Mock
  CurrencyPairExtractor currencyPairExtractor;

  @Mock
  OrderUpdateExtractor bookUpdateMapper;

  @InjectMocks
  KrakenBookUpdateParser bookUpdateParser;

  @Nested
  class Failure {

    @Test
    void givenBaseMapperThrowsException_parse_returnsNoUpdate() {
      // GIVEN
      mockBaseMapperFailure();

      // WHEN
      val update = bookUpdateParser.parse(UPDATE_MESSAGE_PAYLOAD);

      // THEN
      Assertions.assertTrue(update.isEmpty());
    }

    private void mockBaseMapperFailure() {
      val exceptionMessage = "exception message";
      Mockito
        .when(baseMapper.map((UPDATE_MESSAGE_PAYLOAD), Object[].class))
        .thenThrow(new RuntimeException(exceptionMessage));
    }
  }

  @Nested
  class Success {

    private static final String CURRENCY_PAIR_STRING = "ETH/USD";
    private static final CurrencyPair CURRENCY_PAIR = new CurrencyPair(
      CURRENCY_PAIR_STRING
    );

    private static final OrderCollectionUpdate BIDS_UPDATE = OrderCollectionUpdateProvider.getBidsUpdate();
    private static final OrderCollectionUpdate ASKS_UPDATE = OrderCollectionUpdateProvider.getAsksUpdate();

    @Test
    void givenNoExceptionIsThrown_parse_returnsBookUpdate() {
      // GIVEN
      mockDependencies();

      // WHEN
      val update = bookUpdateParser.parse(UPDATE_MESSAGE_PAYLOAD);

      // THEN
      Assertions.assertTrue(update.isDefined());
      Assertions.assertEquals(getExpectedUpdate(), update.get());
    }

    private void mockDependencies() {
      final Object[] payloadElements = {
        "560",
        "{<book_updates>}",
        "book-10",
        CURRENCY_PAIR_STRING,
      };
      Mockito
        .when(baseMapper.map(UPDATE_MESSAGE_PAYLOAD, Object[].class))
        .thenReturn(payloadElements);
      mockDelegate(payloadElements);
      Mockito
        .when(currencyPairExtractor.get(payloadElements))
        .thenReturn(CURRENCY_PAIR);
    }

    private void mockDelegate(final Object[] payloadElements) {
      val orderUpdates = Map.of(
        OrderSide.ASK,
        ASKS_UPDATE,
        OrderSide.BID,
        BIDS_UPDATE
      );
      Mockito
        .when(bookUpdateMapper.get(payloadElements))
        .thenReturn(orderUpdates);
    }

    private BookUpdate getExpectedUpdate() {
      return BookUpdate
        .builder()
        .asks(ASKS_UPDATE)
        .bids(BIDS_UPDATE)
        .currencyPair(CURRENCY_PAIR)
        .timestamp(ASKS_UPDATE.timestamp())
        .build();
    }
  }
}
