package io.github.opendonationassistant.max.repository;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.List;

@Singleton
public class ChatRepository {

  private final ChatDataRepository dataRepository;

  @Inject
  public ChatRepository(ChatDataRepository dataRepository) {
    this.dataRepository = dataRepository;
  }

  public Chat save(ChatData data) {
    dataRepository.save(data);
    return convert(data);
  }

  public List<Chat> list(Long ownerId) {
    return dataRepository
      .findByOwnerId(ownerId)
      .stream()
      .map(this::convert)
      .toList();
  }

  private Chat convert(ChatData data) {
    return new Chat(data, dataRepository);
  }
}
