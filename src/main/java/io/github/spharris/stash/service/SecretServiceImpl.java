package io.github.spharris.stash.service;

import javax.inject.Inject;

import com.amazonaws.services.s3.AmazonS3;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;

import io.github.spharris.stash.Secret;
import io.github.spharris.stash.service.Annotations.BucketOfSecrets;
import io.github.spharris.stash.service.request.CreateSecretRequest;
import io.github.spharris.stash.service.request.DeleteSecretRequest;
import io.github.spharris.stash.service.request.GetSecretRequest;
import io.github.spharris.stash.service.request.ListSecretsRequest;
import io.github.spharris.stash.service.request.UpdateSecretRequest;

public class SecretServiceImpl implements SecretService {

  private final String bucketName;
  private final AmazonS3 s3client;
  private final ObjectMapper mapper;
  
  @Inject
  SecretServiceImpl(
      @BucketOfSecrets String bucketName, AmazonS3 s3client, ObjectMapper mapper) {
    this.bucketName = bucketName;
    this.s3client = s3client;
    this.mapper = mapper;
  }
  
  @Override
  public ImmutableList<Secret> listSecrets(ListSecretsRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Secret createSecret(CreateSecretRequest request) {
    // TODO(spharris): Case where project/environment don't exist
    Secret secret = request.getSecret();
    
    try {
      s3client.putObject(bucketName, ObjectNameUtil.createS3Path(
        request.getProjectId(),
        request.getEnvironmentId(),
        request.getSecret().getSecretId()),
        mapper.writeValueAsString(secret));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    
    return secret;
  }

  @Override
  public Secret getSecret(GetSecretRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Secret updateSecret(UpdateSecretRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void deleteSecret(DeleteSecretRequest request) {
    // TODO Auto-generated method stub

  }

}
