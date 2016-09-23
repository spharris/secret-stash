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

/**
 * Class representing an AWS policy statement. Duplicated here to make serialization and comparison
 * simpler
 */
@AutoValue
@JsonDeserialize(builder = AutoValue_Statement.Builder.class)
public abstract class Statement {

  @JsonProperty("Sid") public abstract @Nullable String getId();
  @JsonProperty("Action") public abstract @Nullable ImmutableList<S3Actions> getActions();
  @JsonProperty("Effect") public abstract @Nullable Effect getEffect(); 
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
    
    @JsonProperty("Sid") public abstract Builder setId(String Id);
    @JsonProperty("Action") public abstract Builder setActions(Iterable<S3Actions> action);
    @JsonProperty("Effect") public abstract Builder setEffect(Effect effect); 
    @JsonProperty("Resource") public abstract Builder setResources(Iterable<String> resources);

    @JsonProperty("Principal")
    @JsonDeserialize(using = PrincipalDeserializer.class)
    public abstract @Nullable Builder setPrincipals(ImmutableMultimap<String, String> setPrincipals);

    @JsonProperty("Condition")
    @JsonDeserialize(using = ConditionDeserializer.class)
    public abstract Builder setConditions(
        ImmutableMap<String, ImmutableMultimap<String, String>> conditions);

    public abstract Statement build();
  }
}
