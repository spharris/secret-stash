package io.github.spharris.stash.service;

import com.google.common.collect.ImmutableList;

import io.github.spharris.stash.Environment;
import io.github.spharris.stash.service.request.CreateEnvironmentRequest;
import io.github.spharris.stash.service.request.DeleteEnvironmentRequest;
import io.github.spharris.stash.service.request.GetEnvironmentRequest;
import io.github.spharris.stash.service.request.ListEnvironmentsRequest;
import io.github.spharris.stash.service.request.UpdateEnvironmentRequest;

public interface EnvironmentService {

  ImmutableList<Environment> listEnvironments(ListEnvironmentsRequest request);
  Environment createEnvironment(CreateEnvironmentRequest request);
  Environment getEnvironment(GetEnvironmentRequest request);
  Environment updateEnvironment(UpdateEnvironmentRequest request);
  void deleteEnvironment(DeleteEnvironmentRequest request);
}
