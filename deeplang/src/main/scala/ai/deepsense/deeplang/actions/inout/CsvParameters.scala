package ai.deepsense.deeplang.actions.inout

import ai.deepsense.deeplang.parameters.choice.Choice
import ai.deepsense.deeplang.parameters.choice.ChoiceParameter
import ai.deepsense.deeplang.parameters.validators.SingleCharRegexValidator
import ai.deepsense.deeplang.parameters.BooleanParameter
import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.StringParameter

trait NamesIncludedParam {
  this: Params =>

  val namesIncluded =
    BooleanParameter(name = "names included", description = Some("Does the first row include column names?"))

  setDefault(namesIncluded, true)

  def getNamesIncluded: Boolean = $(namesIncluded)

  def setNamesIncluded(value: Boolean): this.type = set(namesIncluded, value)

}

trait CsvParameters extends NamesIncludedParam {
  this: Params =>

  import CsvParameters._

  val csvColumnSeparator =
    ChoiceParameter[ColumnSeparatorChoice](name = "separator", description = Some("Column separator."))

  setDefault(csvColumnSeparator, ColumnSeparatorChoice.Comma())

  def getCsvColumnSeparator(): ColumnSeparatorChoice = $(csvColumnSeparator)

  def setCsvColumnSeparator(value: ColumnSeparatorChoice): this.type =
    set(csvColumnSeparator, value)

  def determineColumnSeparator(): Char =
    CsvParameters.determineColumnSeparatorOf(getCsvColumnSeparator())

}

object CsvParameters {

  def determineColumnSeparatorOf(choice: ColumnSeparatorChoice): Char = {
    choice match {
      case ColumnSeparatorChoice.Comma()                => ','
      case ColumnSeparatorChoice.Semicolon()            => ';'
      case ColumnSeparatorChoice.Tab()                  => '\t'
      case ColumnSeparatorChoice.Colon()                => ':'
      case ColumnSeparatorChoice.Space()                => ' '
      case (customChoice: ColumnSeparatorChoice.Custom) =>
        customChoice.getCustomColumnSeparator(0)
    }
  }

  sealed trait ColumnSeparatorChoice extends Choice {

    import ColumnSeparatorChoice._

    override val choiceOrder: List[Class[_ <: ColumnSeparatorChoice]] =
      List(classOf[Comma], classOf[Semicolon], classOf[Colon], classOf[Space], classOf[Tab], classOf[Custom])

  }

  object ColumnSeparatorChoice {

    case class Comma() extends ColumnSeparatorChoice {

      override val name = ","

      override val params: Array[ai.deepsense.deeplang.parameters.Parameter[_]] = Array()

    }

    case class Semicolon() extends ColumnSeparatorChoice {

      override val name = ";"

      override val params: Array[ai.deepsense.deeplang.parameters.Parameter[_]] = Array()

    }

    case class Colon() extends ColumnSeparatorChoice {

      override val name = ":"

      override val params: Array[ai.deepsense.deeplang.parameters.Parameter[_]] = Array()

    }

    case class Space() extends ColumnSeparatorChoice {

      override val name = "Space"

      override val params: Array[ai.deepsense.deeplang.parameters.Parameter[_]] = Array()

    }

    case class Tab() extends ColumnSeparatorChoice {

      override val name = "Tab"

      override val params: Array[ai.deepsense.deeplang.parameters.Parameter[_]] = Array()

    }

    case class Custom() extends ColumnSeparatorChoice {

      override val name = "Custom"

      val customColumnSeparator =
        StringParameter(name = "custom separator", description = None, validator = new SingleCharRegexValidator)

      setDefault(customColumnSeparator, ",")

      def getCustomColumnSeparator: String = $(customColumnSeparator)

      def setCustomColumnSeparator(value: String): this.type = set(customColumnSeparator, value)

      override val params: Array[ai.deepsense.deeplang.parameters.Parameter[_]] = Array(customColumnSeparator)

    }

  }

}
