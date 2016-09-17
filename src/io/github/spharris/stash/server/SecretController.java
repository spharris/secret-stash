package io.github.spharris.stash.server;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.common.collect.ImmutableList;

import io.github.spharris.stash.Secret;
import io.github.spharris.stash.service.ListSecretsRequest;

@Path("/")
class SecretController {
  
  @GET
  @Path("{projectId}")
  @Produces(MediaType.APPLICATION_JSON)
  public ImmutableList<Secret> listSecrets(ListSecretsRequest request) {
    return ImmutableList.of();
  }
  
  @GET
  @Path("{projectId}/{environmentId}/{secretId}")
  @Produces(MediaType.APPLICATION_JSON)
  public Secret getSecret(@PathParam("projectId") String projectId,
      @PathParam("secretId") String secretId) {
    return null;
  }
}
