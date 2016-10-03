package io.github.spharris.stash.service.db;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import com.google.inject.AbstractModule;
import com.google.inject.Key;

import io.github.spharris.stash.service.db.Annotations.DatabaseFile;

public class StashDatabaseModule extends AbstractModule {

  @Override
  protected void configure() {
    // Create the database in the same directory as the JAR file
    try {
      URI parentDirectory = new File(
        getClass().getProtectionDomain().getCodeSource().getLocation().toURI())
          .getParentFile().toURI();
      bind(Key.get(String.class, DatabaseFile.class)).toInstance(
        Paths.get(parentDirectory.resolve("stash-db.sqlite")).toAbsolutePath().toString());
    } catch (URISyntaxException e) {
      throw new RuntimeException("Unable to open database file", e);
    }
    
    bind(DatabaseService.class).to(DatabaseServiceImpl.class);
    bind(ProjectDao.class).to(ProjectDaoImpl.class);
    bind(EnvironmentDao.class).to(EnvironmentDaoImpl.class);
    bind(SecretDao.class).to(SecretDaoImpl.class);
  }
}
