package io.github.spharris.stash.service;

import io.github.spharris.stash.service.aws.Policy;
import io.github.spharris.stash.service.request.DeleteEnvironmentPolicyRequest;
import io.github.spharris.stash.service.request.CreateEnvironmentPolicyRequest;

public interface PolicyService {
  
  Policy createEnvironmentPolicy(CreateEnvironmentPolicyRequest request);
  void deleteEnvironmentPolicy(DeleteEnvironmentPolicyRequest request); 
}
