package io.github.spharris.stash.service.db;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.sqlite.SQLiteConfig;

import io.github.spharris.stash.service.db.Annotations.DatabaseFile;

@Singleton
public class DatabaseServiceImpl implements DatabaseService {

  private static final String SCHEMA_FILE = "db/schema.sql";
  private static final String MEMORY_FILE = ":memory:";
  
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
    boolean loadSchema = false;
    if (!dbFile.equals(MEMORY_FILE) && Files.notExists(Paths.get(dbFile).toAbsolutePath())) {
      loadSchema = true;
    }
    
    SQLiteConfig config = new SQLiteConfig();
    config.enforceForeignKeys(true);
    connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile, config.toProperties());
    
    if (loadSchema) {
      loadSchema();
    }
  }

  @Override
  public void stop() throws SQLException {
    if (connection != null) {
      connection.close();
    }
  }
  
  private void loadSchema() throws SQLException {
    Scanner s = new Scanner(getClass().getClassLoader().getResourceAsStream(SCHEMA_FILE));
    s.useDelimiter(";");

    while (s.hasNext()) {
      String token = s.next();
      
      Statement statement = getConnection().createStatement();
      statement.executeUpdate(token);
    }
    
    s.close();
  }
}
