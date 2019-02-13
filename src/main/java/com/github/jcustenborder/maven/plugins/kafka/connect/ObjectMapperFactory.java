package com.github.jcustenborder.maven.plugins.kafka.connect;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.guava.GuavaModule;

public class ObjectMapperFactory {
  public final static ObjectMapper INSTANCE;

  static {
    INSTANCE = new ObjectMapper();
    INSTANCE.configure(SerializationFeature.INDENT_OUTPUT, true);
    INSTANCE.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    INSTANCE.registerModule(new GuavaModule());
  }
}
