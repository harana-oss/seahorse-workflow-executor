package ai.deepsense.deeplang.actionobjects.wrappers

import org.apache.spark.ml.evaluation
import org.apache.spark.ml.param.Param
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.ml.util.Identifiable
import org.apache.spark.sql

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.actionobjects.Evaluator
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.parameters.wrappers.deeplang.ParamWrapper
import ai.deepsense.sparkutils.ML

class EvaluatorWrapper(context: ExecutionContext, evaluator: Evaluator) extends ML.Evaluator {

  override def evaluateDF(dataset: sql.DataFrame): Double =
    evaluator.evaluate(context)(())(DataFrame.fromSparkDataFrame(dataset.toDF())).value

  override def copy(extra: ParamMap): evaluation.Evaluator = {
    val params        = ParamTransformer.transform(extra)
    val evaluatorCopy = evaluator.replicate().set(params: _*)
    new EvaluatorWrapper(context, evaluatorCopy)
  }

  override lazy val params: Array[Param[_]] =
    evaluator.params.map(new ParamWrapper(uid, _))

  override def isLargerBetter: Boolean = evaluator.isLargerBetter

  override val uid: String = Identifiable.randomUID("EvaluatorWrapper")

}
