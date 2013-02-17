package templates.util

class ClassNameUtil {
  /**
   * Pulls a fully qualified classname into it's parts - package, and name.
   * @param fullClassName
   * @return Map containing the classname, package, and package as a path.
   */
  static def getClassParts(String fullClassName) {
    def classParts = fullClassName.split(/\./) as List
    [
    className: classParts.pop(),
    classPackagePath: classParts.join(File.separator),
    classPackage: classParts.join('.')
    ]
  }

}