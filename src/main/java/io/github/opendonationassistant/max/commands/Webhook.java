package io.github.opendonationassistant.max.commands;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.opendonationassistant.commons.logging.ODALogger;
import io.github.opendonationassistant.integration.max.model.Message;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.serde.annotation.Serdeable;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Controller
public class Webhook {

  private ODALogger log = new ODALogger(this);

  @Post("/notification/max")
  @Secured(SecurityRule.IS_ANONYMOUS)
  public CompletableFuture<HttpResponse<Void>> webhook(
    @Body WebhookPayload payload
  ) {
    log.info("Received webhook", Map.of("payload", payload));
    return CompletableFuture.completedFuture(HttpResponse.ok());
  }

  @Serdeable
  public static record WebhookPayload(
    @JsonProperty("update_type") String updateType,
    String timestamp,
    Message message
  ) {}
}
