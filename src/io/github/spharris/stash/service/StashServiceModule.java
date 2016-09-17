package io.github.spharris.stash.service;

import com.google.inject.AbstractModule;

/**
 * secret-stash backend services
 */
public class StashServiceModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(ProjectService.class).to(ProjectServiceImpl.class);
    bind(EnvironmentService.class).to(EnvironmentServiceImpl.class);
    bind(SecretService.class).to(SecretServiceImpl.class);
  }
}
