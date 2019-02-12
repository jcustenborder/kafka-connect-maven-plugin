package com.github.jcustenborder.maven.plugins.kafka.connect.config;

import com.github.jcustenborder.maven.plugins.kafka.connect.config.model.Configuration;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.helger.jcodemodel.AbstractJClass;
import com.helger.jcodemodel.AbstractJType;
import com.helger.jcodemodel.EClassType;
import com.helger.jcodemodel.JClassAlreadyExistsException;
import com.helger.jcodemodel.JCodeModel;
import com.helger.jcodemodel.JDefinedClass;
import com.helger.jcodemodel.JExpr;
import com.helger.jcodemodel.JFieldRef;
import com.helger.jcodemodel.JFieldVar;
import com.helger.jcodemodel.JInvocation;
import com.helger.jcodemodel.JMethod;
import com.helger.jcodemodel.JMod;
import com.helger.jcodemodel.JVar;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.types.Password;

import javax.annotation.Generated;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ConfigClassGenerator {
  private final JCodeModel model;
  private final Configuration configuration;
  private final AbstractJClass abstractConfigClass;
  private final AbstractJClass configDefClass;
  private final AbstractJClass configDefImportanceClass;
  private final AbstractJClass configDefTypeClass;
  private final AbstractJClass configDefWidthClass;
  private final AbstractJClass configDefRecommenderClass;
  private final AbstractJClass configDefValidatorClass;
  private final AbstractJClass configDefPasswordClass;
  private final AbstractJClass wildcardMapClass;
  private final AbstractJClass configItemBuilderClass;
  private final AbstractJClass arraysClass;
  private final AbstractJClass baseConfigClass;
  private final AbstractJClass baseConfigClassOptionsInterface;
  private final Map<ConfigDef.Type, AbstractJType> configTypeLookup;
  private final Map<ConfigDef.Type, String> configMethodLookup;
  private final Map<ConfigDef.Type, JFieldRef> configReferenceLookup;
  private final Map<ConfigDef.Importance, JFieldRef> configImportanceLookup;
  private final Map<ConfigDef.Width, JFieldRef> configWidthLookup;
  private final JFieldRef configDefNoDefaltValue;
  private JDefinedClass configClass;
  JMethod configMethod;
  JVar configMethodOptionsVar;
  JDefinedClass configOptionsInterface;

  public ConfigClassGenerator(JCodeModel model, Configuration configuration) {
    this.model = model;
    this.abstractConfigClass = model.ref(AbstractConfig.class);
    this.configDefImportanceClass = model.ref(ConfigDef.Importance.class);
    this.configDefTypeClass = model.ref(ConfigDef.Type.class);
    this.configDefWidthClass = model.ref(ConfigDef.Width.class);
    this.configDefRecommenderClass = model.ref(ConfigDef.Recommender.class);
    this.configDefValidatorClass = model.ref(ConfigDef.Validator.class);
    this.configDefPasswordClass = model.ref(Password.class);
    this.configDefClass = model.ref(ConfigDef.class);
    this.configDefNoDefaltValue = this.configDefClass.staticRef("NO_DEFAULT_VALUE");
    this.configItemBuilderClass = model.ref("com.github.jcustenborder.kafka.connect.utils.config.ConfigKeyBuilder");
    this.configuration = configuration;
    this.arraysClass = model.ref(Arrays.class);
    this.wildcardMapClass = model.ref(Map.class).narrow(
        this.model.wildcard(),
        this.model.wildcard()
    );

    Map<ConfigDef.Type, AbstractJType> configTypeLookup = new HashMap<>();
    configTypeLookup.put(ConfigDef.Type.BOOLEAN, this.model.BOOLEAN);
    configTypeLookup.put(ConfigDef.Type.CLASS, this.model.ref(Class.class).narrow(this.model.wildcard()));
    configTypeLookup.put(ConfigDef.Type.DOUBLE, this.model.BOOLEAN);
    configTypeLookup.put(ConfigDef.Type.INT, this.model.INT);
    configTypeLookup.put(ConfigDef.Type.LONG, this.model.LONG);
    configTypeLookup.put(ConfigDef.Type.LIST, this.model.ref(List.class).narrow(String.class));
    configTypeLookup.put(ConfigDef.Type.PASSWORD, this.model.ref(Password.class));
    configTypeLookup.put(ConfigDef.Type.SHORT, this.model.SHORT);
    configTypeLookup.put(ConfigDef.Type.STRING, this.model.ref(String.class));

    this.configTypeLookup = ImmutableMap.copyOf(configTypeLookup);

    Map<ConfigDef.Type, String> configMethodLookup = new HashMap<>();
    configMethodLookup.put(ConfigDef.Type.BOOLEAN, "getBoolean");
    configMethodLookup.put(ConfigDef.Type.DOUBLE, "getDouble");
    configMethodLookup.put(ConfigDef.Type.INT, "getInt");
    configMethodLookup.put(ConfigDef.Type.LONG, "getLong");
    configMethodLookup.put(ConfigDef.Type.LIST, "getList");
    configMethodLookup.put(ConfigDef.Type.PASSWORD, "getPassword");
    configMethodLookup.put(ConfigDef.Type.SHORT, "getShort");
    configMethodLookup.put(ConfigDef.Type.STRING, "getString");

    this.configMethodLookup = ImmutableMap.copyOf(configMethodLookup);

    this.configReferenceLookup = Arrays.stream(ConfigDef.Type.values())
        .map(e -> new AbstractMap.SimpleEntry<>(e, this.configDefTypeClass.staticRef(e.toString())))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    this.configImportanceLookup = Arrays.stream(ConfigDef.Importance.values())
        .map(e -> new AbstractMap.SimpleEntry<>(e, this.configDefImportanceClass.staticRef(e.toString())))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    this.configWidthLookup = Arrays.stream(ConfigDef.Width.values())
        .map(e -> new AbstractMap.SimpleEntry<>(e, this.configDefWidthClass.staticRef(e.toString())))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    if (Strings.isNullOrEmpty(this.configuration._extends())) {
      this.baseConfigClass = null;
      this.baseConfigClassOptionsInterface = null;
    } else {
      this.baseConfigClass = this.model.ref(this.configuration._extends());
      this.baseConfigClassOptionsInterface = this.model.ref(this.configuration._extends() + "ConfigOptions");
    }
  }

  private void setupClass() throws JClassAlreadyExistsException {
    this.configClass = this.model._class(
        JMod.PUBLIC,
        this.configuration.name(),
        EClassType.CLASS
    );
    this.configClass.annotate(Generated.class);

    this.configOptionsInterface = this.configClass._interface("ConfigOptions");
    this.configOptionsInterface.javadoc().add("Interface is used to configure the Recommenders and " +
        "Validators for this configuration.");

    this.configMethod = this.configClass.method(
        JMod.PUBLIC | JMod.STATIC,
        configDefClass,
        "config"
    );
    this.configMethodOptionsVar = this.configMethod.param(
        this.configOptionsInterface,
        "options"
    );
    this.configMethod.javadoc().add("Method is used to define a ConfigDef of this configuration.");
    this.configMethod.javadoc()
        .addParam(this.configMethodOptionsVar)
        .add("Options interface used to configure Recommenders and Validators for each of the " +
            "configuration options.");
    this.configMethod.javadoc().addReturn().add("Returns a ConfigDef for the current configuration");


    if (null == this.baseConfigClass) {
      this.configClass._extends(this.abstractConfigClass);
    } else {
      this.configClass._extends(this.baseConfigClass);
      this.configOptionsInterface._implements(this.baseConfigClassOptionsInterface);
    }

    JMethod constructor = this.configClass.constructor(JMod.PUBLIC);
    JVar definition = constructor.param(configDefClass, "definition");
    JVar originalsVar = constructor.param(wildcardMapClass, "originals");
    constructor.body().add(
        JExpr.invoke("super")
            .arg(definition)
            .arg(originalsVar)
    );

    JMethod constructorWithLog = this.configClass.constructor(JMod.PUBLIC);
    definition = constructorWithLog.param(configDefClass, "definition");
    originalsVar = constructorWithLog.param(wildcardMapClass, "originals");
    JVar doLogVar = constructorWithLog.param(boolean.class, "dolog");
    constructorWithLog.body().add(
        JExpr.invoke("super")
            .arg(definition)
            .arg(originalsVar)
            .arg(doLogVar)
    );


    JMethod constructorWithoutDefinition = this.configClass.constructor(JMod.PUBLIC);
    originalsVar = constructorWithoutDefinition.param(wildcardMapClass, "originals");
    constructorWithoutDefinition.body().add(
        JExpr.invoke("this")
            .arg(JExpr.invoke(this.configMethod).arg(JExpr._new(this.configOptionsInterface)))
            .arg(originalsVar)
    );

    JMethod constructorWithoutDefinitionWithLog = this.configClass.constructor(JMod.PUBLIC);
    originalsVar = constructorWithoutDefinitionWithLog.param(wildcardMapClass, "originals");
    doLogVar = constructorWithoutDefinitionWithLog.param(boolean.class, "dolog");
    constructorWithoutDefinitionWithLog.body().add(
        JExpr.invoke("this")
            .arg(JExpr.invoke(this.configMethod).arg(JExpr._new(this.configOptionsInterface)))
            .arg(originalsVar)
            .arg(doLogVar)
    );

    Map<String, String> parameterJavaDocs = ImmutableMap.of(
        "configOptions", "",
        "definition", "ConfigDef to initialize the AbstractConfig with.",
        "originals", "Original configuration values as they were passed from the client.",
        "dolog", "Flag to determine if the configuration variables should be logged."
    );

    this.configClass.constructors().forEachRemaining(new Consumer<JMethod>() {
      @Override
      public void accept(JMethod jMethod) {
        jMethod.javadoc().add("Creates a new instance of the config.");
        for (JVar parm : jMethod.params()) {
          String comment = parameterJavaDocs.get(parm.name());
          if (!Strings.isNullOrEmpty(comment)) {
            jMethod.javadoc().addParam(parm).add(comment);
          }
        }
      }
    });
  }

  public void generate() throws JClassAlreadyExistsException {
    setupClass();
    setupConstants();
    setupConfigOptionMethods();
    setupConfigMethod();
    setupConfigMethods();
  }

  private void setupConfigOptionMethods() {
    for (Configuration.Group group : this.configuration.groups()) {
      for (Configuration.ConfigItem configItem : group.configItems()) {
        JMethod recommenderMethod = this.configOptionsInterface.method(
            JMod.DEFAULT,
            this.configDefRecommenderClass,
            configItem.recommenderMethodName()
        );
        recommenderMethod.body()._return(JExpr._null());
        recommenderMethod.javadoc().add("Method is used to define the Recommender that will be " +
            "used for the '" + configItem.fullyQualifiedConfigItem() + "' parameter.");
        recommenderMethod.javadoc().addReturn().add("Recommender for the configuration item. " +
            "null if no recommender is desired.");

        JMethod validatorMethod = this.configOptionsInterface.method(
            JMod.DEFAULT,
            this.configDefValidatorClass,
            configItem.validatorMethodName()
        );
        validatorMethod.body()._return(JExpr._null());
        validatorMethod.javadoc().add("Method is used to define the Validator that will be " +
            "used for the '" + configItem.fullyQualifiedConfigItem() + "' parameter.");
        validatorMethod.javadoc().addReturn().add("Validator for the configuration item. " +
            "null if no validator is desired.");
      }
    }
  }

  private void setupConfigMethod() {
    JVar configVar = this.configMethod.body().decl(
        this.configDefClass,
        "config"
    );

    if (null == this.baseConfigClass) {
      configVar.init(JExpr._new(this.configDefClass));
    } else {
      configVar.init(this.baseConfigClass.staticInvoke(this.configMethod));
    }

    for (Configuration.Group group : this.configuration.groups()) {
      this.configMethod.body().addSingleLineComment(
          String.format("Start %s", group.display())
      );
      JFieldVar groupConstant = group.displayConstant(this.configClass);

      for (Configuration.ConfigItem configItem : group.configItems()) {
        JFieldVar confConstant = configItem.configConstant(this.configClass);
        JFieldVar docConstant = configItem.docConstant(this.configClass);
        JFieldVar defaultConstant = configItem.defaultConstant(
            this.configClass,
            this.configDefNoDefaltValue,
            this.arraysClass,
            this.configDefPasswordClass
        );
        JFieldVar displayNameConstant = configItem.displayNameConstant(this.configClass);
        JFieldRef typeReference = configReferenceLookup.get(configItem.type());
        JFieldRef importanceReference = configImportanceLookup.get(configItem.importance());
        JFieldRef widthReference = configWidthLookup.get(configItem.width());

        JInvocation invocation = this.configItemBuilderClass.staticInvoke("of")
            .arg(confConstant)
            .arg(typeReference);
        invocation = JExpr.invoke(invocation, "recommender")
            .arg(JExpr.invoke(configMethodOptionsVar, "recommender"));
        invocation = JExpr.invoke(invocation, "validator")
            .arg(JExpr.invoke(configMethodOptionsVar, "validator"));
        invocation = JExpr.invoke(invocation, "defaultValue")
            .arg(defaultConstant);
        invocation = JExpr.invoke(invocation, "documentation")
            .arg(docConstant);
        invocation = JExpr.invoke(invocation, "importance")
            .arg(importanceReference);
        invocation = JExpr.invoke(invocation, "group")
            .arg(groupConstant);
        invocation = JExpr.invoke(invocation, "displayName")
            .arg(displayNameConstant);
        invocation = JExpr.invoke(invocation, "width")
            .arg(widthReference);
        invocation = JExpr.invoke(invocation, "build");


        this.configMethod.body().add(
            JExpr.invoke(configVar, "define")
                .arg(invocation)
        );
      }
      this.configMethod.body().addSingleLineComment(
          String.format("End %s", group.display())
      );
    }


    this.configMethod.body()._return(configVar);
  }

  private void setupConstants() {
    final int mod = JMod.PUBLIC | JMod.STATIC | JMod.FINAL;

    for (Configuration.Group group : this.configuration.groups()) {
      JFieldVar groupConstant = group.displayConstant(this.configClass);

      for (Configuration.ConfigItem configItem : group.configItems()) {
        JFieldVar confConstant = configItem.configConstant(this.configClass);
        JFieldVar docConstant = configItem.docConstant(this.configClass);
        JFieldVar defaultConstant = configItem.defaultConstant(
            this.configClass,
            this.configDefNoDefaltValue,
            this.arraysClass,
            this.configDefPasswordClass
        );
      }
    }
  }

  private void setupConfigMethods() {
    final int mod = JMod.PUBLIC;

    for (Configuration.Group group : this.configuration.groups()) {
      for (Configuration.ConfigItem configItem : group.configItems()) {
        AbstractJType configType = this.configTypeLookup.get(configItem.type());
        JMethod method = this.configClass.method(mod, configType, configItem.methodName());
        method.javadoc().add(configItem.documentation());
        method.javadoc().addReturn().add(configItem.documentation());
        String configMethod = this.configMethodLookup.get(configItem.type());

        method.body()._return(
            JExpr.invoke(configMethod)
                .arg(configItem.configConstant(this.configClass))
        );
      }
    }
  }

}
