package io.github.opendonationassistant.max.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.opendonationassistant.commons.logging.ODALogger;
import io.github.opendonationassistant.integration.max.model.Message;
import io.github.opendonationassistant.integration.max.model.User;
import io.github.opendonationassistant.max.repository.MaxAccountRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.inject.Inject;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.jspecify.annotations.Nullable;

@Controller
public class Webhook {

  private ODALogger log = new ODALogger(this);
  private final Map<String, String> linkCodes;
  private final MaxAccountRepository repository;

  @Inject
  public Webhook(
    Map<String, String> linkCodes,
    MaxAccountRepository repository
  ) {
    this.linkCodes = linkCodes;
    this.repository = repository;
  }

  @Post("/notification/max")
  @Secured(SecurityRule.IS_ANONYMOUS)
  public CompletableFuture<HttpResponse<Void>> webhook(
    @Body WebhookPayload payload
  ) {
    log.info("Received webhook", Map.of("payload", payload));
    switch (payload.updateType()) {
      case "bot_started":
        Optional.ofNullable(payload.payload()).ifPresent(
          this::handleBotStarted
        );
        break;
      default:
        break;
    }
    return CompletableFuture.completedFuture(HttpResponse.ok());
  }

  private void handleBotStarted(String payload) {
    Optional.ofNullable(linkCodes.get(payload)).ifPresent(recipientId ->
      repository.create(recipientId, payload)
    );
  }

  @Serdeable
  public static record WebhookPayload(
    @JsonProperty("update_type") String updateType,
    String timestamp,
    Message message,
    @Nullable User user,
    @Nullable String payload
  ) {}
}
