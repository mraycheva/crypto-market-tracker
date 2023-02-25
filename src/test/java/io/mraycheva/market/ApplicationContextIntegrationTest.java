package io.mraycheva.market;

import io.mraycheva.market.infrastructure.websocket.SessionConfiguration;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;

@SpringBootTest
@FieldDefaults(level = AccessLevel.PRIVATE)
class ApplicationContextIntegrationTest {

  @Autowired
  ApplicationContext applicationContext;

  @MockBean
  SessionConfiguration sessionConfiguration;

  @Test
  void test() {
    Assertions.assertNotNull(applicationContext);
  }
}
