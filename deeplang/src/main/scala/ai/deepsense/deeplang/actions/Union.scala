package ai.deepsense.deeplang.actions

import scala.reflect.runtime.{universe => ru}

import org.apache.spark.sql.types.StructType

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang.PortPosition.PortPosition
import ai.deepsense.deeplang.documentation.OperationDocumentation
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actions.exceptions.SchemaMismatchException
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.Action2To1
import ai.deepsense.deeplang.PortPosition
import ai.deepsense.deeplang.DataFrame2To1Operation
import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.sparkutils.SQL

case class Union()
    extends Action2To1[DataFrame, DataFrame, DataFrame]
    with DataFrame2To1Operation
    with Params
    with OperationDocumentation {

  override val id: Id = "90fed07b-d0a9-49fd-ae23-dd7000a1d8ad"

  override val name: String = "Union"

  override val description: String =
    "Creates a DataFrame containing all rows from both input DataFrames"

  override def inPortsLayout: Vector[PortPosition] =
    Vector(PortPosition.Left, PortPosition.Right)

  override val since: Version = Version(0, 4, 0)

  val specificParams: Array[ai.deepsense.deeplang.parameters.Parameter[_]] = Array()

  override protected def execute(first: DataFrame, second: DataFrame)(context: ExecutionContext): DataFrame = {

    inferSchema(first.schema.get, second.schema.get)

    context.dataFrameBuilder.buildDataFrame(
      first.schema.get,
      SQL.union(first.sparkDataFrame, second.sparkDataFrame).rdd
    )
  }

  override protected def inferSchema(
      leftSchema: StructType,
      rightSchema: StructType
  ): (StructType, InferenceWarnings) = {
    if (leftSchema.treeString != rightSchema.treeString) {
      throw new SchemaMismatchException(
        "SchemaMismatch. Expected schema " +
          s"${leftSchema.treeString}" +
          s" differs from ${rightSchema.treeString}"
      )
    }
    (leftSchema, InferenceWarnings.empty)
  }

  @transient
  override lazy val tTagTI_0: ru.TypeTag[DataFrame] = ru.typeTag[DataFrame]

  @transient
  override lazy val tTagTI_1: ru.TypeTag[DataFrame] = ru.typeTag[DataFrame]

  @transient
  override lazy val tTagTO_0: ru.TypeTag[DataFrame] = ru.typeTag[DataFrame]

}
