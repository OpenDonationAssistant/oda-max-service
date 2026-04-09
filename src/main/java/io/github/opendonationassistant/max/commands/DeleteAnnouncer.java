package io.github.opendonationassistant.max.commands;

import io.github.opendonationassistant.commons.micronaut.BaseController;
import io.github.opendonationassistant.max.repository.AnnouncerRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.inject.Inject;
import java.util.concurrent.CompletableFuture;

@Controller
public class DeleteAnnouncer extends BaseController {

  private final AnnouncerRepository repository;

  @Inject
  public DeleteAnnouncer(AnnouncerRepository repository) {
    this.repository = repository;
  }

  @Post("/max/commands/delete-announcer")
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
  @ApiResponse(
    responseCode = "404",
    description = "Not Found",
    content = @io.swagger.v3.oas.annotations.media.Content(
      schema = @io.swagger.v3.oas.annotations.media.Schema(
        implementation = Void.class
      )
    )
  )
  public CompletableFuture<HttpResponse<Void>> deleteAnnouncer(
    Authentication auth,
    @Body DeleteAnnouncerCommand command
  ) {
    var ownerId = getOwnerId(auth);
    if (ownerId.isEmpty()) {
      return CompletableFuture.completedFuture(HttpResponse.unauthorized());
    }
    return CompletableFuture.supplyAsync(() ->
      repository
        .findById(command.announcerId())
        .filter(it -> it.data().recipientId().equals(ownerId.get()))
        .map(it -> {
          it.delete();
          return HttpResponse.<Void>ok();
        })
        .orElseGet(() -> HttpResponse.notFound())
    );
  }

  @Serdeable
  public static record DeleteAnnouncerCommand(String announcerId) {}
}
