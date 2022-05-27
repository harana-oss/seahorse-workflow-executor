package ai.deepsense.deeplang.actionobjects.descriptions

import org.apache.spark.sql.types.StructType
import spray.json.JsValue

sealed trait InferenceResult

case class DataFrameInferenceResult(schema: StructType) extends InferenceResult

case class ParamsInferenceResult(
    schema: JsValue,
    values: JsValue
) extends InferenceResult
