package ai.deepsense.deeplang.actions

import java.io._

import scala.reflect.runtime.{universe => ru}

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang.documentation.OperationDocumentation
import ai.deepsense.deeplang.actionobjects.Transformer
import ai.deepsense.deeplang.actions.ReadTransformer.ReadTransformerParameters
import ai.deepsense.deeplang.actions.exceptions.HaranaIOException
import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.StringParameter
import ai.deepsense.deeplang.Action0To1
import ai.deepsense.deeplang.ExecutionContext

case class ReadTransformer()
    extends Action0To1[Transformer]
    with Params
    with ReadTransformerParameters
    with OperationDocumentation {

  override val id: Id = "424dc996-a471-482d-b08c-bc12849f0b68"

  override val name: String = "Read Transformer"

  override val description: String = "Reads a Transformer from a directory"

  override val since: Version = Version(1, 1, 0)

  val specificParams: Array[ai.deepsense.deeplang.parameters.Parameter[_]] = Array(sourcePath)

  override protected def execute()(context: ExecutionContext): Transformer = {
    val path = getSourcePath
    try
      Transformer.load(context, path)
    catch {
      case e: IOException => throw HaranaIOException(e)
    }
  }

  @transient
  override lazy val tTagTO_0: ru.TypeTag[Transformer] = ru.typeTag[Transformer]

}

object ReadTransformer {

  trait ReadTransformerParameters {
    this: Params =>

    val sourcePath = StringParameter(name = "source", description = Some("A path to the Transformer directory."))

    def getSourcePath: String = $(sourcePath)

    def setSourcePath(value: String): this.type = set(sourcePath, value)

  }

  def apply(sourcePath: String): ReadTransformer =
    new ReadTransformer().setSourcePath(sourcePath)

}
