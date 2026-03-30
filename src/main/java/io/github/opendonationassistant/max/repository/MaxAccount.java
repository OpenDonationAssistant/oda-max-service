package io.github.opendonationassistant.max.repository;

import io.github.opendonationassistant.integration.max.MaxApi;

public class MaxAccount {

  private final MaxAccountData data;
  private final MaxApi api;

  public MaxAccount(MaxAccountData data, MaxApi api) {
    this.data = data;
    this.api = api;
  }


}
