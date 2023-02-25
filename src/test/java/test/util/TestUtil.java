package test.util;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.experimental.UtilityClass;
import lombok.val;

@UtilityClass
public class TestUtil {

  public Instant getTimestamp(final String timestampString) {
    val epochSeconds = getBigDecimal(timestampString).longValue();
    return Instant.ofEpochSecond(epochSeconds);
  }

  public BigDecimal getBigDecimal(final String string) {
    return new BigDecimal(string);
  }
}
