package templates

import templates.tasks.CreateGroovyProject
import templates.tasks.CreateGroovyClass

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

/**
 * Adds basic tasks for bootstrapping Groovy projects. Adds createGroovyClass, createGroovyProject,
 * and initGroovyProject tasks.
 */
class GroovyTemplatesPlugin implements Plugin<Project> {
  static final String CREATE_GROOVY_PROJECT_TASK_NAME = "createGroovyProject"
  static final String CREATE_GROOVY_SUBPROJECT_TASK_NAME = "createGroovySubProject"
  static final String CREATE_GROOVY_CLASS_TASK_NAME = "createGroovyClass"

  void apply(Project project) {
    // configureInitGroovyProject(project)
    configureCreateGroovyProject(project)
    // configureCreateGroovySubProject(project)
    configureCreateGroovyClass(project)
  }

  def configureCreateGroovyProject(project) { 
    Task createGroovyProject =  project.tasks.add(CREATE_GROOVY_PROJECT_TASK_NAME, CreateGroovyProject)
    createGroovyProject.group = TemplatesPlugin.GROUP
    createGroovyProject.description = 'Creates a new Gradle Groovy project in a new directory named after your project.'
    createGroovyProject.conventionMapping.startingDir = { project.rootDir }
  }

  def configureCreateGroovySubProject(project) { 

  }
  
  def configureInitGroovyProject(project) { 
    project.task('initGroovyProject', group: TemplatesPlugin.group,
    description: 'Initializes a new Gradle Groovy project in the current directory.') << {
      createBase()
      def buildFile = new File('build.gradle')
      buildFile.exists() ?: buildFile.createNewFile()
      TemplatesPlugin.prependPlugin 'groovy', buildFile
    }
  }

  def configureCreateGroovyClass(project) { 
    Task createGroovyClass = project.tasks.add(CREATE_GROOVY_CLASS_TASK_NAME, CreateGroovyClass)
    createGroovyClass.group = TemplatesPlugin.GROUP
    createGroovyClass.description = 'Creates a new Groovy class in the current project.'
    createGroovyClass.conventionMapping.projectDirectory = { project.projectDir }
    createGroovyClass.conventionMapping.currentDirectory = { "." }
    createGroovyClass.conventionMapping.groovySourceDir = { project.templates.groovySrcDir }
  }
}