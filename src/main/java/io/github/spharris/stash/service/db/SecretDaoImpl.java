package io.github.spharris.stash.service.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import javax.inject.Inject;

import com.google.common.collect.ImmutableList;

import io.github.spharris.stash.Secret;
import io.github.spharris.stash.service.request.CreateSecretRequest;
import io.github.spharris.stash.service.request.DeleteSecretRequest;
import io.github.spharris.stash.service.request.GetSecretRequest;
import io.github.spharris.stash.service.request.ListSecretsRequest;
import io.github.spharris.stash.service.request.UpdateSecretRequest;

public class SecretDaoImpl implements SecretDao {

  private final DatabaseService dbService;
  
  @Inject
  SecretDaoImpl(DatabaseService dbService) {
    this.dbService = dbService;
  }
  
  @Override
  public ImmutableList<Secret> listSecrets(ListSecretsRequest request) {
    try {
      PreparedStatement statement = dbService.getConnection().prepareStatement(
          "SELECT * FROM secret WHERE project_id = ? AND environment_id = ?;");
      statement.setString(1, request.getProjectId());
      statement.setString(2, request.getEnvironmentId());
      
      ResultSet rs = statement.executeQuery(); 
      ImmutableList.Builder<Secret> builder = ImmutableList.builder();
      while(rs.next()) {
        builder.add(extractSecret(rs));
      }
      
      return builder.build();
      
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Secret createSecret(CreateSecretRequest request) {
    Secret secret = request.getSecret();
    try {
      PreparedStatement statement = dbService.getConnection().prepareStatement(
        "INSERT INTO secret (project_id, environment_id, secret_id, description)"
        + " VALUES (?, ?, ?, ?);");
      statement.setString(1, request.getProjectId());
      statement.setString(2, request.getEnvironmentId());
      statement.setString(3, secret.getSecretId());
      statement.setString(4, secret.getDescription());
      statement.execute();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    
    return secret;
  }

  @Override
  public Optional<Secret> getSecret(GetSecretRequest request) {
    try {
      PreparedStatement statement = dbService.getConnection().prepareStatement(
        "SELECT * FROM secret WHERE project_id = ? AND environment_id = ? AND secret_id = ?;");
      statement.setString(1, request.getProjectId());
      statement.setString(2, request.getEnvironmentId());
      statement.setString(3, request.getSecretId());
      statement.execute();
      
      ResultSet rs = statement.getResultSet();
      if (!rs.isBeforeFirst()) {
        return Optional.empty();
      }
      
      return Optional.of(extractSecret(rs));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Secret updateSecret(UpdateSecretRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void deleteSecret(DeleteSecretRequest request) {
    try {
      PreparedStatement statement = dbService.getConnection().prepareStatement(
        "DELETE FROM secret WHERE project_id = ? AND environment_id = ? AND secret_id = ?;");
      statement.setString(1, request.getProjectId());
      statement.setString(2, request.getEnvironmentId());
      statement.setString(3, request.getSecretId());
      statement.execute();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
  
  private static Secret extractSecret(ResultSet rs) throws SQLException {
    return Secret.builder()
        .setSecretId(rs.getString("secret_id"))
        .setDescription(rs.getString("description"))
        .build();
  }

}
