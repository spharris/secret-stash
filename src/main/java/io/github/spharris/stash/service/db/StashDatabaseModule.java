package io.github.spharris.stash.service.db;

import com.google.inject.AbstractModule;
import com.google.inject.Key;

import io.github.spharris.stash.service.db.Annotations.DatabaseFile;

public class StashDatabaseModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(EnvironmentDao.class).to(EnvironmentDaoImpl.class);
  }
}
