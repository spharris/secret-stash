package io.github.spharris.stash.service.db;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseService {

  Connection getConnection();
  void start() throws SQLException;
  void stop() throws SQLException;
}
