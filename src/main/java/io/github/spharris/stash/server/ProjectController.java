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

import io.github.spharris.stash.Project;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public final class ProjectController {
  
  @GET
  @Path("/projects")
  public Response<ImmutableList<Project>> listProjects() {
    return Response.<ImmutableList<Project>>builder().build();
  }
  
  @PUT
  @Path("/projects")
  public Response<Project> createProject(Project secret) {
    return Response.<Project>builder().build();
  }
  
  @GET
  @Path("/projects/{projectId}")
  public Response<Project> getProject(
      @PathParam("projectId") String projectId) {
    return Response.<Project>builder().build();
  }
  
  @PUT
  @Path("/projects/{projectId}")
  public Response<Project> updateProject(
      @PathParam("projectId") String projectId, Project secret) {
    return Response.<Project>builder().build();
  }
  
  @DELETE
  @Path("/projects/{projectId}")
  public Response<Void> deleteProject(
      @PathParam("projectId") String projectId) {
    return Response.<Void>builder().build();
  }
}
