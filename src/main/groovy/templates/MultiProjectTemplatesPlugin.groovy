package templates

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

import templates.tasks.CreateJavaSubProject
import templates.tasks.CreateGroovySubProject


class MultiProjectTemplatesPlugin implements Plugin<Project> {
  static final String GROUP = "Template"
  static final String CREATE_JAVA_SUBPROJECT_TASK_NAME = "createJavaSubProject"
  static final String CREATE_GROOVY_SUBPROJECT_TASK_NAME = "createGroovySubProject"

  def void apply(Project project) {
    configureCreateJavaSubProject(project)
    configureCreateGroovySubProject(project)

  }

  def configureCreateJavaSubProject(project) { 
    Task createJavaSubProject =  project.tasks.add(CREATE_JAVA_SUBPROJECT_TASK_NAME, CreateJavaSubProject)
    createJavaSubProject.group = TemplatesPlugin.GROUP
    createJavaSubProject.description = 'Creates a new Gradle Java project in a new directory named after your project.'
    createJavaSubProject.conventionMapping.startingDir = { project.rootDir }
  }

  def configureCreateGroovySubProject(project) { 
    Task createGroovySubProject =  project.tasks.add(CREATE_GROOVY_SUBPROJECT_TASK_NAME, CreateGroovySubProject)
    createGroovySubProject.group = TemplatesPlugin.GROUP
    createGroovySubProject.description = 'Creates a new Gradle Groovy project in a new directory named after your project.'
    createGroovySubProject.conventionMapping.startingDir = { project.rootDir }
  }
}