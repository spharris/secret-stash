package io.github.spharris.stash;

import javax.annotation.Nullable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Secret {
  
  Secret() {}
  
  public abstract String getSecretId();
  public abstract @Nullable String getDescription();
  public abstract @Nullable String getSecretValue();
  
  public abstract Secret.Builder toBuilder();
  public static Builder builder() {
    return new AutoValue_Secret.Builder();
  }
  
  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setSecretId(String name);
    public abstract Builder setDescription(String description);
    public abstract Builder setSecretValue(String secretValue);
    
    public abstract Secret build();
  }
}
