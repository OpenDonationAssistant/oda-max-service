package io.github.opendonationassistant.integration.max;

import static io.micronaut.http.HttpHeaders.AUTHORIZATION;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.opendonationassistant.integration.max.model.Message;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.serde.annotation.Serdeable;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Client("max")
@Header(name = AUTHORIZATION, value = "Bearer ${max.token}")
public interface MaxApi {
  @Post("/messages")
  public CompletableFuture<Message> sendMessage(
    @Body MessageRequest request,
    @QueryValue("chat_id") String chatId
  );

  @Get("/chats/{chatId}")
  public CompletableFuture<Chat> getChatInfo(@PathVariable Integer chatId);

  @Serdeable
  public static record Chat(
    @JsonProperty("chat_id") Integer id,
    String title,
    @JsonProperty("owner_id") String ownerId
  ) {}

  @Serdeable
  public static record MessageRequest(String text) {}

  @Serdeable
  public static record AttachmentRequest() {}

  @Post("/subscriptions")
  public CompletableFuture<SubscriptionResponse> subscribe(
    @Body SubscriptionRequest request
  );

  @Serdeable
  public static record SubscriptionRequest(
    String url,
    @JsonProperty("update_types") List<String> events,
    String secret
  ) {}

  @Serdeable
  public static record SubscriptionResponse(Boolean success, String message) {}
}
