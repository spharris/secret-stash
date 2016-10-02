package io.github.spharris.stash.server;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.model.Group;
import com.amazonaws.services.identitymanagement.model.ListGroupsResult;
import com.amazonaws.services.identitymanagement.model.ListRolesResult;
import com.amazonaws.services.identitymanagement.model.Role;
import com.google.common.collect.ImmutableList;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class AwsController {

  private final AmazonIdentityManagement iamClient;
  
  @Inject
  AwsController(AmazonIdentityManagement iamClient) {
    this.iamClient = iamClient;
  }
  
  @GET
  @Path("/aws/roles")
  public Response<ImmutableList<String>> listRoles() {
    ListRolesResult result = iamClient.listRoles();
    
    ImmutableList.Builder<String> builder = ImmutableList.builder();
    for (Role role : result.getRoles()) {
      builder.add(role.getRoleName());
    }
    
    return Response.of(builder.build());
  }
  
  @GET
  @Path("/aws/groups")
  public Response<ImmutableList<String>> listGroups() {
    ListGroupsResult result = iamClient.listGroups();
    
    ImmutableList.Builder<String> builder = ImmutableList.builder();
    for (Group group : result.getGroups()) {
      builder.add(group.getGroupName());
    }
    
    return Response.of(builder.build());
  }
}
