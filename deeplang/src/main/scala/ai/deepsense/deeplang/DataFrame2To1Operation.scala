package ai.deepsense.deeplang

import org.apache.spark.sql.types.StructType

import ai.deepsense.deeplang.doperables.dataframe.DataFrame
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings

import ai.deepsense.deeplang.DPortPosition.DPortPosition

trait DataFrame2To1Operation { self: DOperation2To1[DataFrame, DataFrame, DataFrame] =>

  override def inPortsLayout: Vector[DPortPosition] =
    Vector(DPortPosition.Left, DPortPosition.Right)

  final override protected def inferKnowledge(
      leftDataFrameKnowledge: DKnowledge[DataFrame],
      rightDataFrameKnowledge: DKnowledge[DataFrame]
  )(context: InferContext): (DKnowledge[DataFrame], InferenceWarnings) = {

    val leftSchema  = leftDataFrameKnowledge.single.schema
    val rightSchema = rightDataFrameKnowledge.single.schema

    if (leftSchema.isDefined && rightSchema.isDefined) {
      val (outputSchema, warnings) = inferSchema(leftSchema.get, rightSchema.get)
      (DKnowledge(DataFrame.forInference(outputSchema)), warnings)
    } else
      (DKnowledge(DataFrame.forInference()), InferenceWarnings.empty)
  }

  protected def inferSchema(leftSchema: StructType, rightSchema: StructType): (StructType, InferenceWarnings) =
    (StructType(Seq.empty), InferenceWarnings.empty)

}
