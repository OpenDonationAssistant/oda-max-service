package io.github.opendonationassistant.max.repository;

import com.fasterxml.uuid.Generators;
import io.github.opendonationassistant.max.repository.AnnouncerData.Button;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.List;

@Singleton
public class AnnouncerRepository {

  private final AnnouncerDataRepository dataRepository;

  @Inject
  public AnnouncerRepository(AnnouncerDataRepository dataRepository) {
    this.dataRepository = dataRepository;
  }

  public Announcer create(
    String recipientId,
    Integer chatId,
    String text,
    List<Button> buttons
  ) {
    var id = Generators.timeBasedEpochGenerator().generate().toString();
    var data = new AnnouncerData(id, recipientId, chatId, text, buttons, true);
    dataRepository.save(data);
    return new Announcer(data, dataRepository);
  }

  public List<Announcer> findByRecipientId(String recipientId) {
    return dataRepository
      .findByRecipientId(recipientId)
      .stream()
      .map(data -> new Announcer(data, dataRepository))
      .toList();
  }
}
