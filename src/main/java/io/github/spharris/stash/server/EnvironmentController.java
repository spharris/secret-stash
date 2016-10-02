package io.github.spharris.stash.server;

import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.common.collect.ImmutableList;

import io.github.spharris.stash.Environment;
import io.github.spharris.stash.service.EnvironmentService;
import io.github.spharris.stash.service.request.CreateEnvironmentRequest;
import io.github.spharris.stash.service.request.DeleteEnvironmentRequest;
import io.github.spharris.stash.service.request.GetEnvironmentRequest;
import io.github.spharris.stash.service.request.ListEnvironmentsRequest;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public final class EnvironmentController {
  
  private final EnvironmentService environmentService;
  
  @Inject
  EnvironmentController(EnvironmentService environmentService) {
    this.environmentService = environmentService;
  }
  
  @GET
  @Path("/projects/{projectId}/environments")
  public Response<ImmutableList<Environment>> listEnvironments(
      @PathParam("projectId") String projectId) {
    return Response.of(environmentService.listEnvironments(ListEnvironmentsRequest.builder()
      .setProjectId(projectId)
      .build()));
  }
  
  @PUT
  @Path("/projects/{projectId}/environments")
  public Response<Environment> createEnvironment(
      @PathParam("projectId") String projectId,
      Environment environment) {
    return Response.of(environmentService.createEnvironment(CreateEnvironmentRequest.builder()
      .setProjectId(projectId)
      .setEnvironment(environment)
      .build()));
  }
   
  @GET
  @Path("/projects/{projectId}/environments/{environmentId}")
  public Response<Environment> getEnvironment(
      @PathParam("projectId") String projectId,
      @PathParam("environmentId") String environmentId) {
    Optional<Environment> environment = environmentService.getEnvironment(GetEnvironmentRequest.builder()
      .setProjectId(projectId)
      .setEnvironmentId(environmentId)
      .build());
    
    if (!environment.isPresent()) {
      throw new NotFoundException();
    } else {
      return Response.of(environment.get());
    }
  }
  
  @PUT
  @Path("/projects/{projectId}/environments/{environmentId}")
  public Response<Environment> updateEnvironment(
      @PathParam("projectId") String projectId,
      @PathParam("environmentId") String environmentId,
      Environment environment) {
    return Response.of();
  }
  
  @DELETE
  @Path("/projects/{projectId}/environments/{environmentId}")
  public Response<Void> deleteEnvironment(
      @PathParam("projectId") String projectId,
      @PathParam("environmentId") String environmentId) {
    environmentService.deleteEnvironment(DeleteEnvironmentRequest.builder()
      .setProjectId(projectId)
      .setEnvironmentId(environmentId)
      .build());
    
    return Response.of();
  }
}
