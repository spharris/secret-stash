package io.github.spharris.stash;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

@AutoValue
@JsonDeserialize(builder = AutoValue_AccessControlList.Builder.class)
public abstract class AccessControlList {
  
  AccessControlList() {}
  
  public abstract @Nullable ImmutableList<String> getRoles();
  public abstract @Nullable ImmutableList<String> getGroups();
  
  public abstract Builder toBuilder();
  public static Builder builder() {
    return new AutoValue_AccessControlList.Builder();
  }
  
  @AutoValue.Builder
  @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "set")
  public abstract static class Builder {
    
    public abstract Builder setRoles(String... roles);
    public abstract Builder setGroups(String... groups);
    
    @JsonProperty("roles") public abstract Builder setRoles(Iterable<String> roles);
    @JsonProperty("groups") public abstract Builder setGroups(Iterable<String> groups);
    
    protected abstract ImmutableList<String> getRoles();
    protected abstract ImmutableList<String> getGroups();
    protected abstract AccessControlList autoBuild();

    public AccessControlList build() {
      // So that Jackson doesn't barf
      if (getRoles() == null) {
        setRoles(ImmutableList.of());
      }
      
      if (getGroups() == null) {
        setGroups(ImmutableList.of());
      }
      
      return autoBuild();
    }
  }
}
