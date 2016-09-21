package io.github.spharris.stash.service;

import javax.inject.Inject;

import com.amazonaws.auth.policy.Policy;
import com.amazonaws.services.identitymanagement.model.DeletePolicyRequest;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.BucketPolicy;

import io.github.spharris.stash.service.Annotations.BucketOfSecrets;
import io.github.spharris.stash.service.Annotations.PolicyPrefix;
import io.github.spharris.stash.service.request.UpdatePolicyRequest;

public class PolicyServiceImpl implements PolicyService {
  
  private final String bucketName;
  private final String policyPrefix;
  private final AmazonS3 s3client;
  
  @Inject
  PolicyServiceImpl(@BucketOfSecrets String bucketName, @PolicyPrefix String policyPrefix,
      AmazonS3 s3client) {
    this.bucketName = bucketName;
    this.policyPrefix = policyPrefix;
    this.s3client = s3client;
  }

  @Override
  public synchronized void updatePolicy(UpdatePolicyRequest request) {
    BucketPolicy currentPolicy = s3client.getBucketPolicy(bucketName);
    Policy policy; 
    if (currentPolicy.getPolicyText() == null) {
      policy = createPolicyTemplate();
    } else {
      policy = Policy.fromJson(currentPolicy.getPolicyText());
    }
    
    s3client.setBucketPolicy(bucketName, policy.toJson());
  }

  @Override
  public synchronized void deletePolicy(DeletePolicyRequest request) {
    // TODO Auto-generated method stub
    
  }
  
  private Policy createPolicyTemplate() {
    Policy policy = new Policy(policyPrefix + ":" + PolicyService.POLICY_ID);
    
    return policy;
  }
}
