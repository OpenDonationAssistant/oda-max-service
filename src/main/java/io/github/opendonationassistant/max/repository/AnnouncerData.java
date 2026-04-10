package io.github.opendonationassistant.max.repository;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;
import io.micronaut.data.model.DataType;
import io.micronaut.serde.annotation.Serdeable;
import java.util.List;

@Serdeable
@MappedEntity("announcers")
public record AnnouncerData(
  @Id String id,
  String recipientId,
  String accountId,
  Long chatId,
  String text,
  @MappedProperty(type = DataType.JSON, value = "buttons") List<Button> buttons,
  Boolean enabled,
  AnnouncerType announcerType
) {
  @Serdeable
  public static record Button(String text, String url) {}

  @Serdeable
  public static enum AnnouncerType {
    ANNOUNCE_STREAM_AND_DELETE,
    ANNOUNCE_STREAM,
  }
}
