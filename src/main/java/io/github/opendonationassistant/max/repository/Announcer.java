package io.github.opendonationassistant.max.repository;

import io.github.opendonationassistant.integration.max.MaxApi;
import io.github.opendonationassistant.integration.max.MaxApi.MessageRequest;

public class Announcer {

  private AnnouncerData data;
  private final AnnouncerDataRepository dataRepository;
  private final MaxApi api;

  public Announcer(
    AnnouncerData data,
    AnnouncerDataRepository dataRepository,
    MaxApi api
  ) {
    this.data = data;
    this.dataRepository = dataRepository;
    this.api = api;
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

  public void announce() {
    MessageRequest request = new MessageRequest(data.text());
    api.sendMessage(request, data.chatId()).join();
  }
}
