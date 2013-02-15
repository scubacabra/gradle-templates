package templates.model

import templates.util.TemplatesUtil

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

class TemplateBuilder {
  static final log = Logging.getLogger(TemplateBuilder.class)

  private File parentDir

  public TemplateBuilder(File startingDirectory) { 
    parentDir = startingDirectory
    log.debug("starting directory is {}", parentDir.path)
  }

  public void fromDirectory(String templateDirectory, Closure template = {}) { 
    template.delegate = this
    log.debug("making a template at directory {}", templateDirectory)
    makeDirectory(templateDirectory, template)
  }

  private void src(Closure sourceDirectory = {}) { 
    source("src", sourceDirectory)
  }

  private void source(String sourcePath, Closure sourceDirectory = {}) { 
    makeDirectory(sourcePath, sourceDirectory)
  }

  private void main(Closure mainDirectory = {}) { 
    makeDirectory("main", mainDirectory)
  }
  
  private void test(Closure testDirectory = {}) { 
    test("test", testDirectory)
  }

  private void test(String testPath, Closure testDirectory = {}) { 
    makeDirectory(testPath, testDirectory)
  }

  def methodMissing(String name, arguments) {
    if (name in ['java', 'groovy']) { 
      makeDirectory(name, {})
    } else { 
      if(arguments) { 
	makeFile(name, arguments[0])
      }
      else { 
	throw InvalidArgumentException("arguments need to be present in map form for file generation")
      }
    }
  }

  private void makeFile(String fileName, Map arguments = [:]) { 
    log.debug("trying to make file {} with arguments to Templates {}", fileName, arguments)
    def file = new File(parentDir, fileName)
    file.exists() ?: file.parentFile.mkdirs() && file.createNewFile()
    def fileText = TemplatesUtil.renderTemplate(arguments.template, arguments)
    file.text = fileText
  }

  private void makeDirectory(String directory, Closure closure) { 
    File oldParent = parentDir
    def dir = new File(parentDir, directory)
    log.debug("dir is {}", dir)
    if(!dir.exists()) { 
      log.debug("mkdirs {}", dir)
      dir.mkdirs()
    }
    parentDir = dir
    closure()
    parentDir = oldParent
  }

}