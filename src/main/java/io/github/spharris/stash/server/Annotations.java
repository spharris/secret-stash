package io.github.spharris.stash.server;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

class Annotations {
  
  private Annotations() {}
  
  @Qualifier
  @Retention(RetentionPolicy.RUNTIME)
  @interface Port {}
}
