package io.github.opendonationassistant.max.commands;

import io.github.opendonationassistant.commons.micronaut.BaseController;
import io.github.opendonationassistant.max.repository.MaxAccountRepository;
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
public class DeleteAccount extends BaseController {

  private final MaxAccountRepository repository;

  @Inject
  public DeleteAccount(MaxAccountRepository repository) {
    this.repository = repository;
  }

  @Post("/max/commands/delete-account")
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
  public CompletableFuture<HttpResponse<Void>> deleteAccount(
    Authentication auth,
    @Body DeleteAccountCommand command
  ) {
    var ownerId = getOwnerId(auth);
    if (ownerId.isEmpty()) {
      return CompletableFuture.completedFuture(HttpResponse.unauthorized());
    }
    return CompletableFuture.supplyAsync(() ->
      repository
        .findByRecipientId(ownerId.get())
        .filter(it -> it.data().maxId().equals(command.id()))
        .map(it -> {
          it.delete();
          return HttpResponse.<Void>ok();
        })
        .orElseGet(() -> HttpResponse.notFound())
    );
  }

  @Serdeable
  public static record DeleteAccountCommand(Long id) {}
}
