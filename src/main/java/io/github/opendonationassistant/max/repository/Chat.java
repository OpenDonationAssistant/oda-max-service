package io.github.opendonationassistant.max.repository;

public class Chat {

  private ChatData data;
  private final ChatDataRepository dataRepository;

  public Chat(ChatData data, ChatDataRepository dataRepository) {
    this.data = data;
    this.dataRepository = dataRepository;
  }
}
