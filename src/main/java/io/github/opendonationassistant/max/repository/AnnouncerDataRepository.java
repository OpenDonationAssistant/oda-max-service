package io.github.opendonationassistant.max.repository;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;
import java.util.List;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface AnnouncerDataRepository
  extends CrudRepository<AnnouncerData, String> {
  List<AnnouncerData> findByRecipientId(String recipientId);
}
