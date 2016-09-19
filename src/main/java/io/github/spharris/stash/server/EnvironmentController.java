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

import io.github.spharris.stash.Environment;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public final class EnvironmentController {
  
  @GET
  @Path("/projects/{projectId}/environments")
  public Response<ImmutableList<Environment>> listEnvironments(
      @PathParam("projectId") String projectId) {
    return Response.<ImmutableList<Environment>>builder().build();
  }
  
  @PUT
  @Path("/projects/{projectId}/environments")
  public Response<Environment> createEnvironment(
      @PathParam("projectId") String projectId,
      Environment environment) {
    return Response.<Environment>builder().build();
  }
  
  @GET
  @Path("/projects/{projectId}/environments/{environmentId}")
  public Response<Environment> getEnvironment(
      @PathParam("projectId") String projectId,
      @PathParam("environmentId") String environmentId) {
    return Response.<Environment>builder().build();
  }
  
  @PUT
  @Path("/projects/{projectId}/environments/{environmentId}")
  public Response<Environment> updateEnvironment(
      @PathParam("projectId") String projectId,
      @PathParam("environmentId") String environmentId,
      Environment environment) {
    return Response.<Environment>builder().build();
  }
  
  @DELETE
  @Path("/projects/{projectId}/environments/{environmentId}")
  public Response<Void> deleteEnvironment(
      @PathParam("projectId") String projectId,
      @PathParam("environmentId") String environmentId) {
    return Response.<Void>builder().build();
  }
}