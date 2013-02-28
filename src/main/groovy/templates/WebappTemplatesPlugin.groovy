package templates

import templates.tasks.CreateWebappProject

import org.gradle.api.Task
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Adds basic tasks for bootstrapping Webapp projects. Adds createWebappProject, exportWebappTemplates, and
 * initWebappProject tasks. Also applies the java-templates plugin.
 */
class WebappTemplatesPlugin extends JavaTemplatesPlugin implements Plugin<Project> {
  static final CREATE_WEBAPP_PROJECT_TASK_NAME = "createWebappProject"
  static final INIT_WEBAPP_PROJECT_TASK_NAME = "initWebappProject"

  void apply(Project project) {
 
    // Check to make sure JavaTemplatesPlugin isn't already added.
    if (!project.plugins.findPlugin(JavaTemplatesPlugin)) {
      project.apply(plugin: JavaTemplatesPlugin)
    }

    configureCreateWebappProject(project)
    // configureInitWebappProject(project)
  }
  
  def configureCreateWebappProject(project) {
    Task createWebappProject = project.tasks.add(CREATE_WEBAPP_PROJECT_TASK_NAME, CreateWebappProject)
    createWebappProject.group = TemplatesPlugin.GROUP
    createWebappProject.description = 'Creates a new Gradle Webapp project in a new directory named after your project.'
    createWebappProject.conventionMapping.startingDir = { project.rootDir }
  }

  def configureInitWebappProject(project) {
    project.task('initWebappProject', group: TemplatesPlugin.group, description: 'Initializes a new Gradle Webapp project in the current directory.') << {
      createBase(project.name)
      def useJetty = props['useJettyPlugin'] ?: TemplatesPlugin.promptYesOrNo('Use Jetty Plugin?')
      File buildFile = new File('build.gradle')
      buildFile.exists() ?: buildFile.createNewFile()
      if (useJetty) {
	TemplatesPlugin.prependPlugin 'jetty', buildFile
      } else {
	TemplatesPlugin.prependPlugin 'war', buildFile
      }
    }
  }
}