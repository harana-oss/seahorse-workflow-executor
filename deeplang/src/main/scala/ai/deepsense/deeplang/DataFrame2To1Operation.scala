package ai.deepsense.deeplang

import org.apache.spark.sql.types.StructType

import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings

import ai.deepsense.deeplang.PortPosition.PortPosition

trait DataFrame2To1Operation { self: Action2To1[DataFrame, DataFrame, DataFrame] =>

  override def inPortsLayout: Vector[PortPosition] =
    Vector(PortPosition.Left, PortPosition.Right)

  final override protected def inferKnowledge(
                                               leftDataFrameKnowledge: Knowledge[DataFrame],
                                               rightDataFrameKnowledge: Knowledge[DataFrame]
  )(context: InferContext): (Knowledge[DataFrame], InferenceWarnings) = {

    val leftSchema  = leftDataFrameKnowledge.single.schema
    val rightSchema = rightDataFrameKnowledge.single.schema

    if (leftSchema.isDefined && rightSchema.isDefined) {
      val (outputSchema, warnings) = inferSchema(leftSchema.get, rightSchema.get)
      (Knowledge(DataFrame.forInference(outputSchema)), warnings)
    } else
      (Knowledge(DataFrame.forInference()), InferenceWarnings.empty)
  }

  protected def inferSchema(leftSchema: StructType, rightSchema: StructType): (StructType, InferenceWarnings) =
    (StructType(Seq.empty), InferenceWarnings.empty)

}
