package templates

import templates.tasks.CreateGradlePlugin

import org.gradle.api.Project
import org.gradle.api.Task

/**
 * Adds basic tasks for bootstrapping gradle plugin projects. Adds createGradlePlugin, exportPluginTemplates, and
 * initGradlePlugin tasks. Also applies the groovy-templates plugin.
 */
class GradlePluginTemplatesPlugin extends GroovyTemplatesPlugin {
  static final String CREATE_GRADLE_PLUGIN_TASK_NAME = "createGradlePlugin"
  static final String INIT_GRADLE_PLUGIN_TASK_NAME = "initGradlePlugin"

  void apply(Project project) {
    if (!project.plugins.findPlugin(GroovyTemplatesPlugin)) {
      project.apply(plugin: GroovyTemplatesPlugin)
    }

    configureCreateGradlePlugin(project)
    // configureInitGradlePlugin(project)
  }
    
  def configureCreateGradlePlugin(project) {
    Task createGradlePlugin = project.tasks.add(CREATE_GRADLE_PLUGIN_TASK_NAME, CreateGradlePlugin)
    createGradlePlugin.group = TemplatesPlugin.GROUP
    createGradlePlugin.description = 'Creates a new Gradle Plugin project in a new directory named after your project.'  
    createGradlePlugin.conventionMapping.startingDir = { project.rootDir }
  }
    
  def configureInitGradlePlugin(project) {
    Task initGradlePlugin = project.tasks.add(INIT_GRADLE_PLUGIN_TASK_NAME, InitGradlePlugin)
    initGradlePlugin.group = TemplatesPlugin.GROUP
    initGradlePlugin.description = 'Initializes a new Gradle Plugin project in the current directory.'
    createBase(project)
  }
}