package templates.tasks

import org.gradle.api.tasks.Input
import org.gradle.api.DefaultTask

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

import templates.util.ClassNameUtil
import templates.util.ConsolePromptUtil
import templates.model.TemplateBuilder

class CreateClass extends DefaultTask { 
  static final log = Logging.getLogger(CreateClass.class)
  
  String className
  boolean makeTest = false
  int testType

  @Input
  String javaSourceDir

  @Input
  String groovySourceDir

  @Input
  String javaTestDir

  @Input
  String groovyTestDir

  @Input
  String currentDirectory

  @Input
  File projectDirectory

  def createClass(String sourceDirectory) {
    def srcDir = new File(getProjectDirectory(), sourceDirectory)
    if(!srcDir.exists()) { 
      new Exception("Source Directory {} does not exist, the java plugin must not be applied for this project or their is a different folder convention you have not set up, full path is {}", sourceDirectory, srcDir)
    }
    prompts()
    (sourceDirectory == getJavaSourceDir()) ? createClassTemplateBuilder(sourceDirectory, "java", "java/java-class.tmpl") : createClassTemplateBuilder(sourceDirectory, "groovy", "groovy/groovy-class.tmpl")
  }
  
  def createClassTemplateBuilder(String sourceDirectory, String classExtension, String templatePath) {
    def classParts = ClassNameUtil.getClassParts(className)
    def template = new TemplateBuilder(getProjectDirectory())
    template.fromDirectory(getCurrentDirectory()) { 
      source (sourceDirectory + "/" + classParts.classPackagePath) {
	"${classParts.className}.${classExtension}" template: templatePath, classPackage: classParts.classPackage, className: classParts.className
      }
      if(makeTest) { 
	if(testType == 0) { 
	  test(getGroovyTestDir() + "/" + classParts.classPackagePath) { 
	    "${classParts.className}Spec.groovy" template: 'groovy/spock-spec.tmpl', classPackage: classParts.classPackage, className: classParts.className
	  }
	} else {
	  test(getJavaTestDir() + "/" + classParts.classPackagePath) { 
	    "${classParts.className}Test.java" template: 'java/junit-test.tmpl', classPackage: classParts.classPackage, className: classParts.className
	  }
	}
      }
    }
  }

  
  def prompts = { 
    className = ConsolePromptUtil.prompt("Full package class name i.e. (com.some.examples.MyClass)")
    if(className) { 
      makeTest = ConsolePromptUtil.promptYesOrNo("Do you want to create a test Class for ${className}?")  
      if(makeTest) { 
	testType = ConsolePromptUtil.promptOptions("What type of test?", 1, ["Spock Test", "Junit Test"])
      }
    } else {
      throw new Exception("No Class Name Provided, please provide a class name")
    }
  }
}