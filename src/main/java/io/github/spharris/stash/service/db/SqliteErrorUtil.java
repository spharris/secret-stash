package io.github.spharris.stash.service.db;

import java.sql.SQLException;

/**
 * A utility to transform SQL errors from SQLite into something more useful 
 */
class SqliteErrorUtil {
  
  static enum ExceptionType {
    OTHER(-1, null),
    UNIQUENESS(19, "UNIQUE"),
    FOREIGN_KEY(19, "FOREIGN KEY"),
    NOT_NULL(19, "NOT NULL");
    
    private final int errorCode;
    private final String messagePattern;
    
    ExceptionType(int errorCode, String messagePattern) {
      this.errorCode = errorCode;
      this.messagePattern = messagePattern;
    }
    
    public int getErrorCode() {
      return errorCode;
    }
    
    public String getMessagePattern() {
      return messagePattern;
    }
  }

  private SqliteErrorUtil() {}
  
  static ExceptionType getExceptionType(SQLException e) {
    for (ExceptionType et : ExceptionType.values()) {
      if (et == ExceptionType.OTHER) {
        continue;
      }
      
      if (e.getErrorCode() == et.getErrorCode()
          && e.getMessage().contains(et.getMessagePattern())) {
        return et;
      }
    }
    
    return ExceptionType.OTHER;
  }
}
