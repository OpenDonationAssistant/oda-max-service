package io.github.opendonationassistant.max.repository;

import io.github.opendonationassistant.integration.max.MaxApi;
import jakarta.inject.Singleton;

@Singleton
public class AnnounceRepository {

  private final MaxApi api;
  private final AnnounceDataRepository dataRepository;

  public AnnounceRepository(MaxApi api, AnnounceDataRepository dataRepository) {
    this.api = api;
    this.dataRepository = dataRepository;
  }
}
