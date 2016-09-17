package io.github.spharris.stash.server;

import com.google.inject.AbstractModule;

public class ServerModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(SecretController.class);
  }
}
