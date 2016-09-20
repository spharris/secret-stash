package io.github.spharris.stash.service;

import javax.inject.Inject;

import com.amazonaws.services.identitymanagement.model.DeletePolicyRequest;
import com.amazonaws.services.s3.AmazonS3;

import io.github.spharris.stash.service.Annotations.PolicyPrefix;
import io.github.spharris.stash.service.request.UpdatePolicyRequest;

public class PolicyServiceImpl implements PolicyService {

  private final String policyPrefix;
  private final AmazonS3 s3client;
  
  @Inject
  PolicyServiceImpl(@PolicyPrefix String policyPrefix, AmazonS3 s3client) {
    this.policyPrefix = policyPrefix;
    this.s3client = s3client;
  }

  @Override
  public void updatePolicy(UpdatePolicyRequest request) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void deletePolicy(DeletePolicyRequest request) {
    // TODO Auto-generated method stub
    
  }
}
