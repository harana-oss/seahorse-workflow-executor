package io.deepsense.deeplang.params

object ParameterType extends Enumeration {

  type ParameterType = Value

  val Boolean = Value("boolean")

  val Numeric = Value("numeric")

  val MultipleNumeric = Value("multipleNumeric")

  val String = Value("string")

  val Choice = Value("choice")

  val MultipleChoice = Value("multipleChoice")

  val Multiplier = Value("multiplier")

  val ColumnSelector = Value("selector")

  val SingleColumnCreator = Value("creator")

  val MultipleColumnCreator = Value("multipleCreator")

  val PrefixBasedColumnCreator = Value("prefixBasedCreator")

  val CodeSnippet = Value("codeSnippet")

  val Dynamic = Value("dynamic")

  val Workflow = Value("workflow")

  val GridSearch = Value("gridSearch")

  val LoadFromLibrary = Value("loadFromLibrary")

  val SaveToLibrary = Value("saveToLibrary")

  val DatasourceIdForRead = Value("datasourceIdForRead")

  val DatasourceIdForWrite = Value("datasourceIdForWrite")

}
