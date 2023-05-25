package com.synergy.api.database.impl.sql;

import com.synergy.api.database.AbstractDataHandler;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Executor;

public abstract class SqlDataHandler<T> extends AbstractDataHandler<T> {

  protected final Connection connection;

  public SqlDataHandler(Executor executor, Connection connection) {
    super(executor);
    this.connection = connection;
  }

  @Override
  public void close() {
    try {
      if (connection != null && !connection.isClosed()) {
        connection.close();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
