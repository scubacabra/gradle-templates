package templates.tasks

import org.gradle.api.tasks.TaskAction

import templates.util.ConsolePromptUtil

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

class CreateJavaSubProject extends CreateTemplateProject {

  static final log = Logging.getLogger(CreateJavaSubProject.class)

  String projectVersion
  String projectGroup
  
  @TaskAction
  createJavaSubProject() { 
    def template = createTemplateProject(additionalUserPrompts)
    template.fromDirectory(projectName) { 
      source 'src/main/java'
      test 'src/test/java'
      "${projectName}.gradle" template: 'java/sub/build.gradle.tmpl', projectGroup: projectGroup, projectVersion: projectVersion
    }
    template.fromDirectory("./") { //same directory as the root Project
      'settings.gradle' prepend: true, content: "include '${projectName}'"
    }
  }

  def additionalUserPrompts = { 
    projectGroup = ConsolePromptUtil.prompt('Group:', projectName.toLowerCase())
    projectVersion = ConsolePromptUtil.prompt('Version:', '1.0')
  }
}