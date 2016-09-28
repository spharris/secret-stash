package io.github.spharris.stash.service.db;

import static com.google.common.truth.Truth.assertThat;

import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import io.github.spharris.stash.service.db.SqliteErrorUtil.ExceptionType;

@RunWith(JUnit4.class)
public class SqliteErrorUtilTest {
  
  @Test
  public void recognizesDuplicateError() {
    SQLException e = new SQLException("[SQLITE_CONSTRAINT]  Abort due to constraint violation"
        + " (UNIQUE constraint failed: environment.project_id, environment.environment_id)",
        null, 19);
    assertThat(SqliteErrorUtil.getExceptionType(e)).isEqualTo(ExceptionType.UNIQUENESS);
  }
  
  @Test
  public void recognizesForeignKeyError() {
    SQLException e = new SQLException("[SQLITE_CONSTRAINT]  Abort due to constraint violation"
        + " (FOREIGN KEY constraint failed)", null, 19);
    assertThat(SqliteErrorUtil.getExceptionType(e)).isEqualTo(ExceptionType.FOREIGN_KEY);
  }
  
  @Test
  public void recognizesNotNullError() {
    SQLException e = new SQLException("[SQLITE_CONSTRAINT]  Abort due to constraint violation"
        + " (NOT NULL constraint failed: environment.policy_arn)", null, 19);
    assertThat(SqliteErrorUtil.getExceptionType(e)).isEqualTo(ExceptionType.NOT_NULL);
  }
  
  @Test
  public void recognizesOtherException() {
    SQLException e = new SQLException();
    assertThat(SqliteErrorUtil.getExceptionType(e)).isEqualTo(ExceptionType.OTHER);
  }
}
