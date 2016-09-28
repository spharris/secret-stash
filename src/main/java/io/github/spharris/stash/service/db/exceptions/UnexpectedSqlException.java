package io.github.spharris.stash.service.db.exceptions;

public class UnexpectedSqlException extends RuntimeException {
  private static final long serialVersionUID = 1621695382289701904L;
  
  public UnexpectedSqlException(Throwable t) {
    super(t);
  }
}
