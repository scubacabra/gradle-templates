package templates.tasks

import org.gradle.api.tasks.TaskAction

import templates.util.ConsolePromptUtil

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

class CreateWebappProject extends CreateJavaProject {
  static final log = Logging.getLogger(CreateWebappProject.class)

  String useJetty

  @TaskAction
  createWebappProject() { 
    def template = createTemplateProject(additionalUserPrompts)
    template.fromDirectory(projectName) { 
      'build.gradle' template: 'webapp/build.gradle.tmpl', useJetty: useJetty, projectGroup: projectGroup
      // 'gradle.properties' content: "version=${projectVersion}", append: true
    }
  }

  def additionalUserPrompts = { 
    useJetty = ConsolePromptUtil.promptYesOrNo('Use Jetty Plugin?')  
  }
}