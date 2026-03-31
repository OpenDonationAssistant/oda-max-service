package io.github.opendonationassistant.max.repository;

import io.github.opendonationassistant.integration.max.MaxApi;
import java.util.List;

public class MaxAccount {

  private final MaxAccountData data;
  private final MaxApi api;
  private final ChatRepository chatRepository;

  public MaxAccount(
    MaxAccountData data,
    MaxApi api,
    ChatRepository chatRepository
  ) {
    this.data = data;
    this.api = api;
    this.chatRepository = chatRepository;
  }

  public List<Chat> chats() {
    return chatRepository.list(data.maxId());
  }

  public MaxAccountData data(){
    return data;
  }
}
