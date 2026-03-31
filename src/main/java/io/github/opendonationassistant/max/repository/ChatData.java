package io.github.opendonationassistant.max.repository;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
@MappedEntity("chats")
public record ChatData(@Id Integer id, String title, String ownerId) {}
