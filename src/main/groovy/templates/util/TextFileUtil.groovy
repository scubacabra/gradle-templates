package templates.util

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

class TextFileUtil {
  static final log = Logging.getLogger(TextFileUtil.class)
  
  static void prependPlugin(String plugin, File gradleFile) {
    prependTextToFile("apply plugin: '${plugin}'", gradleFile)
  }
  
  static void prependTextToFile(File file, String text) {
    log.debug("prepend {} to {}", text, file)
    def currentText = file.exists() ? file.text : ''
    file.withPrintWriter { pw ->
      pw.print text
      pw.print currentText
    }
  }

  static void appendTextToFile(File file, String text) {
    log.debug("append {} to {}", text, file)
    def oldText = file.exists() ? file.text : ''
    file.withPrintWriter { pw ->
      pw.print oldText
      pw.print text
    }
  }

  static void addTextToFile(File file, String text) {
    log.debug("add content {} to {}", text, file)
    file.text = text
  }
}