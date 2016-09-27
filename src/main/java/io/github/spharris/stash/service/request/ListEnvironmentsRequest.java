package io.github.spharris.stash.service.request;

import javax.annotation.Nullable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ListEnvironmentsRequest {
  
  ListEnvironmentsRequest() {}
  
  public abstract String getProjectId();
  public abstract @Nullable String getContinuationToken();
  
  public abstract Builder toBuilder();
  public static Builder builder() {
    return new AutoValue_ListEnvironmentsRequest.Builder();
  }
  
  @AutoValue.Builder
  public abstract static class Builder {
    
    public abstract Builder setProjectId(String projectId);
    public abstract Builder setContinuationToken(String continuationToken);
    
    public abstract ListEnvironmentsRequest build();
  }
}
