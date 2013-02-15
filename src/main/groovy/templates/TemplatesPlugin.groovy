package templates

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

import templates.tasks.ExportAllTemplates
import templates.tasks.CreateMultipleBuildProject

class TemplatesPlugin implements Plugin<Project> {
  static final String GROUP = "Template"
  static final String CREATE_MULTIPLE_BUILD_PROJECT_TASK = "createMultipleBuildProject"
  static final String EXPORT_ALL_TEMPLATES_TASK = "exportAllTemplates"

  TemplatesExtension extension

  def void apply(Project project) {
    project.convention.plugins.templatePlugin = new TemplatesPluginConvention()
    project.apply(plugin: GroovyTemplatesPlugin)
    // project.apply(plugin: GradlePluginTemplatesPlugin)
    project.apply(plugin: JavaTemplatesPlugin)
    // project.apply(plugin: ScalaTemplatesPlugin)
    // project.apply(plugin: WebappTemplatesPlugin)
    
    configureTemplatesExtension(project)
    // configureExportTemplates(project)
    configureMultipleBuildProject(project)
  }

  def configureTemplatesExtension(Project project) { 
    extension = project.extensions.add('templates', TemplatesExtension)
  }

  def configureExportTemplates(Project project) {
    Task exportAllTemplates = project.tasks.add(EXPORT_ALL_TEMPLATES_TASK, ExportAllTemplates)
    exportAllTemplates.description = 'Exports all the default template files into the current directory.'
    exportAllTemplates.group = TemplatesPlugin.GROUP
    exportAllTemplates.dependsOn(['exportJavaTemplates', 'exportGroovyTemplates', 'exportScalaTemplates', 'exportWebappTemplates','exportPluginTemplates'])
  }

  def configureMultipleBuildProject(Project project) { 
    Task createMultipleBuildProject = project.tasks.add(CREATE_MULTIPLE_BUILD_PROJECT_TASK, CreateMultipleBuildProject)
    createMultipleBuildProject.group = TemplatesPlugin.GROUP
    createMultipleBuildProject.description = 'Creates a multiple directory project root structure'
    createMultipleBuildProject.conventionMapping.startingDir = { project.rootDir }

  }

}