package io.github.spharris.stash.service.request;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class GetProjectRequest {

  public abstract String getProjectId();
  
  public abstract Builder toBuilder();
  public static Builder builder() {
    return new AutoValue_GetProjectRequest.Builder();
  }
  
  @AutoValue.Builder
  public abstract static class Builder {
    
    public abstract Builder setProjectId(String projectId);
    
    public abstract GetProjectRequest build();
  }
}
