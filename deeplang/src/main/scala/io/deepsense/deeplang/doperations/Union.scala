package io.deepsense.deeplang.doperations

import scala.reflect.runtime.{universe => ru}

import org.apache.spark.sql.types.StructType

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.DPortPosition.DPortPosition
import io.deepsense.deeplang.documentation.OperationDocumentation
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperations.exceptions.SchemaMismatchException
import io.deepsense.deeplang.inference.InferenceWarnings
import io.deepsense.deeplang.params.Params
import io.deepsense.deeplang.DOperation2To1
import io.deepsense.deeplang.DPortPosition
import io.deepsense.deeplang.DataFrame2To1Operation
import io.deepsense.deeplang.ExecutionContext
import io.deepsense.sparkutils.SQL

case class Union()
    extends DOperation2To1[DataFrame, DataFrame, DataFrame]
    with DataFrame2To1Operation
    with Params
    with OperationDocumentation {

  override val id: Id = "90fed07b-d0a9-49fd-ae23-dd7000a1d8ad"

  override val name: String = "Union"

  override val description: String =
    "Creates a DataFrame containing all rows from both input DataFrames"

  override def inPortsLayout: Vector[DPortPosition] =
    Vector(DPortPosition.Left, DPortPosition.Right)

  override val since: Version = Version(0, 4, 0)

  val params: Array[io.deepsense.deeplang.params.Param[_]] = Array()

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
    if (leftSchema.treeString != rightSchema.treeString)
      throw new SchemaMismatchException(
        "SchemaMismatch. Expected schema " +
          s"${leftSchema.treeString}" +
          s" differs from ${rightSchema.treeString}"
      )
    (leftSchema, InferenceWarnings.empty)
  }

  @transient
  override lazy val tTagTI_0: ru.TypeTag[DataFrame] = ru.typeTag[DataFrame]

  @transient
  override lazy val tTagTI_1: ru.TypeTag[DataFrame] = ru.typeTag[DataFrame]

  @transient
  override lazy val tTagTO_0: ru.TypeTag[DataFrame] = ru.typeTag[DataFrame]

}
