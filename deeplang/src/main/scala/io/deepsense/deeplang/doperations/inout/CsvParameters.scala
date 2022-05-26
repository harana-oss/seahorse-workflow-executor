package io.deepsense.deeplang.doperations.inout

import io.deepsense.deeplang.params.choice.Choice
import io.deepsense.deeplang.params.choice.ChoiceParam
import io.deepsense.deeplang.params.validators.SingleCharRegexValidator
import io.deepsense.deeplang.params.BooleanParam
import io.deepsense.deeplang.params.Params
import io.deepsense.deeplang.params.StringParam

trait NamesIncludedParam {
  this: Params =>

  val namesIncluded =
    BooleanParam(name = "names included", description = Some("Does the first row include column names?"))

  setDefault(namesIncluded, true)

  def getNamesIncluded: Boolean = $(namesIncluded)

  def setNamesIncluded(value: Boolean): this.type = set(namesIncluded, value)

}

trait CsvParameters extends NamesIncludedParam {
  this: Params =>

  import CsvParameters._

  val csvColumnSeparator =
    ChoiceParam[ColumnSeparatorChoice](name = "separator", description = Some("Column separator."))

  setDefault(csvColumnSeparator, ColumnSeparatorChoice.Comma())

  def getCsvColumnSeparator(): ColumnSeparatorChoice = $(csvColumnSeparator)

  def setCsvColumnSeparator(value: ColumnSeparatorChoice): this.type =
    set(csvColumnSeparator, value)

  def determineColumnSeparator(): Char =
    CsvParameters.determineColumnSeparatorOf(getCsvColumnSeparator())

}

object CsvParameters {

  def determineColumnSeparatorOf(choice: ColumnSeparatorChoice): Char =
    choice match {
      case ColumnSeparatorChoice.Comma()     => ','
      case ColumnSeparatorChoice.Semicolon() => ';'
      case ColumnSeparatorChoice.Tab()       => '\t'
      case ColumnSeparatorChoice.Colon()     => ':'
      case ColumnSeparatorChoice.Space()     => ' '
      case (customChoice: ColumnSeparatorChoice.Custom) =>
        customChoice.getCustomColumnSeparator(0)
    }

  sealed trait ColumnSeparatorChoice extends Choice {

    import ColumnSeparatorChoice._

    override val choiceOrder: List[Class[_ <: ColumnSeparatorChoice]] =
      List(classOf[Comma], classOf[Semicolon], classOf[Colon], classOf[Space], classOf[Tab], classOf[Custom])

  }

  object ColumnSeparatorChoice {

    case class Comma() extends ColumnSeparatorChoice {

      override val name = ","

      override val params: Array[io.deepsense.deeplang.params.Param[_]] = Array()

    }

    case class Semicolon() extends ColumnSeparatorChoice {

      override val name = ";"

      override val params: Array[io.deepsense.deeplang.params.Param[_]] = Array()

    }

    case class Colon() extends ColumnSeparatorChoice {

      override val name = ":"

      override val params: Array[io.deepsense.deeplang.params.Param[_]] = Array()

    }

    case class Space() extends ColumnSeparatorChoice {

      override val name = "Space"

      override val params: Array[io.deepsense.deeplang.params.Param[_]] = Array()

    }

    case class Tab() extends ColumnSeparatorChoice {

      override val name = "Tab"

      override val params: Array[io.deepsense.deeplang.params.Param[_]] = Array()

    }

    case class Custom() extends ColumnSeparatorChoice {

      override val name = "Custom"

      val customColumnSeparator =
        StringParam(name = "custom separator", description = None, validator = new SingleCharRegexValidator)

      setDefault(customColumnSeparator, ",")

      def getCustomColumnSeparator: String = $(customColumnSeparator)

      def setCustomColumnSeparator(value: String): this.type = set(customColumnSeparator, value)

      override val params: Array[io.deepsense.deeplang.params.Param[_]] = Array(customColumnSeparator)

    }

  }

}
