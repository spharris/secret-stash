package io.github.spharris.stash.service.aws;

import java.io.IOException;
import java.util.Arrays;

import com.amazonaws.auth.policy.actions.S3Actions;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

/**
 * Deserializer for S3Actions
 */
class S3ActionsDeserializer extends JsonDeserializer<S3Actions> {

  private static final ImmutableMap<String, S3Actions> ACTION_MAP = ImmutableMap.copyOf(
    Maps.uniqueIndex(
      Arrays.asList(S3Actions.values()),
      (action) -> { return action.getActionName(); }));
  
  @Override
  public S3Actions deserialize(JsonParser p, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    String action = p.getValueAsString();
    
    S3Actions value = ACTION_MAP.get(action);
    if (value == null) {
      throw new JsonParseException(p, "Unrecognized S3Actions: " + action);
    }
    
    return value;
  }

}
