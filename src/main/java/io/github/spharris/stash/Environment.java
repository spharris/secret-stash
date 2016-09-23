package io.github.spharris.stash;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.auto.value.AutoValue;

@AutoValue
@JsonDeserialize(builder = AutoValue_Environment.Builder.class)
public abstract class Environment {

  Environment() {}
  
  public abstract String getEnvironmentId();
  public abstract @Nullable String getDescription();
  public abstract AccessControlList getAcl();
  
  public abstract Builder toBuilder();
  public static Builder builder() {
    return new AutoValue_Environment.Builder();
  }
  
  @AutoValue.Builder
  @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "set")
  public abstract static class Builder {
    
    public abstract Builder setEnvironmentId(String environmentId);
    public abstract Builder setDescription(String description);
    public abstract Builder setAcl(AccessControlList acl);
    
    protected abstract AccessControlList getAcl();
    protected abstract Environment autoBuild();
    public Environment build() {
      // Do this here instead of in builder() above so that Jackson doesn't barf
      if (getAcl() == null) {
        setAcl(AccessControlList.builder().build());
      }
      return autoBuild();
    }
  }

}
