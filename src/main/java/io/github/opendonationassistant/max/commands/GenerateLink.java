package io.github.opendonationassistant.max.commands;

import com.fasterxml.uuid.Generators;
import io.github.opendonationassistant.commons.micronaut.BaseController;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.inject.Inject;
import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Controller
public class GenerateLink extends BaseController {

  private final Map<String, String> linkCodes;

  @Inject
  public GenerateLink(Map<String, String> linkCodes) {
    this.linkCodes = linkCodes;
  }

  @Post
  @Secured(SecurityRule.IS_AUTHENTICATED)
  public CompletableFuture<HttpResponse<TemporaryLink>> generateLink(
    Authentication auth
  ) {
    var ownerId = getOwnerId(auth);
    if (ownerId.isEmpty()) {
      return CompletableFuture.completedFuture(HttpResponse.unauthorized());
    }
    try {
      var temporaryId = Generators.timeBasedEpochGenerator(
        SecureRandom.getInstanceStrong()
      )
        .generate()
        .toString();
      linkCodes.put(temporaryId, ownerId.get());
      return CompletableFuture.completedFuture(
        HttpResponse.ok(
          new TemporaryLink(
            "https://max.ru/id502507151879_bot?start=%s".formatted(temporaryId)
          )
        )
      );
    } catch (Exception e) {
      return CompletableFuture.completedFuture(HttpResponse.serverError());
    }
  }

  @Serdeable
  public static record TemporaryLink(String link) {}
}
