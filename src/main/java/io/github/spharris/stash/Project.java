package io.github.spharris.stash;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.auto.value.AutoValue;

/**
 * Class representing a project for which secrets need to be stored 
 */
@AutoValue
@JsonDeserialize(builder = AutoValue_Project.Builder.class)
public abstract class Project {

  Project() {}
  
  public abstract String getProjectId();
  public abstract @Nullable String getDescription();
  
  public abstract Builder toBuilder();
  public static Builder builder() {
    return new AutoValue_Project.Builder();
  }
  
  @AutoValue.Builder
  @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "set")
  public abstract static class Builder {
    
    public abstract Builder setProjectId(String projectId);
    public abstract Builder setDescription(String projectDescription);
      
    public abstract Project build();
  }
}
