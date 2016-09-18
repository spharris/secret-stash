package io.github.spharris.stash.server.errors;

import com.google.auto.value.AutoValue;

/**
 * A general error class containing an error code and a description of the actual error.
 */
@AutoValue
public abstract class Error {
  
  Error() {}
  
  public abstract ErrorCode getErrorCode();
  public abstract String getDescription();
  
  public Builder builder() {
    return new AutoValue_Error.Builder();
  }
  
  @AutoValue.Builder
  public abstract static class Builder {
    
    public abstract Builder setErrorCode(ErrorCode errorCode);
    public abstract Builder setDescription(String description);
    
    public abstract Error build();
  }
}
