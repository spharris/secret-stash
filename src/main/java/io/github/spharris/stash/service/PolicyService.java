package io.github.spharris.stash.service;

import io.github.spharris.stash.service.aws.Policy;
import io.github.spharris.stash.service.request.DeleteEnvironmentPolicyRequest;
import io.github.spharris.stash.service.request.CreateEnvironmentPolicyRequest;

public interface PolicyService {
  
  /**
   * The name used if secret-stash has to generate a full policy rather than append to
   * an existing policy
   */
  public static final String POLICY_ID = "stash-generated-policy";
  
  Policy createEnvironmentPolicy(CreateEnvironmentPolicyRequest request);
  void deleteEnvironmentPolicy(DeleteEnvironmentPolicyRequest request); 
}
