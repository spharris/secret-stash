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

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class SecretController {
  
  @GET
  @Path("{projectId}/{environmentId}")
  public Response<ImmutableList<Secret>> listSecrets(
      @PathParam("projectId") String projectId,
      @PathParam("environmentId") String environmentId) {
    return Response.<ImmutableList<Secret>>builder().build();
  }
  
  @PUT
  @Path("{projectId}/{environmentId}")
  public Response<Secret> createSecret(
      @PathParam("projectId") String projectId,
      @PathParam("environmentId") String environmentId,
      Secret secret) {
    return Response.<Secret>builder().build();
  }
  
  @GET
  @Path("{projectId}/{environmentId}/{secretId}")
  public Response<Secret> getSecret(
      @PathParam("projectId") String projectId,
      @PathParam("environmentId") String environmentId,
      @PathParam("secretId") String secretId) {
    return Response.<Secret>builder().build();
  }
  
  @PUT
  @Path("{projectId}/{environmentId}/{secretId}")
  public Response<Secret> updateSecret(
      @PathParam("projectId") String projectId,
      @PathParam("environmentId") String environmentId,
      @PathParam("secretId") String secretId,
      Secret secret) {
    return Response.<Secret>builder().build();
  }
  
  @DELETE
  @Path("{projectId}/{environmentId}/{secretId}")
  public Response<Void> deleteSecret(
      @PathParam("projectId") String projectId,
      @PathParam("environmentId") String environmentId,
      @PathParam("secretId") String secretId) {
    return Response.<Void>builder().build();
  }
}
