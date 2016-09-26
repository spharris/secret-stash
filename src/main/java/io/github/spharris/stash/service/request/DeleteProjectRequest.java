package io.github.spharris.stash.service.request;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class DeleteProjectRequest {

  public abstract String getProjectId();
  
  public abstract Builder toBuilder();
  public static Builder builder() {
    return new AutoValue_DeleteProjectRequest.Builder();
  }
  
  @AutoValue.Builder
  public abstract static class Builder {
    
    public abstract Builder setProjectId(String projectId);
    
    public abstract DeleteProjectRequest build();
  }
}
