package io.github.opendonationassistant.max.commands;

import io.github.opendonationassistant.commons.micronaut.BaseController;
import io.github.opendonationassistant.max.repository.MaxAccountRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Controller
public class ListAvailableChats extends BaseController {

  private final MaxAccountRepository accountRepository;

  @Inject
  public ListAvailableChats(MaxAccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Post("/max/commands/list-available-chats")
  @Secured(SecurityRule.IS_AUTHENTICATED)
  @ApiResponse(
    responseCode = "200",
    description = "OK",
    content = @io.swagger.v3.oas.annotations.media.Content(
      schema = @io.swagger.v3.oas.annotations.media.Schema(
        implementation = ChatsResponse.class
      )
    )
  )
  public CompletableFuture<HttpResponse<List<ChatDto>>> listAvailableChats(
    Authentication auth
  ) {
    var ownerId = getOwnerId(auth);
    if (ownerId.isEmpty()) {
      return CompletableFuture.completedFuture(HttpResponse.unauthorized());
    }
    return CompletableFuture.completedFuture(
      HttpResponse.ok(
        accountRepository
          .findByRecipientId(ownerId.get())
          .stream()
          .flatMap(it ->
            it
              .chats()
              .stream()
              .map(data -> new ChatDto(data.data().id(), data.data().title()))
          )
          .toList()
      )
    );
  }

  public static class ChatsResponse extends ArrayList<ChatDto> {}

  @Serdeable
  public record ChatDto(Long id, String title) {}
}
