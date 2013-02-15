package templates.tasks

import org.gradle.api.tasks.TaskAction

import templates.util.ConsolePromptUtil

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

class CreateJavaProject extends CreateTemplateProject {

  static final log = Logging.getLogger(CreateJavaProject.class)

  String projectVersion
  String projectGroup
  
  @TaskAction
  createJavaProject() { 
    def template = createTemplateProject(additionalUserPrompts)
    template.fromDirectory(projectName) { 
      source 'src/main/java'
      test 'src/test/java'
      'build.gradle' template: 'java/build.gradle.tmpl', projectGroup: projectGroup
      // 'gradle.properties' content: "version=$projectVersion", append: true
      // 'License.txt' '// Your License Goes Here'
    }
  }

  def additionalUserPrompts = { 
    projectGroup = ConsolePromptUtil.prompt('Group:', projectName.toLowerCase())
    projectVersion = ConsolePromptUtil.prompt('Version:', '1.0')
  }
}