package templates.util

import groovy.text.GStringTemplateEngine

import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

import java.io.Reader
import java.io.StringReader

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
    def templateReader = getReader(templateLocation)
    return render(templateReader, templateParams)
  }

  static String renderString(String render) {
    log.debug("Rendering String - {}", render)
    def stringReader = getReader(render)
    if(stringReader) {
      return new GStringTemplateEngine().createTemplate(stringReader).make([:]).toString()
    } else {
      throw new RuntimeException("Could not locate stringReader: ${stringReader}")
    }
  }

  static String render(Reader reader, Map templateParams = [:]) {
    if(reader) {
      return new GStringTemplateEngine().createTemplate(reader).make(templateParams).toString()
    } else {
      throw new RuntimeException("Could not locate reader: ${reader}")
    }
  }

  static Reader getReader(URL urlLocation) {
    def UrlReader = urlLocation?.newReader()
    log.debug("url Reader is {}", UrlReader)
    return UrlReader
  }

  static Reader getReader(String string) {
    def stringReader = new StringReader(string)
    log.debug("String Reader is {}", stringReader)
    return stringReader
  }
}