package io.github.spharris.stash.service.aws;

import java.io.IOException;

import com.amazonaws.auth.policy.Action;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class ActionsSerializer extends JsonSerializer<Action> {

  @Override
  public void serialize(Action value, JsonGenerator gen, SerializerProvider serializers)
      throws IOException, JsonProcessingException {
    gen.writeString(value.getActionName());
  }
}
