 package io.github.spharris.stash.service;

import java.util.stream.Collectors;

import javax.inject.Inject;

import com.amazonaws.auth.policy.Statement.Effect;
import com.amazonaws.auth.policy.actions.S3Actions;
import com.amazonaws.services.identitymanagement.model.DeletePolicyRequest;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.BucketPolicy;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

import io.github.spharris.stash.Environment;
import io.github.spharris.stash.service.Annotations.BucketOfSecrets;
import io.github.spharris.stash.service.Annotations.PolicyPrefix;
import io.github.spharris.stash.service.aws.Policy;
import io.github.spharris.stash.service.aws.Statement;
import io.github.spharris.stash.service.request.UpdatePolicyRequest;
import io.github.spharris.stash.service.utils.JsonUtil;
import io.github.spharris.stash.service.utils.ObjectNameUtil;

public class PolicyServiceImpl implements PolicyService {
  
  private static final String AWS_ENTITY = "AWS";
  private static final ImmutableSet<S3Actions> ACTIONS = ImmutableSet.of(S3Actions.GetObject);
  
  private final String bucketName;
  private final String policyPrefix;
  private final AmazonS3 s3client;
  private final JsonUtil json;
  
  @Inject
  PolicyServiceImpl(@BucketOfSecrets String bucketName, @PolicyPrefix String policyPrefix,
      AmazonS3 s3client, JsonUtil json) {
    this.bucketName = bucketName;
    this.policyPrefix = policyPrefix;
    this.s3client = s3client;
    this.json = json;
  }

  @Override
  public synchronized Policy updateEnvironmentPolicy(UpdatePolicyRequest request) {
    BucketPolicy bucketPolicy = s3client.getBucketPolicy(bucketName);

    ImmutableList.Builder<Statement> statementListBuilder = ImmutableList.builder();
    Policy.Builder policyBuilder;
    if (bucketPolicy.getPolicyText() == null) {
      policyBuilder = createPolicyTemplate();
    } else {
      Policy currentPolicy = json.fromString(bucketPolicy.getPolicyText(), Policy.class);
      policyBuilder = currentPolicy.toBuilder();
      
      statementListBuilder.addAll(currentPolicy.getStatements().stream()
        .filter((statement) -> {
          // Filter out the statement for the environment we're updating.
          return Objects.equal(statement.getId(), getStatementId(
            request.getProjectId(), request.getEnvironment().getEnvironmentId()));
        })
        .collect(Collectors.toList()));
    }
    
    Policy policy = policyBuilder
        .setStatements(statementListBuilder
          .add(getEnvironmentStatement(request.getProjectId(), request.getEnvironment()))
          .build())
        .build();
    s3client.setBucketPolicy(bucketName, json.toString(policy));
    
    return policy;
  }

  @Override
  public synchronized void deleteEnvironmentPolicy(DeletePolicyRequest request) {
    // TODO Auto-generated method stub
    
  }
  
  private Statement getEnvironmentStatement(String projectId, Environment environment) {
    return Statement.builder()
        .setId(getStatementId(projectId, environment.getEnvironmentId()))
        .setActions(ACTIONS)
        .setEffect(Effect.Allow)
        .setResources(
          ObjectNameUtil.createEnvironmentResource(projectId, environment.getEnvironmentId()))
        .setPrincipals(ImmutableMultimap.<String, String>builder()
          .putAll(AWS_ENTITY,
            Iterables.concat(environment.getAcl().getGroups(), environment.getAcl().getRoles()))
          .build())
        .build();
  }
  
  private String getStatementId(String projectId, String environmentId) {
    return String.format(
      "%s:%s", policyPrefix, ObjectNameUtil.createS3Path(projectId, environmentId));
  }
  
  private Policy.Builder createPolicyTemplate() {
    return Policy.builder()
        .setId(policyPrefix + ":" + PolicyService.POLICY_ID);
  }
}
