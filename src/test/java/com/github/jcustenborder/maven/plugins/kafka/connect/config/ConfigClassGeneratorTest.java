package com.github.jcustenborder.maven.plugins.kafka.connect.config;

import com.github.jcustenborder.maven.plugins.kafka.connect.config.model.Configuration;
import com.helger.jcodemodel.JClassAlreadyExistsException;
import com.helger.jcodemodel.JCodeModel;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class ConfigClassGeneratorTest {

  ConfigClassGenerator generator;

  @Test
  public void generateA() throws JClassAlreadyExistsException, IOException {
//
//
//    ConfigurationA configurationA = new ConfigurationA();
//    configurationA.name("com.github.jcustenborder.kafka.connect.testing.TestSourceConnectorConfig");
//    configurationA.prefix("myconn.");
//    ConfigurationA.Group group = new ConfigurationA.Group();
//    configurationA.groups().add(group);
//    group.name("SSL");
//    group.prefix("ssl.");
//    group.display("SSL ConfigurationA");
//    ConfigurationA.ConfigItem item;
//    item = new ConfigurationA.ConfigItem();
//    group.configItems().add(item);
//    item.type(ConfigDef.Type.STRING);
//    item.configKey("keystore.path");
//    item.defaultValue("");
//    item.importance(ConfigDef.Importance.HIGH);
//    item.documentation("Location of the Java keystore to use.");
//    item = new ConfigurationA.ConfigItem();
//    group.configItems().add(item);
//    item.configKey("keystore.password");
//    item.defaultValue(null);
//    item.importance(ConfigDef.Importance.HIGH);
//    item.type(ConfigDef.Type.PASSWORD);
//    item.documentation("Location of the Java keystore to use.");
//    item = new ConfigurationA.ConfigItem();
//    group.configItems().add(item);
//    item.type(ConfigDef.Type.STRING);
//    item.configKey("truststore.path");
//    item.defaultValue("");
//    item.importance(ConfigDef.Importance.HIGH);
//    item.documentation("Location of the Java truststore to use.");
//    item = new ConfigurationA.ConfigItem();
//    group.configItems().add(item);
//    item.configKey("truststore.password");
//    item.defaultValue(null);
//    item.importance(ConfigDef.Importance.HIGH);
//    item.type(ConfigDef.Type.PASSWORD);
//    item.documentation("Location of the Java truststore to use.");
//
//
//    configurationA.setParents();
//
//    this.generator = new ConfigClassGenerator(this.codeModel, configurationA);
//    this.generator.generate();
//
//    File outputPath = new File("target/test-generator");
//    outputPath.mkdir();
//
//    this.codeModel.build(outputPath);
//
//    File a = new File("src/test/resources/com/github/jcustenborder/maven/plugins/kafka/connect/config/configurations/Simple.json");
//
//    ConfigurationA.save(configurationA, a);
  }

  @TestFactory
  public Stream<DynamicTest> generate() {
    File rootPath = new File("src/test/resources/com/github/jcustenborder/maven/plugins/kafka/connect/config/configurations");
    final File outputPath = new File("target/test-generator");
    outputPath.mkdir();

    return Arrays.stream(rootPath.listFiles())
        .map(f -> dynamicTest(f.getName(), () -> {
          JCodeModel codeModel = new JCodeModel();
          Configuration configurationA = Configuration.load(f);
          this.generator = new ConfigClassGenerator(codeModel, configurationA);
          this.generator.generate();
          codeModel.build(outputPath);
        }));

  }


}
