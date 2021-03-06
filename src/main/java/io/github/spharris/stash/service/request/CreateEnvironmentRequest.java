package io.github.spharris.stash.service.request;

import com.google.auto.value.AutoValue;

import io.github.spharris.stash.Environment;

@AutoValue
public abstract class CreateEnvironmentRequest {

  public abstract String getProjectId();
  public abstract Environment getEnvironment();
  
  public abstract Builder toBuilder();
  public static Builder builder() {
    return new AutoValue_CreateEnvironmentRequest.Builder();
  }
  
  @AutoValue.Builder
  public abstract static class Builder {
    
    public abstract Builder setProjectId(String projectId);
    public abstract Builder setEnvironment(Environment environment);
    
    public abstract CreateEnvironmentRequest build();
  }
}
