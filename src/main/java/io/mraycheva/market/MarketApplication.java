package io.mraycheva.market;

import io.mraycheva.market.adapter.application.InMemoryCurrencyPairRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = { InMemoryCurrencyPairRepository.class })
public class MarketApplication {

  public static void main(final String[] args) {
    SpringApplication.run(MarketApplication.class, args);
  }
}
