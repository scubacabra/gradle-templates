package templates.model

import spock.lang.Specification

class TemplateBuilderSpec extends Specification {
  
  def template = new TemplateBuilder(new File("./build"))
  //  def file = Mock(File)

  def "simple builder" () {
  when: "simple test"
    template.fromDirectory("test-project") {
      source 'src/main/java'
      test "src/test/java"
      'build.gradle' template: 'java/build.gradle.tmpl', projectGroup: "yomamam"
    }

  then: "should call mkdirs twice"
    //    2 * file.mkdirs()
  }

  def "simple builder 2" () {
  when: "simple test"
    template.fromDirectory("test-project-multi") { 
      'build.gradle' template: 'multiBuild/build.gradle.tmpl'
      'settings.gradle' template: 'multiBuild/settings.gradle.tmpl'
      'subProjectChecks.gradle' template: 'multiBuild/subProjectChecks.gradle.tmpl'
    }

  then: "should call mkdirs twice"
    //    2 * file.mkdirs()
  }
}