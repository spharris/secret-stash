package io.github.spharris.stash;

import com.amazonaws.services.identitymanagement.model.Group;
import com.amazonaws.services.identitymanagement.model.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

@AutoValue
@JsonDeserialize(builder = AutoValue_AccessControlList.Builder.class)
public abstract class AccessControlList {
  
  AccessControlList() {}
  
  public abstract ImmutableList<Role> getRoles();
  public abstract ImmutableList<Group> getGroups();
  
  public abstract AccessControlList.Builder toBuilder();
  public static Builder builder() {
    return new AutoValue_AccessControlList.Builder()
        .setRoles(ImmutableList.of())
        .setGroups(ImmutableList.of());
  }
  
  @AutoValue.Builder
  @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "set")
  public abstract static class Builder {
    
    public abstract Builder setRoles(Role... roles);
    public abstract Builder setGroups(Group... groups);
    
    @JsonProperty("roles") public abstract Builder setRoles(ImmutableList<Role> roles);
    @JsonProperty("groups") public abstract Builder setGroups(ImmutableList<Group> groups);
    
    public abstract AccessControlList build();
  }
}
