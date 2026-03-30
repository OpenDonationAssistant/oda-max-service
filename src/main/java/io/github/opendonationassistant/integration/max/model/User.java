package io.github.opendonationassistant.integration.max.model;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record User(Integer userId){}
