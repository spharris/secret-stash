package io.github.spharris.stash.service.db.exceptions;

public class UniquenessConstraintException extends RuntimeException {

  private static final long serialVersionUID = -6642315364719028202L;
  
  public UniquenessConstraintException(String message, Throwable cause ) {
    super(message, cause);
  }
}
