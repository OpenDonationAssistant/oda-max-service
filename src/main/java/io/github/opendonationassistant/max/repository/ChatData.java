package io.github.opendonationassistant.max.repository;

import org.jspecify.annotations.Nullable;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
@MappedEntity("chats")
public record ChatData(@Id Long id, String title, @Nullable Long ownerId) {}
