package io.github.opendonationassistant.max.repository;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;
import java.time.Instant;

@Serdeable
@MappedEntity("announces")
public record AnnounceData(
  @Id String id,
  String mid,
  Long chatId,
  Instant time
) {}
