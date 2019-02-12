package com.github.jcustenborder.maven.plugins.kafka.connect.config;

import com.github.jcustenborder.maven.plugins.kafka.connect.config.model.Configuration;
import com.helger.jcodemodel.JClassAlreadyExistsException;
import com.helger.jcodemodel.JCodeModel;
import org.apache.kafka.common.config.ConfigDef;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class ConfigClassGeneratorTest {
  JCodeModel codeModel;
  ConfigClassGenerator generator;

  @BeforeEach
  public void before() {
    this.codeModel = new JCodeModel();
  }

  @Test
  public void generateA() throws JClassAlreadyExistsException, IOException {
    Configuration configuration = new Configuration();
    configuration.name("com.github.jcustenborder.kafka.connect.testing.TestSourceConnectorConfig");
    configuration.prefix("myconn.");
    Configuration.Group group = new Configuration.Group();
    configuration.groups().add(group);
    group.name("SSL");
    group.prefix("ssl.");
    group.display("SSL Configuration");
    Configuration.ConfigItem item;
    item = new Configuration.ConfigItem();
    group.configItems().add(item);
    item.type(ConfigDef.Type.STRING);
    item.configKey("keystore.path");
    item.defaultValue("");
    item.importance(ConfigDef.Importance.HIGH);
    item.documentation("Location of the Java keystore to use.");
    item = new Configuration.ConfigItem();
    group.configItems().add(item);
    item.configKey("keystore.password");
    item.defaultValue(null);
    item.importance(ConfigDef.Importance.HIGH);
    item.type(ConfigDef.Type.PASSWORD);
    item.documentation("Location of the Java keystore to use.");
    item = new Configuration.ConfigItem();
    group.configItems().add(item);
    item.type(ConfigDef.Type.STRING);
    item.configKey("truststore.path");
    item.defaultValue("");
    item.importance(ConfigDef.Importance.HIGH);
    item.documentation("Location of the Java truststore to use.");
    item = new Configuration.ConfigItem();
    group.configItems().add(item);
    item.configKey("truststore.password");
    item.defaultValue(null);
    item.importance(ConfigDef.Importance.HIGH);
    item.type(ConfigDef.Type.PASSWORD);
    item.documentation("Location of the Java truststore to use.");


    configuration.setParents();

    this.generator = new ConfigClassGenerator(this.codeModel, configuration);
    this.generator.generate();

    File outputPath = new File("target/test-generator");
    outputPath.mkdir();

    this.codeModel.build(outputPath);

    File a = new File("src/test/resources/com/github/jcustenborder/maven/plugins/kafka/connect/config/configurations/Simple.json");

    Configuration.save(configuration, a);
  }

  @TestFactory
  public Stream<DynamicTest> generate() {
    File rootPath = new File("src/test/resources/com/github/jcustenborder/maven/plugins/kafka/connect/config/configurations");
    final File outputPath = new File("target/test-generator");
    outputPath.mkdir();

    return Arrays.stream(rootPath.listFiles())
        .map(f -> dynamicTest(f.getName(), () -> {
          Configuration configuration = Configuration.load(f);
          this.generator = new ConfigClassGenerator(this.codeModel, configuration);
          this.generator.generate();
          this.codeModel.build(outputPath);
        }));

  }


}
