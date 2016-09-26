package io.github.spharris.stash.service.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import javax.inject.Inject;

import com.google.common.collect.ImmutableList;

import io.github.spharris.stash.Project;
import io.github.spharris.stash.service.request.CreateProjectRequest;
import io.github.spharris.stash.service.request.DeleteProjectRequest;
import io.github.spharris.stash.service.request.GetProjectRequest;
import io.github.spharris.stash.service.request.ListProjectsRequest;
import io.github.spharris.stash.service.request.UpdateProjectRequest;

public class ProjectDaoImpl implements ProjectDao {

  private final DatabaseService dbService;
  
  @Inject
  public ProjectDaoImpl(DatabaseService dbService) {
    this.dbService = dbService;
  }
  
  @Override
  public ImmutableList<Project> listProjects(ListProjectsRequest request) {
    try {
      Statement statement = dbService.getConnection().createStatement();
      ResultSet rs = statement.executeQuery("SELECT * FROM project;");
      
      ImmutableList.Builder<Project> builder = ImmutableList.builder();
      while(rs.next()) {
        builder.add(extractProject(rs));
      }
      
      return builder.build();
      
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Project createProject(CreateProjectRequest request) {
    Project project = request.getProject();
    try {
      PreparedStatement statement = dbService.getConnection().prepareStatement(
        "INSERT INTO project VALUES (?, ?);");
      statement.setString(1, project.getProjectId());
      statement.setString(2, project.getDescription());
      statement.execute();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    
    return request.getProject();
  }

  @Override
  public Optional<Project> getProject(GetProjectRequest request) {
    try {
      PreparedStatement statement = dbService.getConnection().prepareStatement(
        "SELECT * FROM project WHERE project_id = ?;");
      statement.setString(1, request.getProjectId());
      statement.execute();
      
      ResultSet rs = statement.getResultSet();
      if (!rs.isBeforeFirst()) {
        return Optional.empty();
      }
      
      return Optional.of(extractProject(rs));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Project updateProject(UpdateProjectRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void deleteProject(DeleteProjectRequest request) {
    try {
      PreparedStatement statement = dbService.getConnection().prepareStatement(
        "DELETE FROM project WHERE project_id = ?;");
      statement.setString(1, request.getProjectId());
      statement.execute();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
  
  private static Project extractProject(ResultSet rs) throws SQLException {
    return Project.builder()
        .setProjectId(rs.getString("project_id"))
        .setDescription(rs.getString("description"))
        .build();
  }
}
