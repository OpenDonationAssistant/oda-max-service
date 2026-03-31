package io.github.opendonationassistant.max.repository;

import java.util.List;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface ChatDataRepository extends CrudRepository<ChatData, Integer> {
  List<ChatData> findByOwnerId(String ownerId);
}
