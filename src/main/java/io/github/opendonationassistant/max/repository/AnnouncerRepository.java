package io.github.opendonationassistant.max.repository;

import com.fasterxml.uuid.Generators;
import io.github.opendonationassistant.integration.max.MaxApi;
import io.github.opendonationassistant.max.repository.AnnouncerData.Button;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.List;
import java.util.Optional;

@Singleton
public class AnnouncerRepository {

  private final AnnouncerDataRepository dataRepository;
  private final AnnounceRepository announceRepository;
  private final MaxApi api;

  @Inject
  public AnnouncerRepository(
    AnnouncerDataRepository dataRepository,
    AnnounceRepository announceRepository,
    MaxApi api
  ) {
    this.dataRepository = dataRepository;
    this.announceRepository = announceRepository;
    this.api = api;
  }

  public Announcer create(
    String recipientId,
    Long chatId,
    String text,
    List<Button> buttons
  ) {
    var id = Generators.timeBasedEpochGenerator().generate().toString();
    var data = new AnnouncerData(id, recipientId, chatId, text, buttons, true);
    dataRepository.save(data);
    return new Announcer(data, dataRepository, announceRepository, api);
  }

  public List<Announcer> findByRecipientId(String recipientId) {
    return dataRepository
      .findByRecipientId(recipientId)
      .stream()
      .map(data -> new Announcer(data, dataRepository, announceRepository, api))
      .toList();
  }

  public Optional<Announcer> findById(String id) {
    var data = dataRepository.findById(id).orElse(null);
    if (data == null) {
      return Optional.empty();
    }
    return Optional.of(new Announcer(data, dataRepository, announceRepository, api));
  }
}
