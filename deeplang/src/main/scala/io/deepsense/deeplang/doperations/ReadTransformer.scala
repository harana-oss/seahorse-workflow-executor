package io.deepsense.deeplang.doperations

import java.io._

import scala.reflect.runtime.{universe => ru}

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.documentation.OperationDocumentation
import io.deepsense.deeplang.doperables.Transformer
import io.deepsense.deeplang.doperations.ReadTransformer.ReadTransformerParameters
import io.deepsense.deeplang.doperations.exceptions.DeepSenseIOException
import io.deepsense.deeplang.params.{Params, StringParam}
import io.deepsense.deeplang.{DOperation0To1, ExecutionContext}

case class ReadTransformer()
  extends DOperation0To1[Transformer]
  with Params
  with ReadTransformerParameters
  with OperationDocumentation {

  override val id: Id = "424dc996-a471-482d-b08c-bc12849f0b68"
  override val name: String = "Read Transformer"
  override val description: String = "Reads a Transformer from a directory"

  override val since: Version = Version(1, 1, 0)

  val params: Array[io.deepsense.deeplang.params.Param[_]] = Array(sourcePath)

  override protected def execute()(context: ExecutionContext): Transformer = {
    val path = getSourcePath
    try {
      Transformer.load(context, path)
    } catch {
      case e: IOException => throw DeepSenseIOException(e)
    }
  }

  @transient
  override lazy val tTagTO_0: ru.TypeTag[Transformer] = ru.typeTag[Transformer]
}

object ReadTransformer {
  trait ReadTransformerParameters {
    this: Params =>

    val sourcePath = StringParam(
      name = "source",
      description = Some("A path to the Transformer directory."))

    def getSourcePath: String = $(sourcePath)
    def setSourcePath(value: String): this.type = set(sourcePath, value)
  }

  def apply(sourcePath: String): ReadTransformer = {
    new ReadTransformer().setSourcePath(sourcePath)
  }
}
