package io.github.spharris.stash.service.db;

import com.google.inject.AbstractModule;
import com.google.inject.Key;

import io.github.spharris.stash.service.db.Annotations.DatabaseFile;

public class StashDatabaseModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(Key.get(String.class, DatabaseFile.class)).toInstance("stash-db.sqlite");
    
    bind(DatabaseService.class).to(DatabaseServiceImpl.class);
    bind(EnvironmentDao.class).to(EnvironmentDaoImpl.class);
  }
}
