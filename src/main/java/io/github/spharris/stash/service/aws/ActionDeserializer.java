package io.github.spharris.stash.service.aws;

import java.io.IOException;
import java.util.Arrays;

import com.amazonaws.auth.policy.Action;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

/**
 * Deserializer for Actions
 */
public class ActionDeserializer<T extends Enum<T> & Action> extends StdDeserializer<T> {

  private static final long serialVersionUID = -6432993211092523424L;
  
  private ImmutableMap<String, T> actionMap;

  public ActionDeserializer(Class<T> vc) {
    super(vc);
    
    actionMap = ImmutableMap.copyOf(
      Maps.uniqueIndex(Arrays.asList(vc.getEnumConstants()),
        (action) -> { return action.getActionName(); }));
  }
  
  @Override
  public T deserialize(JsonParser p, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    String action = p.getValueAsString();
    
    T value = actionMap.get(action);
    if (value == null) {
      throw new JsonParseException(p, "Unrecognized IdentityManagementActions: " + action);
    }
    
    return value;
  }
}
