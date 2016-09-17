package io.github.spharris.stash.server;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.github.spharris.stash.Environment;
import io.github.spharris.stash.service.CreateEnvironmentRequest;

@Path("/")
public class EnvironmentController {
  
  @PUT
  @Path("{projectId}")
  @Produces(MediaType.APPLICATION_JSON)
  public Environment createEnvironment(CreateEnvironmentRequest request) {
    return null;
  }
}
