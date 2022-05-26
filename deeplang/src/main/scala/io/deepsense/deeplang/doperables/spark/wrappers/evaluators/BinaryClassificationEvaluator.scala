package io.deepsense.deeplang.doperables.spark.wrappers.evaluators

import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics
import org.apache.spark.mllib.evaluation.MulticlassMetrics
import org.apache.spark.sql.Row

import io.deepsense.commons.types.ColumnType
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperables.dataframe.DataFrameColumnsGetter
import io.deepsense.deeplang.doperables.spark.wrappers.evaluators.BinaryClassificationEvaluator._
import io.deepsense.deeplang.doperables.spark.wrappers.params.common.HasLabelColumnParam
import io.deepsense.deeplang.doperables.Evaluator
import io.deepsense.deeplang.doperables.MetricValue
import io.deepsense.deeplang.params.choice.Choice
import io.deepsense.deeplang.params.choice.ChoiceParam
import io.deepsense.deeplang.params.selections.NameSingleColumnSelection
import io.deepsense.deeplang.params.selections.SingleColumnSelection
import io.deepsense.deeplang.params.wrappers.spark.ParamsWithSparkWrappers
import io.deepsense.deeplang.params.Param
import io.deepsense.deeplang.params.Params
import io.deepsense.deeplang.params.SingleColumnSelectorParam
import io.deepsense.deeplang.DKnowledge
import io.deepsense.deeplang.ExecutionContext
import io.deepsense.sparkutils.Linalg.Vector

class BinaryClassificationEvaluator extends Evaluator with ParamsWithSparkWrappers with HasLabelColumnParam {

  val metricName = new ChoiceParam[Metric](name = "binary metric", description = Some("The metric used in evaluation."))

  setDefault(metricName, AreaUnderROC())

  override val params: Array[Param[_]] = Array(metricName, labelColumn)

  def getMetricName: String = $(metricName).name

  def setMetricName(value: Metric): this.type = set(metricName, value)

  override def _evaluate(context: ExecutionContext, dataFrame: DataFrame): MetricValue = {
    val labelColumnName = dataFrame.getColumnName($(labelColumn))
    val metric = $(metricName) match {
      case rawPredictionChoice: RawPredictionMetric =>
        evaluateRawPrediction(dataFrame, labelColumnName, rawPredictionChoice)
      case predChoice: PredictionMetric =>
        evaluatePrediction(dataFrame, labelColumnName, predChoice)
    }
    MetricValue(getMetricName, metric)
  }

  private def evaluateRawPrediction(
      dataFrame: DataFrame,
      labelColumnName: String,
      rawChoice: RawPredictionMetric
  ): Double = {
    val rawPredictionColumnName = dataFrame.getColumnName(rawChoice.getRawPredictionColumnParam)
    val scoreAndLabels = dataFrame.sparkDataFrame.select(rawPredictionColumnName, labelColumnName).rdd.map {
      case Row(rawPrediction: Vector, label: Double) =>
        (rawPrediction(1), label)
    }
    val metrics = new BinaryClassificationMetrics(scoreAndLabels)
    val metric = rawChoice match {
      case areaUnderROCChoice: AreaUnderROC => metrics.areaUnderROC()
      case areaUnderPRChoice: AreaUnderPR   => metrics.areaUnderPR()
    }
    metrics.unpersist()
    metric
  }

  private def evaluatePrediction(
      dataFrame: DataFrame,
      labelColumnName: String,
      predChoice: PredictionMetric
  ): Double = {
    val predictionColumnName = dataFrame.getColumnName(predChoice.getPredictionColumnParam)
    val predictionAndLabels = dataFrame.sparkDataFrame.select(predictionColumnName, labelColumnName).rdd.map {
      case Row(prediction: Double, label: Double) =>
        (prediction, label)
    }
    val metrics = new MulticlassMetrics(predictionAndLabels)
    val metric = predChoice match {
      case precisionChoice: Precision => metrics.precision(1.0)
      case recallChoice: Recall       => metrics.recall(1.0)
      case f1Choice: F1Score          => metrics.fMeasure(1.0)
    }
    metric
  }

  override def _infer(k: DKnowledge[DataFrame]): MetricValue = {
    // TODO: When dataset metadata will be implemented in Spark,
    // check rawPredictionCol vector length = 2.
    k.single.schema.foreach { schema =>
      DataFrameColumnsGetter.assertExpectedColumnType(schema, $(labelColumn), ColumnType.numeric)
      $(metricName) match {
        case rawChoice: RawPredictionMetric =>
          DataFrameColumnsGetter.assertExpectedColumnType(
            schema,
            rawChoice.getRawPredictionColumnParam,
            ColumnType.vector
          )
        case predChoice: PredictionMetric =>
          DataFrameColumnsGetter.assertExpectedColumnType(
            schema,
            predChoice.getPredictionColumnParam,
            ColumnType.numeric
          )
      }
    }

    MetricValue.forInference(getMetricName)
  }

  override def isLargerBetter: Boolean = true

}

object BinaryClassificationEvaluator {

  val areaUnderROC = "Area under ROC"

  val areaUnderPR = "Area under PR"

  val precision = "Precision"

  val recall = "Recall"

  val f1Score = "F1 Score"

  sealed abstract class Metric(override val name: String) extends Choice {

    override val choiceOrder: List[Class[_ <: Choice]] = List(
      classOf[AreaUnderROC],
      classOf[AreaUnderPR],
      classOf[Precision],
      classOf[Recall],
      classOf[F1Score]
    )

  }

  trait RawPredictionMetric extends Params {

    val rawPredictionColumnParam = SingleColumnSelectorParam(
      name = "raw prediction column",
      description = Some("The raw prediction (confidence) column."),
      portIndex = 0
    )

    setDefault(rawPredictionColumnParam, NameSingleColumnSelection("rawPrediction"))

    def getRawPredictionColumnParam: SingleColumnSelection = $(rawPredictionColumnParam)

    def setRawPredictionColumnParam(value: SingleColumnSelection): this.type =
      set(rawPredictionColumnParam, value)

  }

  trait PredictionMetric extends Params {

    val predictionColumnParam = SingleColumnSelectorParam(
      name = "prediction column",
      description = Some("The prediction column created during model scoring."),
      portIndex = 0
    )

    setDefault(predictionColumnParam, NameSingleColumnSelection("prediction"))

    def getPredictionColumnParam: SingleColumnSelection = $(predictionColumnParam)

    def setPredictionColumnParam(value: SingleColumnSelection): this.type =
      set(predictionColumnParam, value)

  }

  case class AreaUnderROC() extends Metric(areaUnderROC) with RawPredictionMetric {

    override val name = areaUnderROC

    override val params: Array[io.deepsense.deeplang.params.Param[_]] = Array(rawPredictionColumnParam)

  }

  case class AreaUnderPR() extends Metric(areaUnderPR) with RawPredictionMetric {

    override val name = areaUnderPR

    override val params: Array[io.deepsense.deeplang.params.Param[_]] = Array(rawPredictionColumnParam)

  }

  case class Precision() extends Metric(precision) with PredictionMetric {

    override val name = precision

    override val params: Array[io.deepsense.deeplang.params.Param[_]] = Array(predictionColumnParam)

  }

  case class Recall() extends Metric(recall) with PredictionMetric {

    override val name = recall

    override val params: Array[io.deepsense.deeplang.params.Param[_]] = Array(predictionColumnParam)

  }

  case class F1Score() extends Metric(f1Score) with PredictionMetric {

    override val name = f1Score

    override val params: Array[io.deepsense.deeplang.params.Param[_]] = Array(predictionColumnParam)

  }

}
