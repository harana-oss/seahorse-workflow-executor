package ai.deepsense.deeplang.actions.inout

import ai.deepsense.deeplang.parameters.FileFormat
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.choice.Choice

sealed trait OutputFileFormatChoice extends Choice {

  import OutputFileFormatChoice._

  override val choiceOrder: List[Class[_ <: OutputFileFormatChoice]] =
    List(classOf[Csv], classOf[Parquet], classOf[Json])

}

object OutputFileFormatChoice {

  class Csv() extends OutputFileFormatChoice with CsvParameters {

    override val name: String = FileFormat.CSV.toString

    override val params: Array[Parameter[_]] = Array(csvColumnSeparator, namesIncluded)

  }

  class Parquet() extends OutputFileFormatChoice {

    override val name: String = FileFormat.PARQUET.toString

    override val params: Array[ai.deepsense.deeplang.parameters.Parameter[_]] = Array()

  }

  class Json() extends OutputFileFormatChoice {

    override val name: String = FileFormat.JSON.toString

    override val params: Array[ai.deepsense.deeplang.parameters.Parameter[_]] = Array()

  }

}

object OutputFromInputFileFormat {

  def apply(inputFileFormatChoice: InputFileFormatChoice): OutputFileFormatChoice =
    inputFileFormatChoice match {
      case csv: InputFileFormatChoice.Csv         =>
        val output = new OutputFileFormatChoice.Csv()
        csv.copyValues(output)
      case json: InputFileFormatChoice.Json       => new OutputFileFormatChoice.Json()
      case parquet: InputFileFormatChoice.Parquet => new OutputFileFormatChoice.Parquet()
      case unsupported                            =>
        throw new IllegalStateException(
          s"Unsupported input file format $inputFileFormatChoice"
        )
    }

}
