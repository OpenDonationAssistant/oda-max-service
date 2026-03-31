package io.github.opendonationassistant.max.repository;

import io.github.opendonationassistant.integration.max.MaxApi;

public class Announce {

  private final AnnounceData data;
  private final AnnounceDataRepository dataRepository;
  private final MaxApi api;

  public Announce(
    AnnounceData data,
    AnnounceDataRepository dataRepository,
    MaxApi api
  ) {
    this.data = data;
    this.dataRepository = dataRepository;
    this.api = api;
  }
}
