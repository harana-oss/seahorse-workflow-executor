package ai.deepsense.deeplang.actions.inout

import ai.deepsense.deeplang.parameters.FileFormat
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.choice.Choice

sealed trait InputFileFormatChoice extends Choice {

  override val choiceOrder: List[Class[_ <: InputFileFormatChoice]] =
    InputFileFormatChoice.choiceOrder

}

object InputFileFormatChoice {

  class Csv() extends InputFileFormatChoice with CsvParameters with HasShouldConvertToBooleanParam {

    override val name: String = FileFormat.CSV.toString

    override val params: Array[Parameter[_]] =
      Array(csvColumnSeparator, namesIncluded, shouldConvertToBoolean)

  }

  class Parquet() extends InputFileFormatChoice {

    override val name: String = FileFormat.PARQUET.toString

    override val params: Array[ai.deepsense.deeplang.parameters.Parameter[_]] = Array()

  }

  class Json() extends InputFileFormatChoice {

    override val name: String = FileFormat.JSON.toString

    override val params: Array[ai.deepsense.deeplang.parameters.Parameter[_]] = Array()

  }

  val choiceOrder: List[Class[_ <: InputFileFormatChoice]] = List(
    classOf[Csv],
    classOf[Parquet],
    classOf[Json]
  )

}
