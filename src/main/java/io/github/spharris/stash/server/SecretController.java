package io.github.spharris.stash.server;

import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.common.collect.ImmutableList;

import io.github.spharris.stash.Secret;
import io.github.spharris.stash.service.SecretService;
import io.github.spharris.stash.service.request.CreateSecretRequest;
import io.github.spharris.stash.service.request.DeleteSecretRequest;
import io.github.spharris.stash.service.request.GetSecretRequest;
import io.github.spharris.stash.service.request.ListSecretsRequest;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public final class SecretController {
  
  private final SecretService secretService;
  
  @Inject
  SecretController(SecretService secretService) {
    this.secretService = secretService;
  }
  
  @GET
  @Path("/projects/{projectId}/environments/{environmentId}/secrets")
  public Response<ImmutableList<Secret>> listSecrets(
      @PathParam("projectId") String projectId,
      @PathParam("environmentId") String environmentId) {
    return Response.<ImmutableList<Secret>>builder()
        .setValue(secretService.listSecrets(ListSecretsRequest.builder()
          .setProjectId(projectId)
          .setEnvironmentId(environmentId)
          .build()))
        .build();
  }
  
  @PUT
  @Path("/projects/{projectId}/environments/{environmentId}/secrets")
  public Response<Secret> createSecret(
      @PathParam("projectId") String projectId,
      @PathParam("environmentId") String environmentId,
      Secret secret) {
    return Response.<Secret>builder()
        .setValue(secretService.createSecret(CreateSecretRequest.builder()
          .setProjectId(projectId)
          .setEnvironmentId(environmentId)
          .setSecret(secret)
          .build()))
        .build();
  }
  
  @GET
  @Path("/projects/{projectId}/environments/{environmentId}/secrets/{secretId}")
  public Response<Secret> getSecret(
      @PathParam("projectId") String projectId,
      @PathParam("environmentId") String environmentId,
      @PathParam("secretId") String secretId,
      @DefaultValue("false") @QueryParam("includeSecretValue") boolean includeSecretValue) {
    
    Optional<Secret> secret = secretService.getSecret(GetSecretRequest.builder()
          .setProjectId(projectId)
          .setEnvironmentId(environmentId)
          .setSecretId(secretId)
          .setIncludeSecretValue(includeSecretValue)
          .build());
    
    if (!secret.isPresent()) {
      throw new NotFoundException();
    }
    
    return Response.<Secret>builder()
        .setValue(secret.get())
        .build();
  }
  
  @PUT
  @Path("/projects/{projectId}/environments/{environmentId}/secrets/{secretId}")
  public Response<Secret> updateSecret(
      @PathParam("projectId") String projectId,
      @PathParam("environmentId") String environmentId,
      @PathParam("secretId") String secretId,
      Secret secret) {
    return Response.<Secret>builder().build();
  }
  
  @DELETE
  @Path("/projects/{projectId}/environments/{environmentId}/secrets/{secretId}")
  public Response<Void> deleteSecret(
      @PathParam("projectId") String projectId,
      @PathParam("environmentId") String environmentId,
      @PathParam("secretId") String secretId) {
    secretService.deleteSecret(DeleteSecretRequest.builder()
      .setProjectId(projectId)
      .setEnvironmentId(environmentId)
      .setSecretId(secretId)
      .build());

    return Response.<Void>builder().build();
  }
}
