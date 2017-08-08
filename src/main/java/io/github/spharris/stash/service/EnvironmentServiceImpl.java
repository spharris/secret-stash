package io.github.spharris.stash.service;

import java.util.Optional;

import javax.inject.Inject;

import com.google.common.collect.ImmutableList;

import io.github.spharris.stash.Environment;
import io.github.spharris.stash.Secret;
import io.github.spharris.stash.service.aws.Policy;
import io.github.spharris.stash.service.db.EnvironmentDao;
import io.github.spharris.stash.service.request.CreateEnvironmentPolicyRequest;
import io.github.spharris.stash.service.request.CreateEnvironmentRequest;
import io.github.spharris.stash.service.request.DeleteEnvironmentPolicyRequest;
import io.github.spharris.stash.service.request.DeleteEnvironmentRequest;
import io.github.spharris.stash.service.request.DeleteSecretRequest;
import io.github.spharris.stash.service.request.GetEnvironmentRequest;
import io.github.spharris.stash.service.request.ListEnvironmentsRequest;
import io.github.spharris.stash.service.request.ListSecretsRequest;
import io.github.spharris.stash.service.request.UpdateEnvironmentRequest;

public class EnvironmentServiceImpl implements EnvironmentService {

  private final EnvironmentDao environmentDao;
  private final SecretService secretService;
  private final PolicyService policyService;
  
  @Inject
  EnvironmentServiceImpl(EnvironmentDao environmentDao, PolicyService policyService,
    SecretService secretService) {
    this.environmentDao = environmentDao;
    this.policyService = policyService;
    this.secretService = secretService;
  }
  
  @Override
  public ImmutableList<Environment> listEnvironments(ListEnvironmentsRequest request) {
    return environmentDao.listEnvironments(request);
  }
 
  @Override
  public Environment createEnvironment(CreateEnvironmentRequest request) {
    Environment environment = request.getEnvironment();
    
    // Do this first to make sure that groups are valid 
    Policy policy = policyService.createEnvironmentPolicy(CreateEnvironmentPolicyRequest.builder()
      .setProjectId(request.getProjectId())
      .setEnvironment(request.getEnvironment())
      .build());
    
    environment = environment.toBuilder()
        .setAcl(environment.getAcl().toBuilder()
          .setPolicyArn(policy.getArn())
          .build())
        .build();
    
    return environmentDao.createEnvironment(request.toBuilder()
      .setEnvironment(environment)
      .build());
  }

  @Override
  public Optional<Environment> getEnvironment(GetEnvironmentRequest request) {
    return environmentDao.getEnvironment(request);
  }

  @Override
  public Environment updateEnvironment(UpdateEnvironmentRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void deleteEnvironment(DeleteEnvironmentRequest request) {
    Optional<Environment> environment = getEnvironment(GetEnvironmentRequest.builder()
      .setProjectId(request.getProjectId())
      .setEnvironmentId(request.getEnvironmentId())
      .build());
    
    if (!environment.isPresent()) {
      return;
    }
    
    ImmutableList<Secret> secrets = secretService.listSecrets(ListSecretsRequest.builder()
      .setProjectId(request.getProjectId())
      .setEnvironmentId(request.getEnvironmentId())
      .build());
    
    // Clean up secrets individually so that the S3 files get deleted
    for (Secret s : secrets) {
      secretService.deleteSecret(DeleteSecretRequest.builder()
        .setProjectId(request.getProjectId())
        .setEnvironmentId(request.getEnvironmentId())
        .setSecretId(s.getSecretId())
        .build());
    }
    
    policyService.deleteEnvironmentPolicy(DeleteEnvironmentPolicyRequest.builder()
      .setProjectId(request.getProjectId())
      .setEnvironment(environment.get())
      .build());
    
    environmentDao.deleteEnvironment(request);
  }
}
