package io.github.spharris.stash.service.db;

import java.sql.Statement;
import java.util.Scanner;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;

import com.google.inject.Guice;
import com.google.inject.Injector;

import io.github.spharris.stash.service.testing.TestDatabaseModule;

public abstract class BaseDaoTest {
  
  static final String SCHEMA_FILE = "db/schema.sql";
  
  Injector injector;
  
  @Inject DatabaseService dbService;
  
  @Before
  public void initialize() throws Exception {
    injector = Guice.createInjector(new TestDatabaseModule());
    injector.injectMembers(this);
    
    dbService.start();
    
    executeFile(SCHEMA_FILE);
  }
  
  @After
  public void cleanup() throws Exception {
    dbService.stop();
  }
  
  void executeFile(String file) throws Exception {
    Scanner s = new Scanner(getClass().getClassLoader().getResourceAsStream(file));
    s.useDelimiter(";");

    while (s.hasNext()) {
      String token = s.next();
      
      Statement statement = dbService.getConnection().createStatement();
      statement.executeUpdate(token);
    }
    
    s.close();
  }
}
