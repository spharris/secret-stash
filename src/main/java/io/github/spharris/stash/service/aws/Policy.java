package io.github.spharris.stash.service.aws;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

/**
 * Class representing an AWS policy. Duplicated here to make serialization and comparison simpler
 */
@AutoValue
@JsonDeserialize(builder = AutoValue_Policy.Builder.class)
public abstract class Policy {

  public static final String DEFAULT_VERSION = "2012-10-17";
  
  @JsonProperty("Id") public abstract @Nullable String getId();
  @JsonProperty("Version") public abstract @Nullable String getVersion(); 
  @JsonProperty("Statement") public abstract @Nullable ImmutableList<Statement> getStatements();
  
  public abstract Builder toBuilder();
  public static Builder builder() {
    return new AutoValue_Policy.Builder()
        .setVersion(DEFAULT_VERSION);
  }
  
  @AutoValue.Builder
  @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "set")
  public abstract static class Builder {
    
    public abstract Builder setStatements(Statement...statements);
    
    @JsonProperty("Id") public abstract Builder setId(String id);
    @JsonProperty("Version") public abstract Builder setVersion(String version);
    
    @JsonProperty("Statement")
    public abstract Builder setStatements(ImmutableList<Statement> statements);
    
    public abstract Policy build();
  }
}
