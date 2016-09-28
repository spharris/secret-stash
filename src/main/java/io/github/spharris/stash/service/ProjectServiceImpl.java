package io.github.spharris.stash.service;

import java.util.Optional;

import javax.inject.Inject;

import com.google.common.collect.ImmutableList;

import io.github.spharris.stash.Environment;
import io.github.spharris.stash.Project;
import io.github.spharris.stash.service.db.ProjectDao;
import io.github.spharris.stash.service.request.CreateProjectRequest;
import io.github.spharris.stash.service.request.DeleteEnvironmentRequest;
import io.github.spharris.stash.service.request.DeleteProjectRequest;
import io.github.spharris.stash.service.request.GetProjectRequest;
import io.github.spharris.stash.service.request.ListEnvironmentsRequest;
import io.github.spharris.stash.service.request.ListProjectsRequest;
import io.github.spharris.stash.service.request.UpdateProjectRequest;

public class ProjectServiceImpl implements ProjectService {

  private final ProjectDao projectDao;
  private final EnvironmentService environmentService;
  
  @Inject
  ProjectServiceImpl(ProjectDao projectDao, EnvironmentService environmentService) {
    this.projectDao = projectDao;
    this.environmentService = environmentService;
  }
  
  @Override
  public ImmutableList<Project> listProjects(ListProjectsRequest request) {
    return projectDao.listProjects(request);
  }

  @Override
  public Project createProject(CreateProjectRequest request) {
    return projectDao.createProject(request);
  }

  @Override
  public Optional<Project> getProject(GetProjectRequest request) {
    return projectDao.getProject(request);
  }

  @Override
  public Project updateProject(UpdateProjectRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void deleteProject(DeleteProjectRequest request) {
    ImmutableList<Environment> environments = environmentService.listEnvironments(
      ListEnvironmentsRequest.builder()
        .setProjectId(request.getProjectId())
        .build());
    
    // To insure cleanup of any S3 data required; expect env service to clean up its
    // secrets
    for (Environment env : environments) {
      environmentService.deleteEnvironment(DeleteEnvironmentRequest.builder()
        .setProjectId(request.getProjectId())
        .setEnvironmentId(env.getEnvironmentId())
        .build());
    }
    
    projectDao.deleteProject(request);
  }
}
