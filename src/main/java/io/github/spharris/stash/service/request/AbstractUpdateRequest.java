package io.github.spharris.stash.service.request;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;

@AutoValue
abstract class AbstractUpdateRequest {
  
  AbstractUpdateRequest() {}
  
  public abstract ImmutableSet<String> getFields();
}
