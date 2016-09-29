package io.github.spharris.stash.service.request;

import com.google.auto.value.AutoValue;

import io.github.spharris.stash.Environment;

@AutoValue
public abstract class DeleteEnvironmentPolicyRequest {

  public abstract Environment getEnvironment();
  
  public abstract Builder toBuilder();
  public static Builder builder() {
    return new AutoValue_DeleteEnvironmentPolicyRequest.Builder();
  }
  
  @AutoValue.Builder
  public abstract static class Builder {
    
    public abstract Builder setEnvironment(Environment environment);
    
    public abstract DeleteEnvironmentPolicyRequest build();
  }
}
