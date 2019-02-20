package com.github.jcustenborder.maven.plugins.kafka.connect.config;

import com.github.jcustenborder.maven.plugins.kafka.connect.config.model.Configuration;
import com.helger.jcodemodel.JCodeModel;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.model.fileset.FileSet;
import org.apache.maven.shared.model.fileset.util.FileSetManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Mojo(name = "generate-config-classes", requiresDirectInvocation = true, requiresOnline = false)
public class GenerateConfigClassesMojo extends AbstractMojo {

  @Parameter(
      property = "outputPath",
      defaultValue = "${project.build.directory}/generated-sources/connect-config-classes"
  )
  private File outputPath;

  @Parameter(defaultValue = "${project}", readonly = true, required = true)
  private MavenProject project;

  @Parameter(
      property = "includeFiles",
      required = true
  )
  private List<String> includeFiles;
  @Parameter(
      property = "excludeFiles"
  )
  private List<String> excludeFiles = new ArrayList<>();

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    if (!outputPath.exists()) {
      outputPath.mkdirs();
    }

    final FileSetManager fileSetManager = new FileSetManager(getLog(), true);
    getLog().info("Searching for input files.");
    List<File> configFiles = new ArrayList<>();
    FileSet fileSet = new FileSet();
    fileSet.setDirectory(project.getBasedir().getAbsolutePath());
    fileSet.setIncludes(this.includeFiles);
    if (!this.excludeFiles.isEmpty()) {
      fileSet.setExcludes(this.excludeFiles);
    }
    String[] fileNames = fileSetManager.getIncludedFiles(fileSet);
    for (String fileName : fileNames) {
      configFiles.add(new File(fileName));
    }

    if (configFiles.isEmpty()) {
      getLog().warn("Found no input files.");
      return;
    }
    getLog().info(
        String.format("Found %s input file(s).", configFiles.size())
    );

    for (File inputFile : configFiles) {
      getLog().info(
          String.format("Processing %s.", inputFile)
      );
      try {
        getLog().debug(
            String.format("Loading %s.", inputFile)
        );
        Configuration configurationA = Configuration.load(inputFile);
        JCodeModel codeModel = new JCodeModel();
        ConfigClassGenerator generator = new ConfigClassGenerator(codeModel, configurationA);
        getLog().debug(
            String.format("Generating %s.", inputFile)
        );
        generator.generate();
        getLog().debug(
            String.format("Building %s to %s.", inputFile, this.outputPath)
        );
        codeModel.build(this.outputPath);
      } catch (Exception ex) {
        getLog().error(ex);
      }
    }

    this.project.addCompileSourceRoot(this.outputPath.getAbsolutePath());
  }

}
