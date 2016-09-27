package io.github.spharris.stash.service.testing;

import com.google.inject.AbstractModule;
import com.google.inject.Key;

import io.github.spharris.stash.service.db.Annotations.DatabaseFile;
import io.github.spharris.stash.service.db.DatabaseService;
import io.github.spharris.stash.service.db.DatabaseServiceImpl;
import io.github.spharris.stash.service.db.EnvironmentDao;
import io.github.spharris.stash.service.db.EnvironmentDaoImpl;
import io.github.spharris.stash.service.db.ProjectDao;
import io.github.spharris.stash.service.db.ProjectDaoImpl;
import io.github.spharris.stash.service.db.SecretDao;
import io.github.spharris.stash.service.db.SecretDaoImpl;

public class TestDatabaseModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(Key.get(String.class, DatabaseFile.class)).toInstance(":memory:");
    
    bind(DatabaseService.class).to(DatabaseServiceImpl.class);
    bind(ProjectDao.class).to(ProjectDaoImpl.class);
    bind(EnvironmentDao.class).to(EnvironmentDaoImpl.class);
    bind(SecretDao.class).to(SecretDaoImpl.class);
  }
}
