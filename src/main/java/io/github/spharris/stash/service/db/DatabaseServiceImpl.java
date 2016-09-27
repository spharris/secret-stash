package io.github.spharris.stash.service.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.sqlite.SQLiteConfig;

import io.github.spharris.stash.service.db.Annotations.DatabaseFile;

@Singleton
public class DatabaseServiceImpl implements DatabaseService {

  private final String dbFile;
  private Connection connection;

  @Inject
  DatabaseServiceImpl(@DatabaseFile String dbFile) {
    this.dbFile = dbFile;
  }

  @Override
  public Connection getConnection() {
    if (connection == null) {
      throw new IllegalStateException("The database service is not running.");
    }
    
    return connection;
  }

  @Override
  public void start() throws SQLException {
    SQLiteConfig config = new SQLiteConfig();
    config.enforceForeignKeys(true);
    connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile, config.toProperties());
  }

  @Override
  public void stop() throws SQLException {
    if (connection != null) {
      connection.close();
    }
  }
}
