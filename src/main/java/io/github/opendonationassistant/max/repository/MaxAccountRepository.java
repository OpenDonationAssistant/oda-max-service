package io.github.opendonationassistant.max.repository;

import com.fasterxml.uuid.Generators;
import io.github.opendonationassistant.commons.logging.ODALogger;
import io.github.opendonationassistant.integration.max.MaxApi;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Singleton
public class MaxAccountRepository {

  private final ODALogger log = new ODALogger(this);
  private final MaxAccountDataRepository dataRepository;
  private final ChatRepository chatRepository;
  private final MaxApi api;

  @Inject
  public MaxAccountRepository(
    MaxAccountDataRepository dataRepository,
    ChatRepository chatRepository,
    MaxApi api
  ) {
    this.dataRepository = dataRepository;
    this.chatRepository = chatRepository;
    this.api = api;
  }

  public MaxAccount create(String recipientId, Long maxId) {
    var id = Generators.timeBasedEpochGenerator().generate().toString();
    MaxAccountData data = new MaxAccountData(id, recipientId, maxId, true);
    dataRepository.save(data);
    log.info(
      "Created max account",
      Map.of("recipientId", recipientId, "maxId", maxId)
    );
    return convert(data);
  }

  public List<MaxAccount> findByRecipientId(String recipientId) {
    return dataRepository
      .findByRecipientId(recipientId)
      .stream()
      .map(this::convert)
      .toList();
  }

  public Optional<MaxAccount> findByMaxId(Long maxId) {
    return dataRepository
      .findByMaxId(maxId)
      .stream()
      .findFirst()
      .map(this::convert);
  }

  public Optional<MaxAccount> findById(String id) {
    return dataRepository.findById(id).map(this::convert);
  }

  private MaxAccount convert(MaxAccountData data) {
    return new MaxAccount(data, api, chatRepository, dataRepository);
  }
}
