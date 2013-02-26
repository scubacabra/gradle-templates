package templates.tasks

import org.gradle.api.tasks.TaskAction

import templates.util.ConsolePromptUtil
import templates.util.ClassNameUtil

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

class CreateGradlePlugin extends CreateGroovyProject {

  static final log = Logging.getLogger(CreateGradlePlugin.class)

  String pluginApplyLabel
  String pluginClassName

  @TaskAction
  createGradlePlugin() { 
    def template = createTemplateProject(additionalUserPrompts)
    def classParts
    if(pluginClassName) { 
      classParts = ClassNameUtil.getClassParts(pluginClassName)
      log.debug("class Parts are {}", classParts)
    } else { 
      throw new Exception("need a plugin class name")
    }
    template.fromDirectory(projectName) { 
      source ('src/main/resources/META-INF/gradle-plugins') {
	"${pluginApplyLabel}.properties" "implementation-class=${pluginClassName}"
      }
      source ("src/main/groovy/${classParts.classPackagePath}") {
	"${classParts.className}.groovy" template: 'plugin/plugin-class.tmpl', className: classParts.className, classPackage: classParts.classPackage
	"${classParts.className}Convention.groovy" template: 'plugin/convention-class.tmpl', className: classParts.className, classPackage: classParts.classPackage
      }
      'build.gradle' template: 'plugin/build.gradle.tmpl', projectGroup: projectGroup
      'build.gradle' template: 'plugin/installation-tasks.tmpl', append: true
    }
  }
  
  def additionalUserPrompts = { 
    pluginApplyLabel = ConsolePromptUtil.prompt('Plugin \'apply\' label:', projectName.toLowerCase())
    pluginClassName = ConsolePromptUtil.prompt('Plugin class name:', "${projectGroup}.${projectName.capitalize()}Plugin")
  }
}