package io.deepsense.deeplang.doperations.inout

import io.deepsense.deeplang.params.{FileFormat, Param}
import io.deepsense.deeplang.params.choice.Choice

sealed trait InputFileFormatChoice extends Choice {
  override val choiceOrder: List[Class[_ <: InputFileFormatChoice]] =
    InputFileFormatChoice.choiceOrder
}

object InputFileFormatChoice {
  class Csv()
    extends InputFileFormatChoice
    with CsvParameters
    with HasShouldConvertToBooleanParam {

    override val name: String = FileFormat.CSV.toString
    override val params: Array[Param[_]] =
      Array(
        csvColumnSeparator,
        namesIncluded,
        shouldConvertToBoolean)
  }
  class Parquet() extends InputFileFormatChoice {
    override val name: String = FileFormat.PARQUET.toString
    override val params: Array[io.deepsense.deeplang.params.Param[_]] = Array()
  }
  class Json() extends InputFileFormatChoice {
    override val name: String = FileFormat.JSON.toString
    override val params: Array[io.deepsense.deeplang.params.Param[_]] = Array()
  }

  val choiceOrder: List[Class[_ <: InputFileFormatChoice]] = List(
    classOf[Csv],
    classOf[Parquet],
    classOf[Json]
  )
}
