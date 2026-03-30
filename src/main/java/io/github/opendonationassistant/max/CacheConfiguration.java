package io.github.opendonationassistant.max;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Factory
public class CacheConfiguration {

  @Bean
  @Singleton
  public Map<String, String> linkCodes() {
    return new HashMap<>();
  }
}
