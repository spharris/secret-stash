package io.github.spharris.stash;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

@AutoValue
@JsonDeserialize(builder = AutoValue_AccessControlList.Builder.class)
public abstract class AccessControlList {
  
  AccessControlList() {}
  
  public abstract ImmutableList<String> getRoles();
  public abstract ImmutableList<String> getGroups();
  
  public abstract Builder toBuilder();
  public static Builder builder() {
    return new AutoValue_AccessControlList.Builder()
        .setRoles(ImmutableList.of())
        .setGroups(ImmutableList.of());
  }
  
  @AutoValue.Builder
  @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "set")
  public abstract static class Builder {
    
    public abstract Builder setRoles(String... roles);
    public abstract Builder setGroups(String... groups);
    
    @JsonProperty("roles") public abstract Builder setRoles(Iterable<String> roles);
    @JsonProperty("groups") public abstract Builder setGroups(Iterable<String> groups);
    
    public abstract AccessControlList build();
  }
}
