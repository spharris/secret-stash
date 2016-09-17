package io.github.spharris.stash;

import javax.annotation.Nullable;

import com.amazonaws.services.identitymanagement.model.Group;
import com.amazonaws.services.identitymanagement.model.Role;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

@AutoValue
public abstract class Secret {
  public abstract @Nullable String getName();
  public abstract @Nullable String getProject();
  public abstract @Nullable String getEnvironment();
  public abstract @Nullable String getDescription();
  public abstract @Nullable String getSecretValue();
  public abstract ImmutableList<Role> getRoles();
  public abstract ImmutableList<Group> getGroups();
  
  public abstract Secret.Builder toBuilder();
  public static Builder builder() {
    return new AutoValue_Secret.Builder()
        .setRoles(ImmutableList.of())
        .setGroups(ImmutableList.of());
  }
  
  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setName(String name);
    public abstract Builder setProject(String project);
    public abstract Builder setEnvironment(String environment);
    public abstract Builder setDescription(String description);
    public abstract Builder setSecretValue(String secretValue);
    public abstract Builder setRoles(ImmutableList<Role> roles);
    public abstract Builder setRoles(Role... roles);
    public abstract Builder setGroups(ImmutableList<Group> groups);
    public abstract Builder setGroups(Group... groups);
    
    public abstract Secret build();
  }
}
