package templates.tasks

import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input
import org.gradle.api.DefaultTask

import templates.model.TemplateBuilder
import templates.util.ConsolePromptUtil

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

class CreateMultipleBuildProject extends DefaultTask {
  static final log = Logging.getLogger(CreateMultipleBuildProject.class)

  @Input
  File startingDir

  String templateDir

  @TaskAction
  void createMultipleProjectBuild() { 
    templateDir = ConsolePromptUtil.prompt('Project Name: ')
    log.info("make template at {}", templateDir)
   
    if(!templateDir) { 
      throw new Exception("Need a project name for this multi build project")
    }

    def template = new TemplateBuilder(getStartingDir())
    template.fromDirectory(templateDir) { 
      'build.gradle' template: "multiBuild/build.gradle.tmpl"
      'settings.gradle' template: 'multiBuild/settings.gradle.tmpl'
      'subProjectChecks.gradle' template: 'multiBuild/subProjectChecks.gradle.tmpl'
    }
  }

}