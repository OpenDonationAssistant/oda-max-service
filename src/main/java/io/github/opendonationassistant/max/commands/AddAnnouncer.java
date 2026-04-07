package io.github.opendonationassistant.max.commands;

import io.github.opendonationassistant.commons.logging.ODALogger;
import io.github.opendonationassistant.commons.micronaut.BaseController;
import io.github.opendonationassistant.max.repository.AnnouncerData;
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
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Controller
public class AddAnnouncer extends BaseController {

  private final ODALogger log = new ODALogger(this);

  private final AnnouncerRepository repository;

  @Inject
  public AddAnnouncer(AnnouncerRepository repository) {
    this.repository = repository;
  }

  @Post("/max/commands/add-announcer")
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
  public CompletableFuture<HttpResponse<Void>> addAnnouncer(
    Authentication auth,
    @Body AddAnnouncerRequest request
  ) {
    var ownerId = getOwnerId(auth);
    if (ownerId.isEmpty()) {
      return CompletableFuture.completedFuture(HttpResponse.unauthorized());
    }
    repository.create(
      ownerId.get(),
      request.accountId(),
      request.chatId(),
      request.text(),
      request
        .buttons()
        .stream()
        .map(it -> new AnnouncerData.Button(it.text(), it.url()))
        .toList(),
      request.trigger(),
      request.type()
    );
    return CompletableFuture.completedFuture(HttpResponse.ok());
  }

  @Serdeable
  public static record AddAnnouncerRequest(
    String text,
    String accountId,
    Long chatId,
    List<Button> buttons,
    String trigger,
    String type
  ) {}

  @Serdeable
  public static record Button(String text, String url) {}
}
