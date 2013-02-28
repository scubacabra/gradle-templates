package templates.model

import templates.util.TemplatesUtil
import templates.util.TextFileUtil

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
    log.debug("create source Path {}", sourcePath)
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
    if (name in ['java', 'groovy', 'resources']) {
      makeDirectory(name, {})
    } else { 
      if(arguments) { 
	log.debug("adding file {}, with arguments {}", name, arguments[0])
	makeFile(name, arguments[0])
      }
      else { 
	throw InvalidArgumentException("arguments need to be present in map or String form for file generation")
      }
    }
  }

  private void makeFile(String fileName, args) {
    log.debug("opening file object with name {}", fileName)
    def file = new File(parentDir, fileName)
    file.exists() ?: file.parentFile.mkdirs() && file.createNewFile()
    def text = generateTextForFile(args)
    if(args instanceof Map) { 
      if(args.append) { 
	TextFileUtil.appendTextToFile(file, text)
      } else if (args.prepend) {
	TextFileUtil.prependTextToFile(file, text)
      } else {
	TextFileUtil.addTextToFile(file, text)
      }
    } else {
      TextFileUtil.addTextToFile(file, text)
    }
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

  private String generateTextForFile(args) {
    String text
    if(args instanceof Map) {
      if (args.template) {
	text = TemplatesUtil.renderTemplate(args.template, args)
      } else if (args.content) {
	text = TemplatesUtil.renderString(args.content)
      }
    } else {
      text = TemplatesUtil.renderString(args)
    }
    log.debug("text to return from template/string is::: {}", text)
    return text
  }
}