package io.github.spharris.stash.service;

import com.amazonaws.services.identitymanagement.model.DeletePolicyRequest;

import io.github.spharris.stash.service.request.UpdatePolicyRequest;

public interface PolicyService {
  
  void updatePolicy(UpdatePolicyRequest request);
  void deletePolicy(DeletePolicyRequest request); 
}
