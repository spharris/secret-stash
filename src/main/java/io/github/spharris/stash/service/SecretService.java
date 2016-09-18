package io.github.spharris.stash.service;

import com.google.common.collect.ImmutableList;

import io.github.spharris.stash.Secret;
import io.github.spharris.stash.service.request.CreateSecretRequest;
import io.github.spharris.stash.service.request.DeleteSecretRequest;
import io.github.spharris.stash.service.request.GetSecretRequest;
import io.github.spharris.stash.service.request.ListSecretsRequest;
import io.github.spharris.stash.service.request.UpdateSecretRequest;

public interface SecretService {

  ImmutableList<Secret> listSecrets(ListSecretsRequest request);
  Secret createSecret(CreateSecretRequest request);
  Secret getSecret(GetSecretRequest request);
  Secret updateSecret(UpdateSecretRequest request);
  void deleteSecret(DeleteSecretRequest request);
}
