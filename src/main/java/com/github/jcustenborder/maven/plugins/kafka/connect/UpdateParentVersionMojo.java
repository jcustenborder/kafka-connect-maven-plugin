package com.github.jcustenborder.maven.plugins.kafka.connect;

import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

@Mojo(name = "update-parent", requiresDirectInvocation = true, requiresOnline = true)
public class UpdateParentVersionMojo extends AbstractPomMojo {

  @Parameter(property = "pomFile", defaultValue = "pom.xml")
  private File pomFile;

  @Parameter(property = "parentVersion")
  private String parentVersion;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    final Model pom = readPom(this.pomFile);
    Parent parent = pom.getParent();

    if (null == parent) {
      getLog().error("pom does not defined a parent.");
      return;
    }

    parent.setVersion(this.parentVersion);
    writePom(pom, this.pomFile);
  }
}

