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
  Integer chatId,
  String text,
  @MappedProperty(type = DataType.JSON, value = "buttons") List<Button> buttons,
  Boolean enabled
) {
  @Serdeable
  public static record Button(String text, String url) {}
}
