package io.github.spharris.stash.server;

import com.google.inject.AbstractModule;

import io.github.spharris.stash.service.EnvironmentService;
import io.github.spharris.stash.service.ProjectService;
import io.github.spharris.stash.service.SecretService;

/**
 * Module for web endpoints of the secret-stash server 
 */
class StashServerModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(ProjectService.class);
    requireBinding(EnvironmentService.class);
    requireBinding(SecretService.class);
    
    bind(ProjectController.class);
    bind(EnvironmentController.class);
    bind(SecretController.class);
  }
}
