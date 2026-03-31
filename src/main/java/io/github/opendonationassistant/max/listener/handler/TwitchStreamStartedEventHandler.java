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

  private AnnouncerRepository announcerRepository;

  @Inject
  public TwitchStreamStartedEventHandler(ObjectMapper mapper) {
    super(mapper);
  }

  @Override
  public void handle(TwitchStreamStartedEvent message) throws IOException {
    announcerRepository
      .findByRecipientId(message.recipientId())
      .forEach(it -> it.announce());
  }
}
