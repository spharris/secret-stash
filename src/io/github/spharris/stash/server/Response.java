package io.github.spharris.stash.server;

import javax.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

@AutoValue
abstract class Response<T> {
  
  Response() {}
  
  abstract @Nullable T getValue();
  abstract ImmutableList<Error> getErrors();
  
  static <T> Builder<T> builder() {
    return new AutoValue_Response.Builder<T>()
        .setErrors(ImmutableList.<Error>of());
  }
  
  @AutoValue.Builder
  abstract static class Builder<T> {
    abstract Builder<T> setValue(T value);
    abstract Builder<T> setErrors(ImmutableList<Error> errors);
    
    abstract Response<T> build();
  }
}
