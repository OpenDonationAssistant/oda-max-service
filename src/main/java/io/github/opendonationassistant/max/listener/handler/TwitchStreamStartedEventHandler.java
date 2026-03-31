package io.github.opendonationassistant.max.listener.handler;

import io.github.opendonationassistant.events.AbstractMessageHandler;
import io.github.opendonationassistant.events.twitch.events.TwitchStreamStartedEvent;
import io.github.opendonationassistant.max.repository.AnnouncerRepository;
import io.micronaut.serde.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.io.IOException;

@Singleton
public class TwitchStreamStartedEventHandler
  extends AbstractMessageHandler<TwitchStreamStartedEvent> {

  private final AnnouncerRepository announcerRepository;

  @Inject
  public TwitchStreamStartedEventHandler(
    ObjectMapper mapper,
    AnnouncerRepository announcerRepository
  ) {
    super(mapper);
    this.announcerRepository = announcerRepository;
  }

  @Override
  public void handle(TwitchStreamStartedEvent message) throws IOException {
    announcerRepository
      .findByRecipientId(message.recipientId())
      .forEach(it -> it.announce());
  }
}
