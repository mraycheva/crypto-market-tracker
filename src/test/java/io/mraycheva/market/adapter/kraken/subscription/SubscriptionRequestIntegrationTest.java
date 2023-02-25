package io.mraycheva.market.adapter.kraken.subscription;

import io.mraycheva.market.common.CurrencyCode;
import io.mraycheva.market.common.CurrencyPair;
import java.io.IOException;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import test.util.FileReader;
import test.util.MessageFilePath;

@JsonTest
@FieldDefaults(level = AccessLevel.PRIVATE)
class SubscriptionRequestIntegrationTest {

  @Value("${kraken.order.count.max}")
  int maxItemsCount;

  @Autowired
  JacksonTester<SubscriptionRequest> tester;

  @Test
  void serialisation() throws IOException {
    // GIVEN
    val request = getRequest();
    // WHEN
    val serialisedRequest = tester.write(request).getJson();
    // THEN
    Assertions.assertEquals(
      FileReader.read(MessageFilePath.SUBSCRIPTION_REQUEST),
      serialisedRequest
    );
  }

  private SubscriptionRequest getRequest() {
    val channelName = SubscriptionChannelName.BOOK;
    val currencyPairs = getCurrencyPairs();
    return SubscriptionRequest.get(channelName, currencyPairs, maxItemsCount);
  }

  private CurrencyPair[] getCurrencyPairs() {
    return Stream
      .of(CurrencyCode.ETH, CurrencyCode.XBT)
      .map(base -> new CurrencyPair(base, CurrencyCode.USD))
      .toArray(CurrencyPair[]::new);
  }
}
