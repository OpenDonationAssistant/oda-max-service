package io.github.opendonationassistant.max.repository;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
@MappedEntity("accounts")
public record MaxAccountData(
  @Id String id,
  String recipientId,
  Integer maxId,
  boolean enabled
) {}
