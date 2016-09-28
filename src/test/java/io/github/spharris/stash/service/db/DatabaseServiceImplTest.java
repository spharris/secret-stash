package io.github.spharris.stash.service.db;

import static com.google.common.truth.Truth.assertThat;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class DatabaseServiceImplTest {

  @Test
  public void doesntCreateMemoryFile() throws Exception {
    String file = ":memory:";
    DatabaseService svc = new DatabaseServiceImpl(file);
    
    // No exception should be thrown
    svc.start();
  }
  
  @Test
  public void createsDbFile() throws Exception {
    String file = getPathString("target/test-classes/test-file.db");
    DatabaseService svc = new DatabaseServiceImpl(file);
    svc.start();

    try {
      svc.stop();
      assertThat(Files.exists(Paths.get(file))).isTrue();
    } finally {
      cleanup(file);
    }
  }
  
  @Test
  public void createsSchema() throws Exception {
    String file = getPathString("target/test-classes/another-test-file.db");
    DatabaseService svc = new DatabaseServiceImpl(file);
    svc.start();
    
    try {
      Statement statement = svc.getConnection().createStatement();
      statement.execute("SELECT * FROM project;");
    } finally {
      svc.stop();
      cleanup(file);
    }
  }
  
  @Test
  public void doesntRecreateSchema() throws Exception {
    String file = getPathString("target/test-classes/a-third-test-file.db");
    DatabaseService svc = new DatabaseServiceImpl(file);
    svc.start();
    
    try {
      svc.getConnection().createStatement().executeUpdate(
        "INSERT INTO project (project_id, description) VALUES ('test', 'test');");
      svc.stop();
      
      svc.start();
      Statement statement = svc.getConnection().createStatement();
      statement.execute("SELECT * FROM project;");
      ResultSet rs = statement.getResultSet();
      
      assertThat(rs.isBeforeFirst()).isTrue();
    } finally {
      svc.stop();
      cleanup(file);
    } 
  }
  
  String getPathString(String file) throws Exception {
    return Paths.get(file).toAbsolutePath().toString();
  }
  
  static void cleanup(String file) throws Exception {
    Files.deleteIfExists(Paths.get(file));
  }
}
