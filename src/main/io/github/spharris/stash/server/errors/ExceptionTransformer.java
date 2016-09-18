package io.github.spharris.stash.server.errors;

/**
 * An interface for transforming {@link RuntimeException}s into {@link Error}s
 */
public interface ExceptionTransformer<T extends Exception> {
  Error transform(T e);
}
