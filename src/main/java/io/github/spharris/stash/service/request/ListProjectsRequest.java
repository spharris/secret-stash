package io.github.spharris.stash.service.request;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ListProjectsRequest {
  
  ListProjectsRequest() {}
  
  public abstract String getContinuationToken();
  
  public abstract Builder toBuilder();
  public static Builder builder() {
    return new AutoValue_ListProjectsRequest.Builder();
  }
  
  @AutoValue.Builder
  public abstract static class Builder {
    
    public abstract Builder setContinuationToken(String continuationToken);
    
    public abstract ListProjectsRequest build();
  }
}
