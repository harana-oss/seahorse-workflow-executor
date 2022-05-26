package io.deepsense.deeplang.doperables.wrappers

import org.apache.spark.ml.evaluation
import org.apache.spark.ml.param.Param
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.ml.util.Identifiable
import org.apache.spark.sql

import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.doperables.Evaluator
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.params.wrappers.deeplang.ParamWrapper
import io.deepsense.sparkutils.ML

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
