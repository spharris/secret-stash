package io.github.spharris.stash.service.aws;

import javax.annotation.Nullable;

import com.amazonaws.auth.policy.Statement.Effect;
import com.amazonaws.auth.policy.actions.S3Actions;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;

@AutoValue
@JsonDeserialize(builder = AutoValue_Statement.Builder.class)
public abstract class Statement {

  @JsonProperty("Id") public abstract @Nullable String getId();
  @JsonProperty("Effect") public abstract @Nullable Effect getEffect(); 
  @JsonProperty("Action") public abstract @Nullable ImmutableList<S3Actions> getActions();
  @JsonProperty("Resource") public abstract @Nullable ImmutableList<String> getResources();
  
  @JsonProperty("Principal")
  public abstract @Nullable ImmutableMultimap<String, String> getPrincipals();
  
  @JsonProperty("Condition")
  public abstract @Nullable ImmutableMap<String, ImmutableMultimap<String, String>> getConditions();
  
  public abstract Builder toBuilder();
  public static Builder builder() {
    return new AutoValue_Statement.Builder();
  }
  
  @AutoValue.Builder
  @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "set")
  public abstract static class Builder {

    public abstract Builder setActions(S3Actions... action);
    public abstract Builder setResources(String... resources);
    
    @JsonProperty("Id") public abstract Builder setId(String Id);
    @JsonProperty("Effect") public abstract Builder setEffect(Effect effect); 
    @JsonProperty("Action") public abstract Builder setActions(ImmutableList<S3Actions> action);
    @JsonProperty("Resource") public abstract Builder setResources(ImmutableList<String> resources);

    @JsonProperty("Principal")
    public abstract @Nullable Builder setPrincipals(ImmutableMultimap<String, String> setPrincipals);

    @JsonProperty("Condition")
    public abstract Builder setConditions(
        ImmutableMap<String, ImmutableMultimap<String, String>> conditions);

    public abstract Statement build();
  }
}
