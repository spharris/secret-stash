package io.github.spharris.stash.service.db;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

public class Annotations {

  @Qualifier
  @Retention(RetentionPolicy.RUNTIME)
  public @interface DatabaseFile {}
}
