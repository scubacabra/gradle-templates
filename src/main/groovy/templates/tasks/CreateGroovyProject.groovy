package templates.tasks

import org.gradle.api.tasks.TaskAction

import templates.util.ConsolePromptUtil

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

class CreateGroovyProject extends CreateTemplateProject {
  
  static final log = Logging.getLogger(CreateGroovyProject.class)

  String projectVersion
  String projectGroup

  @TaskAction
  createGroovyProject() { 
    def template = createTemplateProject(additionalUserPrompts)
    template.fromDirectory(projectName) { 
      source 'src/main/groovy'
      test 'src/test/groovy'
      'build.gradle' template: 'groovy/build.gradle.tmpl', projectGroup: projectGroup
      'gradle.properties' content: "version=$projectVersion", append: true
      'License.txt' '// Your License Goes Here'
    }
  }

  def additionalUserPrompts = { 
    projectGroup = ConsolePromptUtil.prompt('Group:', projectName.toLowerCase())
    projectVersion = ConsolePromptUtil.prompt('Version:', '1.0')
  }
}