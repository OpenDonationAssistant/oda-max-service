package io.github.opendonationassistant.max.repository;

import io.github.opendonationassistant.integration.max.MaxApi;
import io.github.opendonationassistant.integration.max.MaxApi.MessageRequest;
import io.github.opendonationassistant.integration.max.model.Message;
import io.github.opendonationassistant.max.repository.AnnouncerData.Button;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

public class Announcer {

  private AnnouncerData data;
  private final AnnouncerDataRepository dataRepository;
  private final AnnounceRepository announceRepository;
  private final MaxApi api;

  public Announcer(
    AnnouncerData data,
    AnnouncerDataRepository dataRepository,
    AnnounceRepository announceRepository,
    MaxApi api
  ) {
    this.data = data;
    this.dataRepository = dataRepository;
    this.announceRepository = announceRepository;
    this.api = api;
  }

  public void toggle() {
    this.data = new AnnouncerData(
      data.id(),
      data.recipientId(),
      data.accountId(),
      data.chatId(),
      data.text(),
      data.buttons(),
      !data.enabled(),
      data.condition(),
      data.announcerType()
    );
    save();
  }

  public AnnouncerData data() {
    return data;
  }

  public void update(
    @Nullable String text,
    @Nullable List<Button> buttons,
    @Nullable String trigger,
    @Nullable String type,
    @Nullable Boolean enabled
  ) {
    this.data = new AnnouncerData(
      data.id(),
      data.recipientId(),
      data.accountId(),
      data.chatId(),
      Optional.ofNullable(text).orElse(data.text()),
      Optional.ofNullable(buttons).orElse(data.buttons()),
      Optional.ofNullable(enabled).orElse(data.enabled()),
      Optional.ofNullable(trigger).orElse(data.condition()),
      Optional.ofNullable(type).orElse(data.announcerType())
    );
    save();
  }

  private void save() {
    dataRepository.update(data);
  }

  public void announce() {
    MessageRequest request = new MessageRequest(data.text());
    final Message announced = api.sendMessage(request, data.chatId()).join();
    announceRepository.create(announced.body().mid(), data.chatId());
  }

  public void delete() {
    dataRepository.delete(data);
  }
}
