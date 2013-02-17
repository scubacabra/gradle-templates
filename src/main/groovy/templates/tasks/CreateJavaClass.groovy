package templates.tasks

import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input
import org.gradle.api.DefaultTask

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

import templates.util.ClassNameUtil
import templates.util.ConsolePromptUtil
import templates.model.TemplateBuilder

class CreateJavaClass extends DefaultTask {
  static final log = Logging.getLogger(CreateJavaClass.class)

  String className

  @Input
  String javaSourceDir

  @Input
  String currentDirectory

  @Input
  File projectDirectory

  @TaskAction
  def createJavaClass() { 
    def srcDir = new File(getProjectDirectory(), getJavaSourceDir())
    if(!srcDir.exists()) { 
      new Exception("Java Source Directory {} does not exist, the java plugin must not be applied for this project or their is a different folder convention you have not set up, full path is {}", getJavaSourceDir(), srcDir)
    }

    className = ConsolePromptUtil.prompt("Full package class name i.e. (com.some.examples.MyClass)")
    if(className) { 
      def classParts = ClassNameUtil.getClassParts(className)
      def template = new TemplateBuilder(getProjectDirectory())
      template.fromDirectory(getCurrentDirectory()) { 
	source (getJavaSourceDir() + "/" + classParts.classPackagePath) { 
	  "${classParts.className}.java" template: 'java/java-class.tmpl', classPackage: classParts.classPackage, className: classParts.className
	}
      }
    } else {
      throw new Exception("No Class Name Provided, please provide a class name")
    }
  }
}
