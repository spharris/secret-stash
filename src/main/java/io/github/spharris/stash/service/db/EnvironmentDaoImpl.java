package io.github.spharris.stash.service.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import javax.inject.Inject;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;

import io.github.spharris.stash.AccessControlList;
import io.github.spharris.stash.Environment;
import io.github.spharris.stash.service.db.exceptions.UnexpectedSqlException;
import io.github.spharris.stash.service.db.exceptions.UniquenessConstraintException;
import io.github.spharris.stash.service.request.CreateEnvironmentRequest;
import io.github.spharris.stash.service.request.DeleteEnvironmentRequest;
import io.github.spharris.stash.service.request.GetEnvironmentRequest;
import io.github.spharris.stash.service.request.ListEnvironmentsRequest;
import io.github.spharris.stash.service.request.UpdateEnvironmentRequest;

public class EnvironmentDaoImpl implements EnvironmentDao {

  private final DatabaseService dbService;
  
  @Inject
  EnvironmentDaoImpl(DatabaseService dbService) {
    this.dbService = dbService;
  }
  
  @Override
  public ImmutableList<Environment> listEnvironments(ListEnvironmentsRequest request) {
    try {
      PreparedStatement statement = dbService.getConnection().prepareStatement(
          "SELECT * FROM environment where project_id = ?;");
      statement.setString(1, request.getProjectId());
      
      ResultSet rs = statement.executeQuery(); 
      ImmutableList.Builder<Environment> builder = ImmutableList.builder();
      while(rs.next()) {
        builder.add(extractEnvironment(rs));
      }
      
      return builder.build();
      
    } catch (SQLException e) {
      throw new UnexpectedSqlException(e);
    }
  }

  @Override
  public Environment createEnvironment(CreateEnvironmentRequest request) {
    Environment environment = request.getEnvironment();
    try {
      PreparedStatement statement = dbService.getConnection().prepareStatement(
        "INSERT INTO environment (project_id, environment_id, description, policy_arn, roles,"
        + "groups) VALUES (?, ?, ?, ?, ?, ?);");
      statement.setString(1, request.getProjectId());
      statement.setString(2, environment.getEnvironmentId());
      statement.setString(3, environment.getDescription());
      statement.setString(4, environment.getAcl().getPolicyArn());
      statement.setString(5, joinPerms(environment.getAcl().getRoles()));
      statement.setString(6, joinPerms(environment.getAcl().getGroups()));
      statement.execute();
    } catch (SQLException e) {
      handleCreationException(e);
    }
    
    return environment;
  }

  @Override
  public Optional<Environment> getEnvironment(GetEnvironmentRequest request) {
    try {
      PreparedStatement statement = dbService.getConnection().prepareStatement(
        "SELECT * FROM environment WHERE project_id = ? AND environment_id = ?;");
      statement.setString(1, request.getProjectId());
      statement.setString(2, request.getEnvironmentId());
      statement.execute();
      
      ResultSet rs = statement.getResultSet();
      if (!rs.isBeforeFirst()) {
        return Optional.empty();
      }
      
      return Optional.of(extractEnvironment(rs));
    } catch (SQLException e) {
      throw new UnexpectedSqlException(e);
    }
  }

  @Override
  public Environment updateEnvironment(UpdateEnvironmentRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void deleteEnvironment(DeleteEnvironmentRequest request) {
    try {
      PreparedStatement statement = dbService.getConnection().prepareStatement(
        "DELETE FROM environment WHERE project_id = ? AND environment_id = ?;");
      statement.setString(1, request.getProjectId());
      statement.setString(2, request.getEnvironmentId());
      statement.execute();
    } catch (SQLException e) {
      throw new UnexpectedSqlException(e);
    }
  }

  private static Environment extractEnvironment(ResultSet rs) throws SQLException {
    return Environment.builder()
        .setEnvironmentId(rs.getString("environment_id"))
        .setDescription(rs.getString("description"))
        .setAcl(AccessControlList.builder()
          .setPolicyArn(rs.getString("policy_arn"))
          .setRoles(splitPerms(rs.getString("roles")))
          .setGroups(splitPerms(rs.getString("groups")))
          .build())
        .build();
  }

  private static void handleCreationException(SQLException e) {
    switch(SqliteErrorUtil.getExceptionType(e)) {
      case FOREIGN_KEY:
        throw new IllegalStateException("The specified project does not exist.", e);
      case UNIQUENESS:
        throw new UniquenessConstraintException("The specified environmentId already exists.", e);
      default:
        throw new UnexpectedSqlException(e);
    }
  }
  
  private static ImmutableList<String> splitPerms(String perms) {
    return ImmutableList.copyOf(Splitter.on(";").omitEmptyStrings().split(perms));
  }
  
  private static String joinPerms(Iterable<String> perms) {
    return Joiner.on(";").join(perms);
  }
}
