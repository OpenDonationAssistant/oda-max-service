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
import jakarta.inject.Inject;
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

  @Serdeable
  public record ChatDto(Integer id, String title) {}
}
