package io.github.opendonationassistant.max.repository;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class ChatRepository {

  private final ChatDataRepository dataRepository;

  @Inject
  public ChatRepository(ChatDataRepository dataRepository) {
    this.dataRepository = dataRepository;
  }

  public Chat save(ChatData data) {
    dataRepository.save(data);
    return new Chat(data, dataRepository);
  }
}
