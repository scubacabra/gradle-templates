package templates.tasks

import org.gradle.api.tasks.TaskAction

import templates.util.ConsolePromptUtil

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

class CreateGroovySubProject extends CreateTemplateProject {

  static final log = Logging.getLogger(CreateGroovySubProject.class)

  String projectVersion
  String projectGroup
  
  @TaskAction
  createGroovySubProject() { 
    def template = createTemplateProject(additionalUserPrompts)
    template.fromDirectory(projectName) { 
      source 'src/main/groovy'
      test 'src/test/groovy'
      "${projectName}.gradle" template: 'groovy/sub/build.gradle.tmpl', projectGroup: projectGroup, projectVersion: projectVersion
    }
    template.fromDirectory("./") { //same directory as the root Project
      'settings.gradle' prepend: true, content: "include '${projectName}'\n"
    }
  }

  def additionalUserPrompts = { 
    projectGroup = ConsolePromptUtil.prompt('Group:', projectName.toLowerCase())
    projectVersion = ConsolePromptUtil.prompt('Version:', '1.0')
  }
}