package io.github.opendonationassistant.max.commands;

import io.github.opendonationassistant.commons.logging.ODALogger;
import io.github.opendonationassistant.commons.micronaut.BaseController;
import io.github.opendonationassistant.max.repository.Announcer;
import io.github.opendonationassistant.max.repository.AnnouncerData.AnnouncerType;
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

  @Post("/max/commands/update-announcers")
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
    final var updating = CompletableFuture.runAsync(() -> {
      request
        .updated()
        .forEach(update -> {
          repository
            .findById(update.id())
            .filter(it -> it.data().recipientId().equals(ownerId.get()))
            .ifPresent(announcer -> {
              announcer.update(
                update.text(),
                Optional.ofNullable(update.buttons())
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
                update.type(),
                update.enabled()
              );
            });
        });
    });
    final var creating = CompletableFuture.runAsync(() -> {
      request
        .added()
        .forEach(newAnnouncer -> {
          repository.create(
            ownerId.get(),
            newAnnouncer.accountId(),
            newAnnouncer.chatId(),
            newAnnouncer.text(),
            newAnnouncer
              .buttons()
              .stream()
              .map(it ->
                new io.github.opendonationassistant.max.repository.AnnouncerData.Button(
                  it.text(),
                  it.url()
                )
              )
              .toList(),
            newAnnouncer.type()
          );
        });
    });
    final var deleting = CompletableFuture.runAsync(() -> {
      request
        .deleted()
        .stream()
        .flatMap(id -> repository.findById(id).stream())
        .forEach(Announcer::delete);
    });
    return CompletableFuture.allOf(updating, creating, deleting).thenApplyAsync(
      it -> HttpResponse.ok()
    );
  }

  @Serdeable
  public static record NewAnnouncer(
    String text,
    String accountId,
    Long chatId,
    List<Button> buttons,
    AnnouncerType type
  ) {}

  @Serdeable
  public static record AnnouncerUpdate(
    String id,
    @Nullable String text,
    @Nullable List<Button> buttons,
    @Nullable String trigger,
    @Nullable AnnouncerType type,
    @Nullable Boolean enabled
  ) {}

  @Serdeable
  public static record UpdateAnnouncerRequest(
    List<NewAnnouncer> added,
    List<AnnouncerUpdate> updated,
    List<String> deleted
  ) {}

  @Serdeable
  public static record Button(String text, String url) {}
}
