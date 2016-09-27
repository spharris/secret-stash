package io.github.spharris.stash.service.request;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class GetEnvironmentRequest {

  public abstract String getProjectId();
  public abstract String getEnvironmentId();
  
  public abstract Builder toBuilder();
  public static Builder builder() {
    return new AutoValue_GetEnvironmentRequest.Builder();
  }
  
  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder setProjectId(String projectId);
    public abstract Builder setEnvironmentId(String environmentId);
    
    public abstract GetEnvironmentRequest build();
  }
}
