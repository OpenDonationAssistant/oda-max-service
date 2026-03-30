package io.github.opendonationassistant.max.repository;

import com.fasterxml.uuid.Generators;
import io.github.opendonationassistant.commons.logging.ODALogger;
import io.github.opendonationassistant.integration.max.MaxApi;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.Map;
import java.util.Optional;

@Singleton
public class MaxAccountRepository {

  private final ODALogger log = new ODALogger(this);
  private final MaxAccountDataRepository dataRepository;
  private final MaxApi api;

  @Inject
  public MaxAccountRepository(
    MaxAccountDataRepository dataRepository,
    MaxApi api
  ) {
    this.dataRepository = dataRepository;
    this.api = api;
  }

  public MaxAccount create(String recipientId, String maxId) {
    var id = Generators.timeBasedEpochGenerator().generate().toString();
    MaxAccountData data = new MaxAccountData(id, recipientId, maxId);
    dataRepository.save(data);
    log.info(
      "Created max account",
      Map.of("recipientId", recipientId, "maxId", maxId)
    );
    return convert(data);
  }

  public Optional<MaxAccount> findByRecipientId(String recipientId) {
    return Optional.ofNullable(
      dataRepository.findByRecipientId(recipientId).getFirst()
    ).map(this::convert);
  }

  public Optional<MaxAccount> findByMaxId(String maxId) {
    return Optional.ofNullable(
      dataRepository.findByMaxId(maxId).getFirst()
    ).map(this::convert);
  }

  private MaxAccount convert(MaxAccountData data) {
    return new MaxAccount(data, api);
  }
}
