package io.github.opendonationassistant.max.commands;

import io.github.opendonationassistant.commons.micronaut.BaseController;
import io.github.opendonationassistant.max.repository.AnnouncerRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.inject.Inject;
import java.util.concurrent.CompletableFuture;

@Controller
public class ToggleAnnouncer extends BaseController {

  private final AnnouncerRepository repository;

  @Inject
  public ToggleAnnouncer(AnnouncerRepository repository) {
    this.repository = repository;
  }

  @Post("/max/commands/toggle-announcer")
  @Secured(SecurityRule.IS_AUTHENTICATED)
  @ApiResponse(
    responseCode = "200",
    description = "OK",
    content = @io.swagger.v3.oas.annotations.media.Content(
      schema = @io.swagger.v3.oas.annotations.media.Schema(
        implementation = Void.class
      )
    )
  )
  public CompletableFuture<HttpResponse<Void>> disableAnnouncer(
    Authentication auth
  ) {
    var ownerId = getOwnerId(auth);
    if (ownerId.isEmpty()) {
      return CompletableFuture.completedFuture(HttpResponse.unauthorized());
    }
    repository.findByRecipientId(ownerId.get()).forEach(it -> it.toggle());
    return CompletableFuture.completedFuture(HttpResponse.ok());
  }
}
