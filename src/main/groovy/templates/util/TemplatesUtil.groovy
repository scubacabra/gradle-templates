package templates.util

import groovy.text.GStringTemplateEngine

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

class TemplatesUtil {
  
  static final log = Logging.getLogger(TemplatesUtil.class)

  // static void exportTemplates(def templates = []) {
  //   ProjectTemplate.fromUserDir {
  //     templates.each { template ->
  // 	def tStream = getClass().getResourceAsStream(template)
  // 	"$template" tStream.text
  //     }
  //   }
  // }

  static String renderTemplate(String template, Map templateParams) {
    log.debug("Rendering template - path: {}, params: {}", template, templateParams)
    def templateLocation = TemplatesUtil.class.getResource("/templates/" + template)
    log.debug("template url is {}", templateLocation)
    def templateReader = templateLocation?.newReader()
    log.debug("template Reader is {}", templateReader)
    if (templateReader) {
      return new GStringTemplateEngine().createTemplate(templateReader)?.make(templateParams)?.toString()
    } else {
      throw new RuntimeException("Could not locate template suckaaaa: ${template}")
    }
  }

}