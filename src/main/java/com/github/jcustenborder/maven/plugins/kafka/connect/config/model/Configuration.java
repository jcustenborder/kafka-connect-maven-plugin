package com.github.jcustenborder.maven.plugins.kafka.connect.config.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.CaseFormat;
import com.google.common.base.Strings;
import com.helger.jcodemodel.AbstractJClass;
import com.helger.jcodemodel.JDefinedClass;
import com.helger.jcodemodel.JExpr;
import com.helger.jcodemodel.JFieldRef;
import com.helger.jcodemodel.JFieldVar;
import com.helger.jcodemodel.JInvocation;
import com.helger.jcodemodel.JMod;
import org.apache.kafka.common.config.ConfigDef;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Configuration {
  @JsonProperty("name")
  private String name;
  @JsonProperty("extends")
  private String _extends;

  @JsonProperty("prefix")
  private String prefix;
  @JsonProperty("groups")
  private List<Group> groups = new ArrayList<>();


  public String name() {
    return this.name;
  }

  public void name(String name) {
    this.name = name;
  }

  public String _extends() {
    return this._extends;
  }

  public void _extends(String _extends) {
    this._extends = _extends;
  }

  public String prefix() {
    return this.prefix;
  }

  public void prefix(String prefix) {
    this.prefix = prefix;
  }

  public List<Group> groups() {
    return this.groups;
  }

  public void groups(List<Group> groups) {
    this.groups = groups;
  }

  public void setParents() {
    for (Group group : this.groups) {
      group.configuration = this;
      for (ConfigItem item : group.configItems) {
        item.group = group;
      }
    }
  }

  public static class Group {
    @JsonIgnore
    private Configuration configuration;
    @JsonProperty("name")
    private String name;
    @JsonProperty("display")
    private String display;
    @JsonProperty("prefix")
    private String prefix;
    @JsonProperty("configItems")
    private List<ConfigItem> configItems = new ArrayList<>();

    public String display() {
      return this.display;
    }

    public void display(String display) {
      this.display = display;
    }

    public Configuration configuration() {
      return this.configuration;
    }

    public String name() {
      return this.name;
    }

    public void name(String name) {
      this.name = name;
    }

    public String prefix() {
      return this.prefix;
    }

    public void prefix(String prefix) {
      this.prefix = prefix;
    }

    public List<ConfigItem> configItems() {
      return this.configItems;
    }

    public void configItems(List<ConfigItem> configItems) {
      this.configItems = configItems;
    }

    @JsonIgnore
    private JFieldVar displayConstant;

    public JFieldVar displayConstant(JDefinedClass configClass) {
      if (null == this.displayConstant) {
        String fieldName = CaseFormat.LOWER_HYPHEN.to(
            CaseFormat.UPPER_UNDERSCORE,
            this.prefix.replace('.', '_') + "group"
        );
        this.displayConstant = configClass.field(
            JMod.PROTECTED | JMod.STATIC | JMod.FINAL,
            String.class,
            fieldName
        );
        this.displayConstant.init(JExpr.lit(this.display));
      }

      return this.displayConstant;
    }
  }

  public static class ConfigItem {
    @JsonIgnore
    private Group group;
    @JsonProperty("configKey")
    private String configKey;
    @JsonProperty("type")
    private ConfigDef.Type type;
    @JsonProperty("documentation")
    private String documentation;
    @JsonProperty("importance")
    private ConfigDef.Importance importance = ConfigDef.Importance.MEDIUM;
    @JsonProperty("width")
    private ConfigDef.Width width = ConfigDef.Width.MEDIUM;
    @JsonProperty("defaultValue")
    private Object defaultValue;
    @JsonProperty("displayName")
    private String displayName;
    @JsonProperty("extendedType")
    private ExtendedType extendedType = ExtendedType.None;

    public Group group() {
      return this.group;
    }

    public String configKey() {
      return this.configKey;
    }

    public void configKey(String configKey) {
      this.configKey = configKey;
    }

    public ConfigDef.Type type() {
      return this.type;
    }

    public void type(ConfigDef.Type type) {
      this.type = type;
    }

    public String documentation() {
      return this.documentation;
    }

    public void documentation(String documentation) {
      this.documentation = documentation;
    }

    public ConfigDef.Importance importance() {
      return this.importance;
    }

    public void importance(ConfigDef.Importance importance) {
      this.importance = importance;
    }

    public ConfigDef.Width width() {
      return this.width;
    }

    public void width(ConfigDef.Width width) {
      this.width = width;
    }

    public Object defaultValue() {
      return this.defaultValue;
    }

    public void defaultValue(Object defaultValue) {
      this.defaultValue = defaultValue;
    }

    public String displayName() {
      return this.displayName;
    }

    public void displayName(String displayName) {
      this.displayName = displayName;
    }

    public ExtendedType extendedType() {
      return this.extendedType;
    }

    public void extendedType(ExtendedType extendedType) {
      this.extendedType = extendedType;
    }

    public String fullyQualifiedConfigItem() {
      return String.format(
          "%s%s%s",
          this.group().configuration().prefix(),
          this.group().prefix(),
          this.configKey()
      );
    }

    private String constantName(String name) {
      return CaseFormat.LOWER_UNDERSCORE.to(
          CaseFormat.UPPER_UNDERSCORE,
          String.format(
              "%s%s_%s",
              this.group().prefix(),
              this.configKey(),
              name
          ).replace('.', '_')
      );
    }

    @JsonIgnore
    JFieldVar configConstant;

    public JFieldVar configConstant(JDefinedClass configClass) {
      if (null == this.configConstant) {
        String fieldName = constantName("conf");
        this.configConstant = configClass.field(
            JMod.PUBLIC | JMod.STATIC | JMod.FINAL,
            String.class,
            fieldName
        );
        this.configConstant.init(JExpr.lit(this.fullyQualifiedConfigItem()));
      }

      return this.configConstant;
    }

    @JsonIgnore
    JFieldVar docConstant;

    public JFieldVar docConstant(JDefinedClass configClass) {
      if (null == this.docConstant) {
        String fieldName = constantName("doc");
        this.docConstant = configClass.field(
            JMod.PROTECTED | JMod.STATIC | JMod.FINAL,
            String.class,
            fieldName
        );
        this.docConstant.init(JExpr.lit(this.documentation()));
      }

      return this.docConstant;
    }

    @JsonIgnore
    JFieldVar defaultConstant;

    public JFieldVar defaultConstant(
        JDefinedClass configClass,
        JFieldRef noDefaultValue,
        AbstractJClass arraysClass,
        AbstractJClass configDefPasswordClass) {
      if (null == this.defaultConstant) {
        String fieldName = constantName("default");
        this.defaultConstant = configClass.field(
            JMod.PROTECTED | JMod.STATIC | JMod.FINAL,
            Object.class,
            fieldName
        );

        if (null == this.defaultValue) {
          this.defaultConstant.init(noDefaultValue);
        } else if (this.defaultValue instanceof Number) {
          Number number = (Number) this.defaultValue;
          switch (this.type) {
            case DOUBLE:
              this.defaultConstant.init(JExpr.lit(number.doubleValue()));
              break;
            case SHORT:
              this.defaultConstant.init(JExpr.lit(number.shortValue()));
              break;
            case INT:
              this.defaultConstant.init(JExpr.lit(number.intValue()));
              break;
            case LONG:
              this.defaultConstant.init(JExpr.lit(number.longValue()));
              break;
          }
        } else if (this.defaultValue instanceof String) {
          switch (this.type()) {
            case STRING:
              this.defaultConstant.init(JExpr.lit(this.defaultValue.toString()));
              break;
            case PASSWORD:
              this.defaultConstant.init(
                  JExpr._new(configDefPasswordClass)
                      .arg(this.defaultValue.toString())
              );
              break;
          }

        } else if (this.defaultValue instanceof Boolean) {
          this.defaultConstant.init(JExpr.lit((Boolean) this.defaultValue));
        } else if (this.defaultValue instanceof List) {
          List<String> items = (List<String>) this.defaultValue;
          JInvocation initExpression = arraysClass.staticInvoke("asList");
          for (String o : items) {
            initExpression.arg(o);
          }

          this.defaultConstant.init(initExpression);
        }


      }

      return this.defaultConstant;
    }

    public String methodName() {
      return CaseFormat.LOWER_UNDERSCORE.to(
          CaseFormat.LOWER_CAMEL,
          String.format(
              "%s%s",
              this.group().prefix(),
              this.configKey()
          ).replace('.', '_')
      );
    }

    public String recommenderMethodName() {
      return methodName() + "Recommender";
    }

    public String validatorMethodName() {
      return methodName() + "Validator";
    }

    JFieldVar displayNameConstant;

    public JFieldVar displayNameConstant(JDefinedClass configClass) {
      if (null == displayNameConstant) {
        String fieldName = constantName("display");
        this.displayNameConstant = configClass.field(
            JMod.PROTECTED | JMod.STATIC | JMod.FINAL,
            String.class,
            fieldName
        );

        if (!Strings.isNullOrEmpty(this.displayName)) {
          this.displayNameConstant.init(JExpr.lit(this.displayName));
        } else {
          this.displayNameConstant.init(JExpr._null());
        }
      }

      return this.displayNameConstant;
    }
  }

  public enum ExtendedType {
    None,
    Enum,
    Url,
    Uri,
    HostAndPort,
    Charset
  }

  static final ObjectMapper OBJECT_MAPPER;

  static {
    OBJECT_MAPPER = new ObjectMapper();
    OBJECT_MAPPER.configure(SerializationFeature.INDENT_OUTPUT, true);
    OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
  }

  public static void save(Configuration configuration, File outputPath) throws IOException {
    try (OutputStream outputStream = new FileOutputStream(outputPath)) {
      save(configuration, outputStream);
    }
  }

  public static void save(Configuration configuration, OutputStream outputStream) throws IOException {
    OBJECT_MAPPER.writeValue(outputStream, configuration);
  }

  public static Configuration load(File inputFile) throws IOException {
    try (InputStream inputStream = new FileInputStream(inputFile)) {
      return load(inputStream);
    }
  }

  public static Configuration load(InputStream inputStream) throws IOException {
    Configuration result = OBJECT_MAPPER.readValue(inputStream, Configuration.class);
    result.setParents();
    return result;
  }
}
