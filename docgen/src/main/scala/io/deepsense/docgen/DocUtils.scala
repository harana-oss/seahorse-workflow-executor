package io.deepsense.docgen

object DocUtils {

  def underscorize(input: String): String = input.toLowerCase.replace(' ', '_')

  def forceDotAtEnd(s: String): String =
    if (s.charAt(s.length - 1) != '.')
      s + "."
    else
      s

}
