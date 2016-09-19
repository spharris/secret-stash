package io.github.spharris.stash.service.request;

import com.google.auto.value.AutoValue;

import io.github.spharris.stash.Secret;

@AutoValue
public abstract class CreateSecretRequest {

  public abstract String getProjectId();
  public abstract String getEnvironmentId();
  public abstract Secret getSecret();
  
  public abstract Builder toBuilder();
  public static Builder builder() {
    return new AutoValue_CreateSecretRequest.Builder();
  }
  
  @AutoValue.Builder
  public abstract static class Builder {
    
    public abstract Builder setProjectId(String projectId);
    public abstract Builder setEnvironmentId(String environmentId);
    public abstract Builder setSecret(Secret secret);
    
    public abstract CreateSecretRequest build();
  }
}
