package templates.tasks

import org.gradle.api.tasks.TaskAction

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

class CreateGroovyClass extends CreateClass {
  static final log = Logging.getLogger(CreateGroovyClass.class)

  @TaskAction
  def createGroovyClass() { 
    log.debug("creating class in source directory {}", getGroovySourceDir())
    createClass(getGroovySourceDir())
  }
}