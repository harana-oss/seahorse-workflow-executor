package io.deepsense.deeplang.doperations.inout

import io.deepsense.deeplang.params.FileFormat
import io.deepsense.deeplang.params.Param
import io.deepsense.deeplang.params.choice.Choice

sealed trait OutputFileFormatChoice extends Choice {

  import OutputFileFormatChoice._

  override val choiceOrder: List[Class[_ <: OutputFileFormatChoice]] =
    List(classOf[Csv], classOf[Parquet], classOf[Json])

}

object OutputFileFormatChoice {

  class Csv() extends OutputFileFormatChoice with CsvParameters {

    override val name: String = FileFormat.CSV.toString

    override val params: Array[Param[_]] = Array(csvColumnSeparator, namesIncluded)

  }

  class Parquet() extends OutputFileFormatChoice {

    override val name: String = FileFormat.PARQUET.toString

    override val params: Array[io.deepsense.deeplang.params.Param[_]] = Array()

  }

  class Json() extends OutputFileFormatChoice {

    override val name: String = FileFormat.JSON.toString

    override val params: Array[io.deepsense.deeplang.params.Param[_]] = Array()

  }

}

object OutputFromInputFileFormat {

  def apply(inputFileFormatChoice: InputFileFormatChoice): OutputFileFormatChoice =
    inputFileFormatChoice match {
      case csv: InputFileFormatChoice.Csv =>
        val output = new OutputFileFormatChoice.Csv()
        csv.copyValues(output)
      case json: InputFileFormatChoice.Json       => new OutputFileFormatChoice.Json()
      case parquet: InputFileFormatChoice.Parquet => new OutputFileFormatChoice.Parquet()
      case unsupported =>
        throw new IllegalStateException(
          s"Unsupported input file format $inputFileFormatChoice"
        )
    }

}
