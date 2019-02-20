package com.github.jcustenborder.maven.plugins.kafka.connect.config;

import com.github.jcustenborder.maven.plugins.kafka.connect.config.model.Configuration;
import com.google.common.base.CaseFormat;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.net.HostAndPort;
import com.helger.jcodemodel.AbstractJClass;
import com.helger.jcodemodel.AbstractJType;
import com.helger.jcodemodel.EClassType;
import com.helger.jcodemodel.IJExpression;
import com.helger.jcodemodel.JCatchBlock;
import com.helger.jcodemodel.JClassAlreadyExistsException;
import com.helger.jcodemodel.JCodeModel;
import com.helger.jcodemodel.JDefinedClass;
import com.helger.jcodemodel.JExpr;
import com.helger.jcodemodel.JFieldRef;
import com.helger.jcodemodel.JFieldVar;
import com.helger.jcodemodel.JInvocation;
import com.helger.jcodemodel.JMethod;
import com.helger.jcodemodel.JMod;
import com.helger.jcodemodel.JTryBlock;
import com.helger.jcodemodel.JVar;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.common.config.types.Password;

import javax.annotation.Generated;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.security.KeyStore;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ConfigClassGenerator {
  private final JCodeModel model;
  private final Configuration configurationA;
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
  private final AbstractJClass configUtilsClass;
  private final AbstractJClass arraysClass;
  private final AbstractJClass baseConfigClass;
  private final AbstractJClass baseConfigClassOptionsInterface;
  private final AbstractJClass charsetClass;
  private final AbstractJClass stringClass;
  private final AbstractJClass unsupportedCharsetExceptionClass;
  private final AbstractJClass configExceptionClass;
  private final AbstractJClass listClass;
  private final AbstractJClass setClass;
  private final AbstractJClass hostAndPortClass;
  private final AbstractJClass validEnumClass;
  private final AbstractJClass recommendersClass;
  private final AbstractJClass validatorsClass;
  private final AbstractJClass patternClass;


  private final Map<ConfigDef.Type, AbstractJType> configTypeLookup;
  private final Map<ConfigDef.Type, String> configMethodLookup;
  private final Map<ConfigDef.Type, JFieldRef> configReferenceLookup;
  private final Map<ConfigDef.Importance, JFieldRef> configImportanceLookup;
  private final Map<ConfigDef.Width, JFieldRef> configWidthLookup;
  private final JFieldRef configDefNoDefaultValue;
  private JDefinedClass configClass;
  JMethod configMethod;
  JVar configMethodOptionsVar;
  JDefinedClass configOptionsInterface;

  public ConfigClassGenerator(JCodeModel model, Configuration config) {
    this.model = model;
    this.abstractConfigClass = model.ref(AbstractConfig.class);
    this.configDefImportanceClass = model.ref(ConfigDef.Importance.class);
    this.configDefTypeClass = model.ref(ConfigDef.Type.class);
    this.configDefWidthClass = model.ref(ConfigDef.Width.class);
    this.configDefRecommenderClass = model.ref(ConfigDef.Recommender.class);
    this.configDefValidatorClass = model.ref(ConfigDef.Validator.class);
    this.configDefPasswordClass = model.ref(Password.class);
    this.configDefClass = model.ref(ConfigDef.class);
    this.configDefNoDefaultValue = this.configDefClass.staticRef("NO_DEFAULT_VALUE");
    this.configItemBuilderClass = model.ref("com.github.jcustenborder.kafka.connect.utils.config.ConfigKeyBuilder");
    this.configUtilsClass = model.ref("com.github.jcustenborder.kafka.connect.utils.config.ConfigUtils");
    this.configurationA = config;
    this.arraysClass = model.ref(Arrays.class);
    this.charsetClass = model.ref(Charset.class);
    this.stringClass = model.ref(String.class);
    this.listClass = model.ref(List.class);
    this.setClass = model.ref(Set.class);
    this.wildcardMapClass = model.ref(Map.class).narrow(
        this.model.wildcard(),
        this.model.wildcard()
    );
    this.unsupportedCharsetExceptionClass = model.ref(UnsupportedCharsetException.class);
    this.configExceptionClass = model.ref(ConfigException.class);
    this.hostAndPortClass = model.ref(HostAndPort.class);
    this.validEnumClass = model.ref("com.github.jcustenborder.kafka.connect.utils.config.ValidEnum");
    this.recommendersClass = model.ref("com.github.jcustenborder.kafka.connect.utils.config.recommenders.Recommenders");
    this.validatorsClass = model.ref("com.github.jcustenborder.kafka.connect.utils.config.validators.Validators");
    this.patternClass = model.ref(Pattern.class);

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

    if (Strings.isNullOrEmpty(this.configurationA._extends())) {
      this.baseConfigClass = null;
      this.baseConfigClassOptionsInterface = null;
    } else {
      this.baseConfigClass = this.model.ref(this.configurationA._extends());
      this.baseConfigClassOptionsInterface = this.model.ref(this.configurationA._extends() + "ConfigOptions");
    }
  }

  private void setupClass() throws JClassAlreadyExistsException {
    this.configClass = this.model._class(
        JMod.PUBLIC,
        this.configurationA.name(),
        EClassType.CLASS
    );
    this.configClass.annotate(Generated.class).param("generate-config-classes");

    this.configOptionsInterface = this.configClass._interface("ConfigOptions");
    this.configOptionsInterface.javadoc().add("Interface is used to configure the Recommenders and " +
        "Validators for this configurationA.");

    this.configMethod = this.configClass.method(
        JMod.PUBLIC | JMod.STATIC,
        configDefClass,
        "config"
    );
    this.configMethodOptionsVar = this.configMethod.param(
        this.configOptionsInterface,
        "options"
    );
    this.configMethod.javadoc().add("Method is used to define a ConfigDef of this configurationA.");
    this.configMethod.javadoc()
        .addParam(this.configMethodOptionsVar)
        .add("Options interface used to configure Recommenders and Validators for each of the " +
            "configurationA options.");
    this.configMethod.javadoc().addReturn().add("Returns a ConfigDef for the current configurationA");


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

    Map<String, String> parameterJavaDocs = ImmutableMap.of(
        "configOptions", "",
        "definition", "ConfigDef to initialize the AbstractConfig with.",
        "originals", "Original configurationA values as they were passed from the client.",
        "dolog", "Flag to determine if the configurationA variables should be logged."
    );

    this.configClass.constructors().forEachRemaining(jMethod -> {
      jMethod.javadoc().add("Creates a new instance of the config.");
      for (JVar parm : jMethod.params()) {
        String comment = parameterJavaDocs.get(parm.name());
        if (!Strings.isNullOrEmpty(comment)) {
          jMethod.javadoc().addParam(parm).add(comment);
        }
      }
    });
  }

  public void generate() throws JClassAlreadyExistsException {
    setupClass();
    ConfigurationState configurationState = setupConfigState();


//    setupConstants(configurationState);
//    setupConfigOptionMethods();
    setupConfigMethod(configurationState);
    setupConfigMethods(configurationState);
    setupRecommenderMethods(configurationState);
    setupValidatorMethods(configurationState);
  }

  private void setupValidatorMethods(ConfigurationState configurationState) {
    for (ConfigurationState.GroupState groupState : configurationState.groups()) {
      for (ConfigurationState.ConfigItemState itemState : groupState.configItems()) {

        final String fullyQualifiedConfigItem = fullyQualifiedConfigItem(
            configurationState.configuration(), groupState.group(), itemState.configItem()
        );
        JMethod result = this.configOptionsInterface.method(
            JMod.DEFAULT,
            this.configDefValidatorClass,
            itemState.validatorMethodName()
        );

        if (Configuration.ExtendedType.Enum == itemState.configItem().extendedType()) {
          JInvocation invocation = this.validatorsClass.staticInvoke("validEnum")
              .arg(JExpr.dotClass(itemState.type()));
          if (null != itemState.configItem().enumExcludes() && !itemState.configItem().enumExcludes().isEmpty()) {
            for (String enumExclude : itemState.configItem().enumExcludes()) {
              invocation.arg(JExpr.enumConstantRef((AbstractJClass) itemState.type(), enumExclude));
            }
          }
          result.body()._return(invocation);
        } else if (Configuration.ExtendedType.Charset == itemState.configItem().extendedType()) {
          result.body()._return(
              this.validatorsClass.staticInvoke("validCharset")
          );
        } else if (Configuration.ExtendedType.HostAndPort == itemState.configItem().extendedType()) {
          JInvocation invocation = this.validatorsClass.staticInvoke("validHostAndPort");
          if (null != itemState.configItem().defaultPort()) {
            invocation.arg(itemState.configItem().defaultPort())
                .arg(itemState.configItem().requireBracketsForIPv6())
                .arg(itemState.configItem().portRequired());
          }
          result.body()._return(invocation);
        } else if (Configuration.ExtendedType.Uri == itemState.configItem().extendedType()) {
          result.body()._return(
              this.validatorsClass.staticInvoke("validURI")
          );
        } else if (Configuration.ExtendedType.Url == itemState.configItem().extendedType()) {
          result.body()._return(
              this.validatorsClass.staticInvoke("validUrl")
          );
        } else if (Configuration.ExtendedType.Pattern == itemState.configItem().extendedType()) {
          result.body()._return(
              this.validatorsClass.staticInvoke("pattern")
          );
        } else {
          result.body()._return(JExpr._null());
        }


        result.javadoc().add("Method is used to define the Validator that will be " +
            "used for the '" + fullyQualifiedConfigItem + "' parameter.");
        result.javadoc().addReturn().add("Validator for the configurationA item. " +
            "null if no validator is desired.");
      }
    }
  }

  private void setupRecommenderMethods(ConfigurationState configurationState) {
    for (ConfigurationState.GroupState groupState : configurationState.groups()) {
      for (ConfigurationState.ConfigItemState itemState : groupState.configItems()) {

        final String fullyQualifiedConfigItem = fullyQualifiedConfigItem(
            configurationState.configuration(), groupState.group(), itemState.configItem()
        );
        JMethod result = this.configOptionsInterface.method(
            JMod.DEFAULT,
            this.configDefRecommenderClass,
            itemState.recommenderMethodName()
        );

        if (Configuration.ExtendedType.Enum == itemState.configItem().extendedType()) {
          JInvocation invocation = this.recommendersClass.staticInvoke("enumValues")
              .arg(JExpr.dotClass(itemState.type()));

          if (null != itemState.configItem().enumExcludes() && !itemState.configItem().enumExcludes().isEmpty()) {
            for (String enumExclude : itemState.configItem().enumExcludes()) {
              invocation.arg(JExpr.enumConstantRef((AbstractJClass) itemState.type(), enumExclude));
            }
          }
          result.body()._return(invocation);
        } else {
          result.body()._return(JExpr._null());
        }

        result.javadoc().add("Method is used to define the Recommender that will be " +
            "used for the '" + fullyQualifiedConfigItem + "' parameter.");
        result.javadoc().addReturn().add("Recommender for the configurationA item. " +
            "null if no recommender is desired.");
      }
    }
  }

  private ConfigurationState setupConfigState() throws JClassAlreadyExistsException {
    ImmutableConfigurationState.Builder stateBuilder = ImmutableConfigurationState.builder()
        .configuration(this.configurationA);

    for (Configuration.Group group : this.configurationA.groups()) {
      ImmutableGroupState.Builder groupStateBuilder = ImmutableGroupState.builder()
          .groupConstant(addGroupConstant(group))
          .group(group);

      for (Configuration.ConfigItem item : group.configItems()) {
        AbstractJType type = getItemType(item);
        ImmutableConfigItemState.Builder itemStateBuilder = ImmutableConfigItemState.builder()
            .configItem(item)
            .type(type)
            .confConstant(addConfConstant(this.configurationA, group, item))
            .defaultConstant(addDefaultConstant(group, item))
            .defaultPortConstant(addDefaultPortConstant(group, item))
            .displayNameConstant(addDisplayNameConstant(group, item))
            .docConstant(addDocConstant(group, item))
            .methodName(methodName(group, item))
            .recommenderMethodName(methodName(group, item) + "Recommender")
            .validatorMethodName(methodName(group, item) + "Validator");

        groupStateBuilder.addConfigItems(
            itemStateBuilder.build()
        );


      }

      stateBuilder.addGroups(groupStateBuilder.build());
    }

    return stateBuilder.build();
  }

  private JFieldVar addDefaultPortConstant(Configuration.Group group, Configuration.ConfigItem item) {
    JFieldVar result;
    if (Configuration.ExtendedType.HostAndPort != item.extendedType()) {
      result = null;
    } else {
      String constantName = constantName(group, item, "default_port");
      result = this.configClass.field(
          CONSTANT,
          Integer.class,
          constantName
      );
      if (null == item.defaultPort()) {
        result.init(JExpr._null());
      } else {
        result.init(JExpr.lit(item.defaultPort()));
      }
    }

    return result;
  }


  private AbstractJType getItemType(Configuration.ConfigItem item) throws JClassAlreadyExistsException {
    AbstractJType result;

    if (Configuration.ExtendedType.None != item.extendedType()) {
      switch (item.extendedType()) {
        case Uri:
          if (ConfigDef.Type.STRING == item.type()) {
            result = this.model.ref(URI.class);
          } else if (ConfigDef.Type.LIST == item.type()) {
            result = this.listClass.narrow(URI.class);
          } else {
            throw new UnsupportedOperationException(
                String.format(
                    "%s has an unsupported combination of extendedType(%s) and type(%s)",
                    item.configKey(),
                    item.extendedType(),
                    item.type()
                )
            );
          }
          break;
        case Url:
          if (ConfigDef.Type.STRING == item.type()) {
            result = this.model.ref(URL.class);
          } else if (ConfigDef.Type.LIST == item.type()) {
            result = this.listClass.narrow(URL.class);
          } else {
            throw new UnsupportedOperationException(
                String.format(
                    "%s has an unsupported combination of extendedType(%s) and type(%s)",
                    item.configKey(),
                    item.extendedType(),
                    item.type()
                )
            );
          }
          break;
        case Charset:
          result = this.model.ref(Charset.class);
          break;
        case HostAndPort:
          if (ConfigDef.Type.STRING == item.type()) {
            result = this.hostAndPortClass;
          } else if (ConfigDef.Type.LIST == item.type()) {
            result = this.listClass.narrow(this.hostAndPortClass);
          } else {
            throw new UnsupportedOperationException(
                String.format(
                    "%s has an unsupported combination of extendedType(%s) and type(%s)",
                    item.configKey(),
                    item.extendedType(),
                    item.type()
                )
            );
          }
          break;
        case Pattern:
          result = this.patternClass;
          break;
        case Set:
          result = this.setClass.narrow(String.class);
          break;
        case Enum:
          if (null != item.enumValues() && !item.enumValues().isEmpty()) {
            result = addEnum(item);
          } else {
            result = this.model.ref(item.enumType());
          }
          break;
        case PasswordBytes:
          result = this.model.ref(byte[].class);
          break;
        case PasswordCharArray:
          result = this.model.ref(char[].class);
          break;
        case PasswordString:
          result = this.model.ref(String.class);
          break;
        case SSLContext:
          result = this.model.ref(SSLContext.class);
          break;
        case KeyStore:
          result = this.model.ref(KeyStore.class);
          break;
        case KeyManagerFactory:
          result = this.model.ref(KeyManagerFactory.class);
          break;
        case TrustManagerFactory:
          result = this.model.ref(TrustManagerFactory.class);
          break;
        case File:
          result = this.model.ref(File.class);
          break;
        default:
          throw new UnsupportedOperationException(
              String.format("%s", item.type())
          );
      }
    } else {
      switch (item.type()) {
        case DOUBLE:
          result = this.model.DOUBLE;
          break;
        case SHORT:
          result = this.model.SHORT;
          break;
        case INT:
          result = this.model.INT;
          break;
        case LONG:
          result = this.model.LONG;
          break;
        case STRING:
          result = this.model.ref(String.class);
          break;
        case PASSWORD:
          result = this.configDefPasswordClass;
          break;
        case BOOLEAN:
          result = this.model.BOOLEAN;
          break;
        case LIST:
          result = this.model.ref(List.class).narrow(String.class);
          break;
        case CLASS:
          result = this.model.ref(Class.class).narrow(this.model.wildcard());
          break;
        default:
          throw new UnsupportedOperationException(
              String.format("%s", item.type())
          );
      }
    }


    return result;
  }

  private AbstractJClass addEnum(Configuration.ConfigItem item) throws JClassAlreadyExistsException {
    JDefinedClass result = this.configClass._enum(
        JMod.PUBLIC,
        item.enumType()
    );
    for (String s : item.enumValues()) {
      result.enumConstant(s);
    }
    return result;
  }


  private JFieldVar addGroupConstant(Configuration.Group group) {
    String constantName = "GROUP_" + group.name();
    JFieldVar result = this.configClass.field(
        CONSTANT,
        String.class,
        constantName
    );
    result.init(JExpr.lit(group.display()));
    return result;
  }


  private static String methodName(
      Configuration.Group group,
      Configuration.ConfigItem item
  ) {
    return CaseFormat.LOWER_UNDERSCORE.to(
        CaseFormat.LOWER_CAMEL,
        String.format(
            "%s%s",
            group.prefix(),
            item.configKey()
        ).replace('.', '_')
    );
  }


  private JMethod addValidatorMethod(
      Configuration config,
      Configuration.Group group,
      Configuration.ConfigItem item) {
    final String methodName = methodName(group, item) + "Validator";
    final String fullyQualifiedConfigItem = fullyQualifiedConfigItem(
        config, group, item
    );
    JMethod result = this.configOptionsInterface.method(
        JMod.DEFAULT,
        this.configDefValidatorClass,
        methodName
    );
    result.body()._return(JExpr._null());
    result.javadoc().add("Method is used to define the Validator that will be " +
        "used for the '" + fullyQualifiedConfigItem + "' parameter.");
    result.javadoc().addReturn().add("Validator for the configurationA item. " +
        "null if no validator is desired.");
    return result;
  }

  private JMethod addRecommenderMethod(
      Configuration config,
      Configuration.Group group,
      Configuration.ConfigItem item) {
    final String methodName = methodName(group, item) + "Recommender";
    final String fullyQualifiedConfigItem = fullyQualifiedConfigItem(
        config, group, item
    );
    JMethod result = this.configOptionsInterface.method(
        JMod.DEFAULT,
        this.configDefRecommenderClass,
        methodName
    );

    if (Configuration.ExtendedType.Enum == item.extendedType()) {

    } else {
      result.body()._return(JExpr._null());
    }


    result.javadoc().add("Method is used to define the Recommender that will be " +
        "used for the '" + fullyQualifiedConfigItem + "' parameter.");
    result.javadoc().addReturn().add("Recommender for the configurationA item. " +
        "null if no recommender is desired.");

    return result;
  }

  private JFieldVar addDisplayNameConstant(Configuration.Group group, Configuration.ConfigItem item) {
    String constantName = constantName(group, item, "display_name");
    JFieldVar result = this.configClass.field(
        CONSTANT,
        String.class,
        constantName
    );
    if (!Strings.isNullOrEmpty(item.displayName())) {
      result.init(JExpr.lit(item.displayName()));
    } else {
      result.init(JExpr._null());
    }

    return result;
  }

  private JFieldVar addDocConstant(Configuration.Group group, Configuration.ConfigItem item) {
    String constantName = constantName(group, item, "doc");
    JFieldVar result = this.configClass.field(
        CONSTANT,
        String.class,
        constantName
    );
    result.init(JExpr.lit(item.documentation()));

    return result;
  }

  static final int CONSTANT = JMod.PUBLIC | JMod.STATIC | JMod.FINAL;

  IJExpression defaultForType(Configuration.ConfigItem item) {
    IJExpression result;

    if (item.defaultValue() instanceof Number) {
      Number number = (Number) item.defaultValue();
      switch (item.type()) {
        case DOUBLE:
          result = (JExpr.lit(number.doubleValue()));
          break;
        case SHORT:
          result = (JExpr.lit(number.shortValue()));
          break;
        case INT:
          result = (JExpr.lit(number.intValue()));
          break;
        case LONG:
          result = (JExpr.lit(number.longValue()));
          break;
        default:
          throw new UnsupportedOperationException(item.type().name());
      }
    } else if (item.defaultValue() instanceof String) {
      switch (item.type()) {
        case STRING:
          result = (JExpr.lit(item.defaultValue().toString()));
          break;
        case PASSWORD:
          result = (
              JExpr._new(configDefPasswordClass)
                  .arg(item.defaultValue().toString())
          );
          break;
        default:
          throw new UnsupportedOperationException(item.type().name());
      }
    } else if (item.defaultValue() instanceof Boolean) {
      result = (JExpr.lit((Boolean) item.defaultValue()));
    } else if (item.defaultValue() instanceof List) {
      List<String> items = (List<String>) item.defaultValue();
      JInvocation initExpression = arraysClass.staticInvoke("asList");
      for (String o : items) {
        initExpression.arg(o);
      }
      result = (initExpression);
    } else {
      throw new UnsupportedOperationException(
          String.format("%s", item.configKey())
      );
    }

    return result;
  }

  private JFieldVar addDefaultConstant(
      Configuration.Group group,
      Configuration.ConfigItem item
  ) {
    String constantName = constantName(group, item, "default");
    JFieldVar result = this.configClass.field(
        CONSTANT,
        Object.class,
        constantName
    );


    if (null == item.defaultValue()) {
      result.init(this.configDefNoDefaultValue);
    } else if (null != item.defaultValue() && Configuration.ExtendedType.Enum == item.extendedType()) {
      AbstractJClass enumClass = this.model.ref(item.enumType());
      result.init(
          JExpr.invoke(
              enumClass.staticRef(item.defaultValue().toString()),
              "name"
          )
      );
    } else {
      result.init(defaultForType(item));
    }

    return result;
  }

  static String constantName(
      Configuration.Group group,
      Configuration.ConfigItem item,
      String name) {
    return CaseFormat.LOWER_UNDERSCORE.to(
        CaseFormat.UPPER_UNDERSCORE,
        String.format(
            "%s%s_%s",
            group.prefix(),
            item.configKey(),
            name
        ).replace('.', '_')
    );
  }

  public String fullyQualifiedConfigItem(
      Configuration configuration,
      Configuration.Group group,
      Configuration.ConfigItem item
  ) {
    return String.format(
        "%s%s%s",
        configuration.prefix(),
        group.prefix(),
        item.configKey()
    );
  }

  private JFieldVar addConfConstant(
      Configuration configuration,
      Configuration.Group group,
      Configuration.ConfigItem item
  ) {
    String constantName = constantName(group, item, "conf");
    String fullyQualifiedConfig = fullyQualifiedConfigItem(
        configuration,
        group,
        item
    );
    JFieldVar result = this.configClass.field(
        CONSTANT,
        String.class,
        constantName
    );
    result.init(JExpr.lit(fullyQualifiedConfig));

    return result;
  }

  private void setupConfigMethod(ConfigurationState configurationState) {
    JVar configVar = this.configMethod.body().decl(
        this.configDefClass,
        "config"
    );

    if (null == this.baseConfigClass) {
      configVar.init(JExpr._new(this.configDefClass));
    } else {
      configVar.init(this.baseConfigClass.staticInvoke(this.configMethod));
    }

    for (ConfigurationState.GroupState groupState : configurationState.groups()) {
      this.configMethod.body().addSingleLineComment(
          String.format("Start %s", groupState.group().display())
      );


      for (ConfigurationState.ConfigItemState configItemState : groupState.configItems()) {
        JFieldRef typeReference = configReferenceLookup.get(configItemState.configItem().type());
        JFieldRef importanceReference = configImportanceLookup.get(configItemState.configItem().importance());
        JFieldRef widthReference = configWidthLookup.get(configItemState.configItem().width());

        JInvocation invocation = this.configItemBuilderClass.staticInvoke("of")
            .arg(configItemState.confConstant())
            .arg(typeReference);
        invocation = JExpr.invoke(invocation, "recommender")
            .arg(JExpr.invoke(configMethodOptionsVar, configItemState.recommenderMethodName()));
        invocation = JExpr.invoke(invocation, "validator")
            .arg(JExpr.invoke(configMethodOptionsVar, configItemState.validatorMethodName()));
        invocation = JExpr.invoke(invocation, "defaultValue")
            .arg(configItemState.defaultConstant());
        invocation = JExpr.invoke(invocation, "documentation")
            .arg(configItemState.docConstant());
        invocation = JExpr.invoke(invocation, "importance")
            .arg(importanceReference);
        invocation = JExpr.invoke(invocation, "group")
            .arg(groupState.groupConstant());
        invocation = JExpr.invoke(invocation, "displayName")
            .arg(configItemState.displayNameConstant());
        invocation = JExpr.invoke(invocation, "width")
            .arg(widthReference);
        invocation = JExpr.invoke(invocation, "build");


        this.configMethod.body().add(
            JExpr.invoke(configVar, "define")
                .arg(invocation)
        );
      }
      this.configMethod.body().addSingleLineComment(
          String.format("End %s", groupState.group().display())
      );
    }


    this.configMethod.body()._return(configVar);
  }


  private void setupConfigMethods(ConfigurationState configurationState) {
    final int mod = JMod.PUBLIC;

    for (ConfigurationState.GroupState groupState : configurationState.groups()) {
      for (ConfigurationState.ConfigItemState itemState : groupState.configItems()) {
        JMethod method = this.configClass.method(mod, itemState.type(), itemState.methodName());
        method.javadoc().add(itemState.configItem().documentation());
        method.javadoc().addReturn().add(itemState.configItem().documentation());

        if (Configuration.ExtendedType.None == itemState.configItem().extendedType()) {
          String configMethod = this.configMethodLookup.get(itemState.configItem().type());
          method.body()._return(
              JExpr.invoke(configMethod)
                  .arg(itemState.confConstant())
          );
        } else if (Configuration.ExtendedType.Charset == itemState.configItem().extendedType()) {
          JVar charsetNameVar = method.body().decl(JMod.FINAL, this.stringClass, "charsetName");
          JTryBlock tryBlock = method.body()._try();

          charsetNameVar.init(
              JExpr.invoke("getString")
                  .arg(itemState.confConstant())
          );
          tryBlock.body()._return(
              charsetClass.staticInvoke("forName")
                  .arg(charsetNameVar)
          );

          JCatchBlock catchUnsupportedCharsetException = tryBlock._catch(this.unsupportedCharsetExceptionClass);
          JVar varException = catchUnsupportedCharsetException.body().decl(
              this.configExceptionClass,
              "exception"
          );
          varException.init(
              JExpr._new(this.configExceptionClass)
                  .arg(itemState.confConstant())
                  .arg(charsetNameVar)
                  .arg("Invalid charset.")
          );
          catchUnsupportedCharsetException.body().add(
              JExpr.invoke(varException, "initCause")
                  .arg(catchUnsupportedCharsetException.param("ex"))
          );
          catchUnsupportedCharsetException.body()._throw(varException);
        } else if (Configuration.ExtendedType.Enum == itemState.configItem().extendedType()) {
          method.body()._return(
              this.configUtilsClass.staticInvoke("getEnum")
                  .arg(JExpr.dotClass(itemState.type()))
                  .arg(JExpr._this())
                  .arg(itemState.confConstant())
          );
        } else if (Configuration.ExtendedType.HostAndPort == itemState.configItem().extendedType()) {
          String methodName = ConfigDef.Type.LIST == itemState.configItem().type() ?
              "hostAndPorts" :
              "hostAndPort";

          method.body()._return(
              this.configUtilsClass.staticInvoke(methodName)
                  .arg(JExpr._this())
                  .arg(itemState.confConstant())
                  .arg(itemState.defaultPortConstant())
          );
        } else if (Configuration.ExtendedType.Uri == itemState.configItem().extendedType()) {
          String methodName = ConfigDef.Type.LIST == itemState.configItem().type() ?
              "uri" :
              "uris";

          method.body()._return(
              this.configUtilsClass.staticInvoke(methodName)
                  .arg(JExpr._this())
                  .arg(itemState.confConstant())
          );
        } else if (Configuration.ExtendedType.Url == itemState.configItem().extendedType()) {
          String methodName = ConfigDef.Type.LIST == itemState.configItem().type() ?
              "url" :
              "urls";

          method.body()._return(
              this.configUtilsClass.staticInvoke(methodName)
                  .arg(JExpr._this())
                  .arg(itemState.confConstant())
          );
        } else if (Configuration.ExtendedType.Pattern == itemState.configItem().extendedType()) {
          method.body()._return(
              this.configUtilsClass.staticInvoke("pattern")
                  .arg(JExpr._this())
                  .arg(itemState.confConstant())
          );
        } else if (Configuration.ExtendedType.Set == itemState.configItem().extendedType()) {
          method.body()._return(
              this.configUtilsClass.staticInvoke("getSet")
                  .arg(JExpr._this())
                  .arg(itemState.confConstant())
          );
        } else if (Configuration.ExtendedType.PasswordBytes == itemState.configItem().extendedType()) {
          method.body()._return(
              this.configUtilsClass.staticInvoke("passwordBytes")
                  .arg(JExpr._this())
                  .arg(itemState.confConstant())
          );
        } else if (Configuration.ExtendedType.PasswordCharArray == itemState.configItem().extendedType()) {
          method.body()._return(
              this.configUtilsClass.staticInvoke("passwordCharArray")
                  .arg(JExpr._this())
                  .arg(itemState.confConstant())
          );
        } else if (Configuration.ExtendedType.PasswordString == itemState.configItem().extendedType()) {
          method.body()._return(
              JExpr.invoke(
                  JExpr.invoke("getPassword")
                      .arg(itemState.confConstant()),
                  "value"
              )
          );
        } else if (Configuration.ExtendedType.SSLContext == itemState.configItem().extendedType()) {
          method.body()._return(
              this.configUtilsClass.staticInvoke("sslContext")
                  .arg(JExpr._this())
                  .arg(itemState.confConstant())
          );
        } else if (Configuration.ExtendedType.TrustManagerFactory == itemState.configItem().extendedType()) {
          method.body()._return(
              this.configUtilsClass.staticInvoke("trustManagerFactory")
                  .arg(JExpr._this())
                  .arg(itemState.confConstant())
          );
        } else if (Configuration.ExtendedType.KeyStore == itemState.configItem().extendedType()) {
          method.body()._return(
              this.configUtilsClass.staticInvoke("keyStore")
                  .arg(JExpr._this())
                  .arg(itemState.confConstant())
          );
        } else if (Configuration.ExtendedType.KeyManagerFactory == itemState.configItem().extendedType()) {
          method.body()._return(
              this.configUtilsClass.staticInvoke("keyManagerFactory")
                  .arg(JExpr._this())
                  .arg(itemState.confConstant())
          );
        } else if (Configuration.ExtendedType.File == itemState.configItem().extendedType()) {
          method.body()._return(
              this.configUtilsClass.staticInvoke("getAbsoluteFile")
                  .arg(JExpr._this())
                  .arg(itemState.confConstant())
          );
        }
      }
    }
  }

}
