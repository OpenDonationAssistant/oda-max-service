package io.github.opendonationassistant.max.repository;

import com.fasterxml.uuid.Generators;
import io.github.opendonationassistant.integration.max.MaxApi;
import jakarta.inject.Singleton;
import java.time.Instant;

@Singleton
public class AnnounceRepository {

  private final MaxApi api;
  private final AnnounceDataRepository dataRepository;

  public AnnounceRepository(MaxApi api, AnnounceDataRepository dataRepository) {
    this.api = api;
    this.dataRepository = dataRepository;
  }

  public void create(String mid, Long chatId) {
    var id = Generators.timeBasedEpochGenerator().generate().toString();
    var data = new AnnounceData(id, mid, chatId, Instant.now());
    dataRepository.save(data);
  }
}
