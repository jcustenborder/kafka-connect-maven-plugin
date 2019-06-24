package com.github.jcustenborder.maven.plugins.kafka.connect.config.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.jcustenborder.maven.plugins.kafka.connect.ObjectMapperFactory;
import org.apache.kafka.common.config.ConfigDef;
import org.immutables.value.Value;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;

@Value.Immutable
@JsonSerialize(as = ImmutableConfiguration.class)
@JsonDeserialize(as = ImmutableConfiguration.class)
public interface Configuration {

  public static void save(Configuration Configuration, File outputPath) throws IOException {
    try (OutputStream outputStream = new FileOutputStream(outputPath)) {
      save(Configuration, outputStream);
    }
  }

  public static void save(Configuration Configuration, OutputStream outputStream) throws IOException {
    ObjectMapperFactory.INSTANCE.writeValue(outputStream, Configuration);
  }

  public static Configuration load(File inputFile) throws IOException {
    try (InputStream inputStream = new FileInputStream(inputFile)) {
      return load(inputStream);
    }
  }

  public static Configuration load(InputStream inputStream) throws IOException {
    Configuration result = ObjectMapperFactory.INSTANCE.readValue(inputStream, Configuration.class);
    return result;
  }

  @JsonProperty("name")
  String name();

  @Nullable
  @JsonProperty("extends")
  String _extends();

  @JsonProperty("prefix")
  String prefix();

  @JsonProperty("groups")
  List<Group> groups();

  @Value.Immutable
  @JsonSerialize(as = ImmutableGroup.class)
  @JsonDeserialize(as = ImmutableGroup.class)
  interface Group {
    @JsonProperty("name")
    String name();

    @JsonProperty("display")
    String display();

    @JsonProperty("prefix")
    String prefix();

    @JsonProperty("configItems")
    List<ConfigItem> configItems();
  }

  @Value.Immutable
  @JsonSerialize(as = ImmutableConfigItem.class)
  @JsonDeserialize(as = ImmutableConfigItem.class)
  interface ConfigItem {
    @JsonProperty("configKey")
    String configKey();

    @JsonProperty("type")
    ConfigDef.Type type();

    @JsonProperty("documentation")
    String documentation();

    @Nullable
    @JsonProperty(value = "importance")
    @Value.Default
    default ConfigDef.Importance importance() {
      return ConfigDef.Importance.MEDIUM;
    }

    @Nullable
    @JsonProperty(value = "width")
    @Value.Default
    default ConfigDef.Width width() {
      return ConfigDef.Width.MEDIUM;
    }

    @Nullable
    @JsonProperty("defaultValue")
    Object defaultValue();

    @Nullable
    @JsonProperty("displayName")
    String displayName();

    @Nullable
    @JsonProperty(value = "extendedType")
    @Value.Default
    default ExtendedType extendedType() {
      return ExtendedType.None;
    }

    @Nullable
    @JsonProperty(value = "enumType")
    String enumType();

    @Nullable
    @JsonProperty(value = "enumValues")
    Set<String> enumValues();

    @Nullable
    @JsonProperty(value = "enumExcludes")
    Set<String> enumExcludes();

    @Nullable
    @JsonProperty(value = "defaultPort")
    Integer defaultPort();

    @Nullable
    @JsonProperty(value = "requireBracketsForIPv6")
    @Value.Default
    default Boolean requireBracketsForIPv6(){return false;}

    @Nullable
    @JsonProperty(value = "portRequired")
    @Value.Default
    default Boolean portRequired(){return false;}
  }

  enum ExtendedType {
    None,
    Enum,
    Url,
    Uri,
    HostAndPort,
    Charset,
    Pattern,
    Set,
    PasswordBytes,
    PasswordCharArray,
    PasswordString,
    KeyStore,
    KeyManagerFactory,
    TrustManagerFactory,
    SSLContext,
    File
  }
}
