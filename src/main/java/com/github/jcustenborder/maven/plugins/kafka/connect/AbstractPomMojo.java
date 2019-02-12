package com.github.jcustenborder.maven.plugins.kafka.connect;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractPomMojo extends AbstractMojo {
  MavenXpp3Reader mavenXpp3Reader = new MavenXpp3Reader();
  MavenXpp3Writer mavenXpp3Writer = new MavenXpp3Writer();

  protected Model readPom(InputStream inputStream) throws MojoExecutionException {
    try {
      return mavenXpp3Reader.read(inputStream);
    } catch (IOException|XmlPullParserException e) {
      throw new MojoExecutionException("Failed to save pom", e);
    }
  }

  protected Model readPom(File file) throws MojoExecutionException {
    getLog().info(
        String.format("Reading {}", file)
    );
    try {
      try(FileReader reader = new FileReader(file)) {
        return mavenXpp3Reader.read(reader);
      }
    } catch (IOException|XmlPullParserException e) {
      throw new MojoExecutionException("Failed to save pom", e);
    }
  }

  protected void writePom(Model pom, File file) throws MojoExecutionException {
    getLog().info(
        String.format("Writing {}", file)
    );
    try(FileWriter writer = new FileWriter(file)) {
      this.mavenXpp3Writer.write(writer, pom);
    } catch (IOException e) {
      throw new MojoExecutionException("Failed to save pom", e);
    }
  }



}
