package io.github.spharris.stash.service.request;

import com.google.auto.value.AutoValue;

import io.github.spharris.stash.Project;

@AutoValue
public abstract class CreateProjectRequest {
  
  public abstract Project getProject();
  
  public static Builder builder() {
    return new AutoValue_CreateProjectRequest.Builder();
  }
  
  @AutoValue.Builder
  public abstract static class Builder {
    
    public abstract Builder setProject(Project project);
    
    public abstract CreateProjectRequest build();
  }
}
