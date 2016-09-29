 package io.github.spharris.stash.service;

import java.util.Arrays;

import javax.inject.Inject;

import com.amazonaws.auth.policy.Statement.Effect;
import com.amazonaws.auth.policy.actions.S3Actions;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.model.AttachGroupPolicyRequest;
import com.amazonaws.services.identitymanagement.model.AttachRolePolicyRequest;
import com.amazonaws.services.identitymanagement.model.CreatePolicyRequest;
import com.amazonaws.services.identitymanagement.model.CreatePolicyResult;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;

import io.github.spharris.stash.service.Annotations.BucketOfSecrets;
import io.github.spharris.stash.service.Annotations.PolicyPath;
import io.github.spharris.stash.service.Annotations.PolicyPrefix;
import io.github.spharris.stash.service.aws.Policy;
import io.github.spharris.stash.service.aws.Statement;
import io.github.spharris.stash.service.request.DeleteEnvironmentPolicyRequest;
import io.github.spharris.stash.service.request.CreateEnvironmentPolicyRequest;
import io.github.spharris.stash.service.utils.JsonUtil;
import io.github.spharris.stash.service.utils.ObjectNameUtil;

public class PolicyServiceImpl implements PolicyService {
  
  private static final ImmutableSet<S3Actions> ACTIONS = ImmutableSet.of(S3Actions.GetObject);
  
  private final String bucketName;
  private final String policyPrefix;
  private final String policyPath;
  private final AmazonIdentityManagement iamClient;
  private final JsonUtil json;
  
  @Inject
  PolicyServiceImpl(@BucketOfSecrets String bucketName, @PolicyPath String policyPath, 
      @PolicyPrefix String policyPrefix, AmazonIdentityManagement iamClient, JsonUtil json) {
    this.bucketName = bucketName;
    this.policyPrefix = policyPrefix;
    this.policyPath = policyPath;
    this.iamClient = iamClient;
    this.json = json;
  }

  @Override
  public Policy createEnvironmentPolicy(CreateEnvironmentPolicyRequest request) {
    Policy policy = createPolicy(request.getProjectId(),
      request.getEnvironment().getEnvironmentId());
    
    CreatePolicyResult result = iamClient.createPolicy(new CreatePolicyRequest()
      .withPath(policyPath)
      .withPolicyName(createPolicyName(
        request.getProjectId(), request.getEnvironment().getEnvironmentId()))
      .withPolicyDocument(json.toString(policy)));
    
    for (String arn : request.getEnvironment().getAcl().getGroups()) { 
      iamClient.attachGroupPolicy(new AttachGroupPolicyRequest()
        .withGroupName(arn)
        .withPolicyArn(result.getPolicy().getArn()));
    }
    
    for (String arn : request.getEnvironment().getAcl().getRoles()) { 
      iamClient.attachRolePolicy(new AttachRolePolicyRequest()
        .withRoleName(arn)
        .withPolicyArn(result.getPolicy().getArn()));
    }
    
    return policy.toBuilder()
        .setArn(result.getPolicy().getArn())
        .build();
  }

  @Override
  public synchronized void deleteEnvironmentPolicy(DeleteEnvironmentPolicyRequest request) {
    // TODO Auto-generated method stub
    
  }
  
  private Policy createPolicy(String projectId, String environmentId) {
    return Policy.builder()
        .setStatements(Statement.builder()
          .setEffect(Effect.Allow)
          .setActions(ACTIONS)
          .setResources(
            ObjectNameUtil.createEnvironmentResource(bucketName, projectId, environmentId))
          .build())
        .build();
  }
  
  private String createPolicyName(String projectId, String environmentId) {
    return policyPrefix + Joiner.on("-").join(Arrays.asList(projectId, environmentId));
  }
}
