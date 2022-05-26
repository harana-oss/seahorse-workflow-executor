package ai.deepsense.deeplang.doperables

import scala.reflect.runtime.universe._

import org.apache.spark.ml

import ai.deepsense.deeplang.doperables.dataframe.DataFrame
import ai.deepsense.deeplang.params.wrappers.spark.ParamsWithSparkWrappers
import ai.deepsense.deeplang.DKnowledge
import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.TypeUtils

/** Wrapper for creating deeplang Evaluators from spark ml Evaluators. It is parametrized by evaluator type.
  *
  * @tparam E
  *   Type of wrapped ml.evaluation.Evaluator
  */
abstract class SparkEvaluatorWrapper[E <: ml.evaluation.Evaluator](implicit val evaluatorTag: TypeTag[E])
    extends Evaluator
    with ParamsWithSparkWrappers {

  val sparkEvaluator: E = createEvaluatorInstance()

  def getMetricName: String

  override def _evaluate(context: ExecutionContext, dataFrame: DataFrame): MetricValue = {
    val sparkParams = sparkParamMap(sparkEvaluator, dataFrame.sparkDataFrame.schema)
    val value       = sparkEvaluator.evaluate(dataFrame.sparkDataFrame, sparkParams)
    MetricValue(getMetricName, value)
  }

  override def _infer(k: DKnowledge[DataFrame]): MetricValue = {
    k.single.schema.foreach(sparkParamMap(sparkEvaluator, _))
    MetricValue.forInference(getMetricName)
  }

  def createEvaluatorInstance(): E = TypeUtils.instanceOfType(evaluatorTag)

  override def isLargerBetter: Boolean = sparkEvaluator.isLargerBetter

}
