package io.github.spharris.stash.service;

import java.util.Optional;

import com.google.common.collect.ImmutableList;

import io.github.spharris.stash.Project;
import io.github.spharris.stash.service.request.CreateProjectRequest;
import io.github.spharris.stash.service.request.DeleteProjectRequest;
import io.github.spharris.stash.service.request.GetProjectRequest;
import io.github.spharris.stash.service.request.ListProjectsRequest;
import io.github.spharris.stash.service.request.UpdateProjectRequest;

public interface ProjectService {

  ImmutableList<Project> listProjects(ListProjectsRequest request);
  Project createProject(CreateProjectRequest request);
  Optional<Project> getProject(GetProjectRequest request);
  Project updateProject(UpdateProjectRequest request);
  void deleteProject(DeleteProjectRequest request);
}
