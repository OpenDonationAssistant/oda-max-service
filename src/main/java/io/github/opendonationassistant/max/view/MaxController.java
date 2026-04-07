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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.inject.Inject;
import java.util.ArrayList;
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
  @ApiResponse(
    responseCode = "200",
    description = "OK",
    content = @io.swagger.v3.oas.annotations.media.Content(
      schema = @io.swagger.v3.oas.annotations.media.Schema(
        implementation = AnnouncersResponse.class
      )
    )
  )
  public HttpResponse<List<AnnouncerDto>> announcers(Authentication auth) {
    return getOwnerId(auth)
      .map(repository::findByRecipientId)
      .map(announcers ->
        announcers.stream().map(this::mapToDto).toList()
      )
      .map(HttpResponse::ok)
      .orElseGet(HttpResponse::unauthorized);
  }

  private AnnouncerDto mapToDto(AnnouncerData data) {
    return new AnnouncerDto(
      data.id(),
      data.recipientId(),
      data.chatId(),
      data.text(),
      data.buttons(),
      data.enabled(),
      data.condition(),
      data.announcerType()
    );
  }

  @Get("/max/accounts")
  @Secured(SecurityRule.IS_AUTHENTICATED)
  @ApiResponse(
    responseCode = "200",
    description = "OK",
    content = @io.swagger.v3.oas.annotations.media.Content(
      schema = @io.swagger.v3.oas.annotations.media.Schema(
        implementation = AccountsResponse.class
      )
    )
  )
  public HttpResponse<List<AccountDto>> accounts(Authentication auth) {
    var ownerId = getOwnerId(auth);
    if (ownerId.isEmpty()) {
      return HttpResponse.unauthorized();
    }
    return HttpResponse.ok(
      accountRepository
        .findByRecipientId(ownerId.get())
        .stream()
        .map(it ->
          new AccountDto(it.data().id(), it.data().maxId(), it.data().enabled())
        )
        .toList()
    );
  }

  @Serdeable
  public static record AccountDto(String id, Long maxId, boolean enabled) {}

  @Serdeable
  public static record AnnouncerDto(
    String id,
    String recipientId,
    Long chatId,
    String text,
    List<AnnouncerData.Button> buttons,
    Boolean enabled,
    String condition,
    String announcerType
  ) {}

  public static class AnnouncersResponse extends ArrayList<AnnouncerDto> {}

  public static class AccountsResponse extends ArrayList<AccountDto> {}
}
