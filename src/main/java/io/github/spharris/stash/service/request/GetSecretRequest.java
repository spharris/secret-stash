package io.github.spharris.stash.service.request;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class GetSecretRequest {

  public abstract String getProjectId();
  public abstract String getEnvironmentId();
  public abstract String getSecretId();
  public abstract boolean getIncludeSecretValue();
  
  public abstract Builder toBuilder();
  public static Builder builder() {
    return new AutoValue_GetSecretRequest.Builder()
        .setIncludeSecretValue(false);
  }
  
  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder setProjectId(String projectId);
    public abstract Builder setEnvironmentId(String environmentId);
    public abstract Builder setSecretId(String secretId);
    public abstract Builder setIncludeSecretValue(boolean includeSecretValue);
    
    public abstract GetSecretRequest build();
  }
}
