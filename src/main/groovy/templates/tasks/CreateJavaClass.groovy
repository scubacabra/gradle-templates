package templates.tasks

import org.gradle.api.tasks.TaskAction

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

class CreateJavaClass extends CreateClass {
  static final log = Logging.getLogger(CreateJavaClass.class)

  @TaskAction
  def createJavaClass() {
    log.debug("creating class in source directory {}", getJavaSourceDir())
    createClass(getJavaSourceDir())
  }
}
