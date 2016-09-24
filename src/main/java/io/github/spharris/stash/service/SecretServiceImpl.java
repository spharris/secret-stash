package io.github.spharris.stash.service;

import javax.inject.Inject;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.google.common.collect.ImmutableList;

import io.github.spharris.stash.Secret;
import io.github.spharris.stash.service.Annotations.BucketOfSecrets;
import io.github.spharris.stash.service.request.CreateSecretRequest;
import io.github.spharris.stash.service.request.DeleteSecretRequest;
import io.github.spharris.stash.service.request.GetSecretRequest;
import io.github.spharris.stash.service.request.ListSecretsRequest;
import io.github.spharris.stash.service.request.UpdateSecretRequest;
import io.github.spharris.stash.service.utils.JsonUtil;
import io.github.spharris.stash.service.utils.ObjectNameUtil;

public class SecretServiceImpl implements SecretService {

  private final String bucketName;
  private final AmazonS3 s3client;
  private final JsonUtil json;
  
  @Inject
  SecretServiceImpl(
      @BucketOfSecrets String bucketName, AmazonS3 s3client, JsonUtil json) {
    this.bucketName = bucketName;
    this.s3client = s3client;
    this.json = json;
  }
  
  @Override
  public ImmutableList<Secret> listSecrets(ListSecretsRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Secret createSecret(CreateSecretRequest request) {
    // TODO(spharris): Case where project/environment don't exist
    ObjectMetadata meta = new ObjectMetadata();
    meta.setSSEAlgorithm(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION);
    
    s3client.putObject(bucketName, ObjectNameUtil.createS3Path(
      request.getProjectId(),
      request.getEnvironmentId(),
      request.getSecret().getSecretId()),
      json.toInputStream(request.getSecret()),
      meta);
    
    return request.getSecret();
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
