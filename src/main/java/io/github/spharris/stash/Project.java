package io.github.spharris.stash;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Project {

  Project() {}
  
  public abstract String getProjectId();
  public abstract String getDescription();
  
  public static Builder builder() {
    return new AutoValue_Project.Builder();
  }
  
  @AutoValue.Builder
  public abstract static class Builder {
    
    public abstract Builder setProjectId(String projectId);
    public abstract Builder setDescription(String projectDescription);
      
    public abstract Project build();
  }
}
