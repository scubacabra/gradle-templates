package templates.tasks

import org.gradle.api.tasks.TaskAction
import templates.util.ConsolePromptUtil

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

class CreateMultipleBuildProject extends CreateTemplateProject {
  static final log = Logging.getLogger(CreateMultipleBuildProject.class)

  @TaskAction
  void createMultipleProjectBuild() { 
    def template = createTemplateProject({})
    template.fromDirectory(projectName) { 
      'build.gradle' template: "multiBuild/build.gradle.tmpl"
      'settings.gradle' template: 'multiBuild/settings.gradle.tmpl'
      'subProjectChecks.gradle' template: 'multiBuild/subProjectChecks.gradle.tmpl'
    }
  }
}

