package io.github.opendonationassistant.max.repository;

import io.github.opendonationassistant.integration.max.MaxApi;
import java.util.List;

public class MaxAccount {

  private final MaxAccountData data;
  private final MaxApi api;
  private final ChatRepository chatRepository;
  private final MaxAccountDataRepository dataRepository;

  public MaxAccount(
    MaxAccountData data,
    MaxApi api,
    ChatRepository chatRepository,
    MaxAccountDataRepository dataRepository
  ) {
    this.data = data;
    this.api = api;
    this.chatRepository = chatRepository;
    this.dataRepository = dataRepository;
  }

  public List<Chat> chats() {
    return chatRepository.list(data.maxId());
  }

  public MaxAccountData data(){
    return data;
  }

  public void delete(){
    dataRepository.delete(data);
  }

  public void setEnabled(boolean enabled) {
    var newData = new MaxAccountData(
      data.id(),
      data.recipientId(),
      data.maxId(),
      enabled
    );
    dataRepository.update(newData);
  }
}
