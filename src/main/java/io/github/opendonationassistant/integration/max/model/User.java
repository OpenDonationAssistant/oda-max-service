package io.github.opendonationassistant.integration.max.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record User(@JsonProperty("user_id") Long userId) {}
