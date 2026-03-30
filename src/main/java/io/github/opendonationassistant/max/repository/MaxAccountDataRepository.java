package io.github.opendonationassistant.max.repository;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;
import java.util.List;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface MaxAccountDataRepository
  extends CrudRepository<MaxAccountData, String> {
  public List<MaxAccountData> findByMaxId(String maxId);

  public List<MaxAccountData> findByRecipientId(String recipientId);
}
