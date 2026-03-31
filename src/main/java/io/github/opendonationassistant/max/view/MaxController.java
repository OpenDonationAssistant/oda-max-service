package io.github.opendonationassistant.max.view;

import io.github.opendonationassistant.commons.micronaut.BaseController;
import io.github.opendonationassistant.max.repository.AnnouncerData;
import io.github.opendonationassistant.max.repository.AnnouncerDataRepository;
import io.github.opendonationassistant.max.repository.MaxAccountRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.inject.Inject;
import java.util.List;

@Controller
public class MaxController extends BaseController {

  private final AnnouncerDataRepository repository;
  private final MaxAccountRepository accountRepository;

  @Inject
  public MaxController(
    AnnouncerDataRepository repository,
    MaxAccountRepository accountRepository
  ) {
    this.repository = repository;
    this.accountRepository = accountRepository;
  }

  @Get("/max/announcers")
  @Secured(SecurityRule.IS_AUTHENTICATED)
  public HttpResponse<List<AnnouncerData>> announcers(Authentication auth) {
    var ownerId = getOwnerId(auth);
    if (ownerId.isEmpty()) {
      return HttpResponse.unauthorized();
    }
    return HttpResponse.ok(repository.findByRecipientId(ownerId.get()));
  }

  @Get("/max/accounts")
  @Secured(SecurityRule.IS_AUTHENTICATED)
  public HttpResponse<List<AccountDto>> accounts(Authentication auth) {
    var ownerId = getOwnerId(auth);
    if (ownerId.isEmpty()) {
      return HttpResponse.unauthorized();
    }
    return HttpResponse.ok(
      accountRepository
        .findByRecipientId(ownerId.get())
        .stream()
        .map(it -> new AccountDto(it.data().maxId()))
        .toList()
    );
  }

  @Serdeable
  public static record AccountDto(Long id) {}
}
