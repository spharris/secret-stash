package io.github.spharris.stash.server;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.common.collect.ImmutableList;

import io.github.spharris.stash.Secret;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public final class SecretController {
  
  @GET
  @Path("/projects/{projectId}/environments/{environmentId}/secrets")
  public Response<ImmutableList<Secret>> listSecrets(
      @PathParam("projectId") String projectId,
      @PathParam("environmentId") String environmentId) {
    return Response.<ImmutableList<Secret>>builder().build();
  }
  
  @PUT
  @Path("/projects/{projectId}/environments/{environmentId}/secrets")
  public Response<Secret> createSecret(
      @PathParam("projectId") String projectId,
      @PathParam("environmentId") String environmentId,
      Secret secret) {
    return Response.<Secret>builder().build();
  }
  
  @GET
  @Path("/projects/{projectId}/environments/{environmentId}/secrets/{secretId}")
  public Response<Secret> getSecret(
      @PathParam("projectId") String projectId,
      @PathParam("environmentId") String environmentId,
      @PathParam("secretId") String secretId) {
    return Response.<Secret>builder().build();
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
    return Response.<Void>builder().build();
  }
}
