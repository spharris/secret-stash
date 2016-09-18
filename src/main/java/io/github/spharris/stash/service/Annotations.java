package io.github.spharris.stash.service;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Annotations used for Guice configuration 
 */
class Annotations {

  private Annotations() {}
  
  @Qualifier
  @Retention(RetentionPolicy.RUNTIME)
  @interface BucketOfSecrets {}
}
