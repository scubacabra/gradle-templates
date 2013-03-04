package templates.util

import org.gradle.api.GradleException

class ConsolePromptUtil {
  static final String lineSep = System.getProperty("line.separator")
  static final String inputPrompt = "${lineSep}??>"

  static String prompt(String message, String defaultValue = null) {
    readLine(message, defaultValue)
  }

  static int promptOptions(String message, List options = []) {
    promptOptions(message, 0, options)
  }

  static int promptOptions(String message, int defaultValue, List options = []) {
    String consoleMessage = "${inputPrompt} ${message}"
    consoleMessage += "${lineSep}    Pick an option ${1..options.size()}"

    options.eachWithIndex { option, index ->
      consoleMessage += "${lineSep}     (${index + 1}): ${option}"
    }

    if (defaultValue) {
      consoleMessage += "${inputPrompt} [${defaultValue}] "
    } else {
      consoleMessage += "${inputPrompt} "
    }

    try {
      def range = 0..options.size() - 1
      int choice = Integer.parseInt(readLine(consoleMessage, defaultValue))
      if (choice == 0) {
	throw new GradleException('No option provided')
      }
      choice--
      if (range.containsWithinBounds(choice)) {
	return choice
      } else {
	throw new IllegalArgumentException('Option is not valid.')
      }
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException('Option is not valid.', e)
    }
  }

  static boolean promptYesOrNo(String message, boolean defaultValue = false) {
    def defaultStr = defaultValue ? 'Y' : 'n'
    String consoleVal = readLine("${message} (Y|n)", defaultValue)
    if (consoleVal) {
      return consoleVal.toLowerCase().startsWith('y')
    }
    return defaultValue
  }

  private static readLine(String message, def defaultValue = null) {
    String _message = "$inputPrompt $message " + (defaultValue ? "[$defaultValue] " : "")
    if (System.console()) {
      return System.console().readLine(_message) ?: defaultValue
    }
    println "$_message (WAITING FOR INPUT BELOW)"
    return System.in.newReader().readLine() ?: defaultValue
  }
}