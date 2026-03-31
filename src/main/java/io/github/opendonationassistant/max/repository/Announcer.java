package io.github.opendonationassistant.max.repository;

public class Announcer {

  private AnnouncerData data;
  private final AnnouncerDataRepository dataRepository;

  public Announcer(AnnouncerData data, AnnouncerDataRepository dataRepository) {
    this.data = data;
    this.dataRepository = dataRepository;
  }

  public void toggle() {
    this.data = new AnnouncerData(
      data.id(),
      data.recipientId(),
      data.chatId(),
      data.text(),
      data.buttons(),
      !data.enabled()
    );
    save();
  }

  private void save() {
    dataRepository.update(data);
  }
}
