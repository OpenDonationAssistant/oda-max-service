package io.github.opendonationassistant.max.commands;

import io.github.opendonationassistant.commons.logging.ODALogger;
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
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.jspecify.annotations.Nullable;

@Controller
public class UpdateAnnouncer extends BaseController {

  private final ODALogger log = new ODALogger(this);

  private final AnnouncerRepository repository;

  @Inject
  public UpdateAnnouncer(AnnouncerRepository repository) {
    this.repository = repository;
  }

  @Post("/max/commands/update-announcer")
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
  @Transactional
  public CompletableFuture<HttpResponse<Void>> updateAnnouncer(
    Authentication auth,
    @Body UpdateAnnouncerRequest request
  ) {
    var ownerId = getOwnerId(auth);
    if (ownerId.isEmpty()) {
      return CompletableFuture.completedFuture(HttpResponse.unauthorized());
    }
    return CompletableFuture.supplyAsync(() ->
      repository
        .findById(request.id())
        .filter(it -> it.data().recipientId().equals(ownerId.get()))
        .map(announcer -> {
          announcer.update(
            request.text(),
            Optional.ofNullable(request.buttons())
              .map(buttons ->
                buttons
                  .stream()
                  .map(it ->
                    new io.github.opendonationassistant.max.repository.AnnouncerData.Button(
                      it.text(),
                      it.url()
                    )
                  )
                  .toList()
              )
              .orElse(null),
            request.trigger(),
            request.type()
          );
          return HttpResponse.<Void>ok();
        })
        .orElse(HttpResponse.unauthorized())
    );
  }

  @Serdeable
  public static record UpdateAnnouncerRequest(
    String id,
    @Nullable String text,
    @Nullable List<Button> buttons,
    @Nullable String trigger,
    @Nullable String type
  ) {}

  @Serdeable
  public static record Button(String text, String url) {}
}
