package templates

import templates.tasks.CreateGroovyProject

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
    // configureCreateGroovyClass(project)
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
    project.task('createGroovyClass', group: TemplatesPlugin.group, description: 'Creates a new Groovy class in the current project.') << {

      def mainSrcDir = null
      try {
	// get main groovy dir, and check to see if Groovy plugin is installed.
	mainSrcDir = findMainGroovyDir(project)
      } catch (Exception e) {
	throw new IllegalStateException('It seems that the Groovy plugin is not installed, I cannot determine the main groovy source directory.', e)
      }

      def fullClassName = props['newClassName'] ?: TemplatesPlugin.prompt('Class name (com.example.MyClass)')

      if (fullClassName) {
	def classParts = JavaTemplatesPlugin.getClassParts(fullClassName)
	ProjectTemplate.fromUserDir {
	  "${mainSrcDir}" {
	    "${classParts.classPackagePath}" {
	      "${classParts.className}.groovy" template: '/templates/groovy/groovy-class.tmpl',
	    className: classParts.className,
	    classPackage: classParts.classPackage
	    }
	  }
	}
      } else {
	println 'No class name provided.'
      }
    }
  }
}