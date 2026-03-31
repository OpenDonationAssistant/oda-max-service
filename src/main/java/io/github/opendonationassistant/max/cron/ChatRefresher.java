package io.github.opendonationassistant.max.cron;

import io.github.opendonationassistant.commons.logging.ODALogger;
import io.github.opendonationassistant.integration.max.MaxApi;
import io.github.opendonationassistant.integration.max.MaxApi.Chat;
import io.github.opendonationassistant.integration.max.MaxApi.PagedChats;
import io.github.opendonationassistant.max.repository.ChatData;
import io.github.opendonationassistant.max.repository.ChatRepository;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

@Singleton
public class ChatRefresher {

  private ODALogger log = new ODALogger(this);
  private final MaxApi api;
  private final ChatRepository chatRepository;
  private Long lastEventTime = 0L;

  @Inject
  public ChatRefresher(MaxApi api, ChatRepository chatRepository) {
    this.api = api;
    this.chatRepository = chatRepository;
  }

  @Scheduled(fixedDelay = "60s")
  public void refreshChats() {
    log.info("Refreshing chats", Map.of());
    final PagedChats page = api.getChats(100, null).join();
    final List<Chat> chats = page.chats();
    boolean shouldContinue = addChats(chats);
    if (shouldContinue) {
      refreshChats(page.marker());
    }
    Optional.ofNullable(chats)
      .map(list -> list.getFirst())
      .map(chat -> chat.lastEventTime())
      .ifPresent(lastEventTime -> {
        this.lastEventTime = lastEventTime;
        log.info("Last event time updated", Map.of("lastEventTime", lastEventTime));
      });
  }

  public void refreshChats(Integer marker) {
    if (marker == null) {
      return;
    }
    log.info("Reading next page of chats", Map.of());
    final PagedChats page = api.getChats(100, marker).join();

    boolean shouldContinue = addChats(page.chats());
    if (shouldContinue) {
      refreshChats(page.marker());
    }
  }

  private boolean addChats(@Nullable List<Chat> chats) {
    if (chats == null) {
      return false;
    }
    chats
      .stream()
      .filter(chat -> "active".equals(chat.status()))
      .forEach(chat -> {
        var ownerId = chat.ownerId();
        if (ownerId == null) {
          return;
        }
        chatRepository.save(
          new ChatData(chat.id(), chat.title(), chat.ownerId())
        );
      });
    return Optional.ofNullable(chats.getLast())
      .map(chat -> chat.lastEventTime())
      .filter(lastEventTime -> lastEventTime > this.lastEventTime)
      .isPresent();
  }
}
