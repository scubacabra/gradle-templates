package templates.tasks

import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input
import org.gradle.api.DefaultTask

import templates.model.TemplateBuilder
import templates.util.ConsolePromptUtil

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

abstract class CreateTemplateProject extends DefaultTask {
  static final log = Logging.getLogger(CreateTemplateProject.class)
  
  @Input
  File startingDir

  String projectName
  
  def createTemplateProject(Closure additionalUserPrompts) { 
    projectName = ConsolePromptUtil.prompt('Project Name: ')
    log.debug("make template at {}", projectName)
    if(projectName) {
      additionalUserPrompts()
      return new TemplateBuilder(getStartingDir())
    } else { 
      throw new Exception("Need a project name for this template project")
    }
  }
  
}