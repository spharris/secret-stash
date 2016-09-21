package io.github.spharris.stash.service;

import com.amazonaws.services.identitymanagement.model.DeletePolicyRequest;

import io.github.spharris.stash.service.request.UpdatePolicyRequest;

public interface PolicyService {
  
  /**
   * The name used if secret-stash has to generate a full policy rather than append to
   * an existing policy
   */
  public static final String POLICY_ID = "stash-generated-policy";
  
  void updatePolicy(UpdatePolicyRequest request);
  void deletePolicy(DeletePolicyRequest request); 
}
