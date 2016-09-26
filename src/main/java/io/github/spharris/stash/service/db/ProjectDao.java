package io.github.spharris.stash.service.db;

import com.google.common.collect.ImmutableList;

import io.github.spharris.stash.Project;
import io.github.spharris.stash.service.request.CreateProjectRequest;
import io.github.spharris.stash.service.request.DeleteProjectRequest;
import io.github.spharris.stash.service.request.GetProjectRequest;
import io.github.spharris.stash.service.request.ListProjectsRequest;
import io.github.spharris.stash.service.request.UpdateProjectRequest;

public interface ProjectDao {

  ImmutableList<Project> listProjects(ListProjectsRequest request);
  Project createProject(CreateProjectRequest request);
  Project getProject(GetProjectRequest request);
  Project updateProject(UpdateProjectRequest request);
  void deleteProject(DeleteProjectRequest request);
}
