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
    if (name in ['java', 'groovy']) { 
      makeDirectory(name, {})
    } else { 
      if(arguments) { 
	log.debug("adding file {}, with arguments {}", name, arguments[0])
	makeFile(name, arguments[0])
      }
      else { 
	throw InvalidArgumentException("arguments need to be present in map form for file generation")
      }
    }
  }

  private void makeFile(String fileName, args) {
    log.debug("opening file object with name {}", fileName)
    def file = new File(parentDir, fileName)
    file.exists() ?: file.parentFile.mkdirs() && file.createNewFile()
    def text
    if(args instanceof Map) {
      if (args.template) {
	text = makeTemplate(args.template, args)
      } else if (args.content) {
	text = makeTemplate(args.content)
      }
    } else {
      text = makeTemplate(args)
    }

    if(args instanceof Map) { 
      if(args.append) { 
	log.debug("append {} to {}", text, file)
	appendTextToFile(file, text)
      } else if (args.prepend) {
	log.debug("prepend {} to {}", text, file)
	prependTextToFile(file, text)
      } else {
	log.debug("add content {} to {}", text, file)
	addTextToFile(file, text)      
      }
    } else {
      log.debug("add {} to {}", text, file)
      addTextToFile(file, text)      
    }
  }

  private void addTextToFile(File file, String text) {
    file.text = text
  }

  private void prependTextToFile(File file, String text) { 
    def oldText = file.exists() ? file.text : ''
    file.withPrintWriter { pw -> 
      pw.print text
      pw.print oldText
    }
  }

  private void appendTextToFile(File file, String text) { 
    def oldText = file.exists() ? file.text : ''
    file.withPrintWriter { pw -> 
      pw.print oldText
      pw.print text
    }
  }

  private String makeTemplate(String templateURL, Map arguments) {
    log.debug("trying to make templates with template URL {} with arguments {}", templateURL, arguments)
    return TemplatesUtil.renderTemplate(templateURL, arguments)
  }

  private String makeTemplate(String content) {
    log.debug("trying to render content {}", content)
    return TemplatesUtil.renderString(content)
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