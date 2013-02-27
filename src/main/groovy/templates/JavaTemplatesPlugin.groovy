package templates

import templates.tasks.CreateJavaProject
import templates.tasks.CreateJavaSubProject
import templates.tasks.CreateJavaClass

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

/**
 * Adds basic tasks for bootstrapping Java projects. Adds createJavaClass, createJavaProject,
 * createJavaSubProject, and initJavaProject tasks.
 */
class JavaTemplatesPlugin implements Plugin<Project> {
  static final String CREATE_JAVA_PROJECT_TASK_NAME = "createJavaProject"
  static final String CREATE_JAVA_CLASS_TASK_NAME = "createJavaClass"

  void apply(Project project) {
    // configureInitJavaProject(project)
    configureCreateJavaProject(project)
    configureCreateJavaClass(project)
  }

  def configureInitJavaProject(project) { 
    Task initJavaProject = project.tasks.add(INIT_JAVA_PROJECT_TASK_NAME, InitJavaProject)
    initJavaProject.group = TemplatesPlugin.GROUP
    initJavaProject.description = 'Initializes a new Gradle Java project in the current directory.'
    createBase()
    File buildFile = new File('build.gradle')
    buildFile.exists() ?: buildFile.createNewFile()
    TemplatesPlugin.prependPlugin 'java', buildFile
  }

  def configureCreateJavaProject(project) { 
    Task createJavaProject =  project.tasks.add(CREATE_JAVA_PROJECT_TASK_NAME, CreateJavaProject)
    createJavaProject.group = TemplatesPlugin.GROUP
    createJavaProject.description = 'Creates a new Gradle Java project in a new directory named after your project.'
    createJavaProject.conventionMapping.startingDir = { project.rootDir }
  }

  def configureCreateJavaClass(project) { 
    Task createJavaClass = project.tasks.add(CREATE_JAVA_CLASS_TASK_NAME, CreateJavaClass)
    createJavaClass.group = TemplatesPlugin.GROUP
    createJavaClass.description = 'Creates a new Java class in the current project.'
    createJavaClass.conventionMapping.projectDirectory = { project.projectDir }
    createJavaClass.conventionMapping.currentDirectory = { "." }
    createJavaClass.conventionMapping.javaSourceDir = { project.templates.javaSrcDir }
  }
}