package io.github.spharris.stash.server;

import javax.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

@AutoValue
public abstract class Response<T> {
  
  Response() {}
  
  public abstract @Nullable T getValue();
  public abstract ImmutableList<Error> getErrors();
  
  public static <T> Builder<T> builder() {
    return new AutoValue_Response.Builder<T>()
        .setErrors(ImmutableList.<Error>of());
  }
  
  public static <T> Response<T> of(T value) {
    return Response.<T>builder()
        .setValue(value)
        .build();
  }
  
  public static <T> Response<T> of() {
    return Response.<T>builder().build();
  }
  
  @AutoValue.Builder
  public abstract static class Builder<T> {
    public abstract Builder<T> setValue(T value);
    public abstract Builder<T> setErrors(ImmutableList<Error> errors);
    
    public abstract Response<T> build();
  }
}
