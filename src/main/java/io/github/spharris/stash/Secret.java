package io.github.spharris.stash;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.auto.value.AutoValue;

@AutoValue
@JsonDeserialize(builder = AutoValue_Environment.Builder.class)
public abstract class Secret {
  
  Secret() {}
  
  public abstract String getSecretId();
  public abstract @Nullable String getDescription();
  public abstract @Nullable String getSecretValue();
  
  public abstract Builder toBuilder();
  public static Builder builder() {
    return new AutoValue_Secret.Builder();
  }
  
  @AutoValue.Builder
  @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "set")
  public abstract static class Builder {
    public abstract Builder setSecretId(String name);
    public abstract Builder setDescription(String description);
    public abstract Builder setSecretValue(String secretValue);
    
    public abstract Secret build();
  }
}
