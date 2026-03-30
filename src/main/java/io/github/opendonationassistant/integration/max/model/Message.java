package io.github.opendonationassistant.integration.max.model;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record Message(MessageBody body) {
  @Serdeable
  public static record MessageBody(String mid, Integer seq, String text) {}
}
