package com.github.jcustenborder.maven.plugins.kafka.connect.config;

import com.github.jcustenborder.maven.plugins.kafka.connect.config.model.Configuration;
import com.helger.jcodemodel.AbstractJType;
import com.helger.jcodemodel.JFieldVar;
import org.immutables.value.Value;

import javax.annotation.Nullable;
import java.util.List;

@Value.Immutable
interface ConfigurationState {
  Configuration configuration();

  List<GroupState> groups();

  @Value.Immutable
  interface GroupState {
    Configuration.Group group();

    JFieldVar groupConstant();

    List<ConfigItemState> configItems();
  }

  @Value.Immutable
  interface ConfigItemState {
    Configuration.ConfigItem configItem();

    JFieldVar confConstant();

    JFieldVar docConstant();

    JFieldVar defaultConstant();

    @Nullable
    JFieldVar defaultPortConstant();

    JFieldVar displayNameConstant();

    AbstractJType type();

    String methodName();
  }
}
