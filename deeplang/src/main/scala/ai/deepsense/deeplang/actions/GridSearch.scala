package ai.deepsense.deeplang.actions

import scala.reflect.runtime.universe.TypeTag

import org.apache.spark.ml
import org.apache.spark.ml.param.Param
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.ml.tuning.CrossValidator
import org.apache.spark.ml.tuning.CrossValidatorModel
import org.apache.spark.ml.tuning.ParamGridBuilder
import spray.json.JsNull
import spray.json.JsValue

import ai.deepsense.commons.types.ColumnType
import ai.deepsense.commons.utils.DoubleUtils
import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang.documentation.OperationDocumentation
import ai.deepsense.deeplang.actionobjects._
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actionobjects.report.Report
import ai.deepsense.deeplang.actionobjects.wrappers.EstimatorWrapper
import ai.deepsense.deeplang.actionobjects.wrappers.EvaluatorWrapper
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.parameters.gridsearch.GridSearchParameter
import ai.deepsense.deeplang.parameters.validators.RangeValidator
import ai.deepsense.deeplang.parameters.wrappers.deeplang.ParamWrapper
import ai.deepsense.deeplang.parameters.DynamicParameter
import ai.deepsense.deeplang.parameters.NumericParameter
import ai.deepsense.deeplang.parameters.ParamPair
import ai.deepsense.deeplang.Knowledge
import ai.deepsense.deeplang.Action3To1
import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphReader
import ai.deepsense.reportlib.model.ReportContent
import ai.deepsense.reportlib.model.ReportType
import ai.deepsense.reportlib.model.Table

case class GridSearch()
    extends Action3To1[Estimator[Transformer], DataFrame, Evaluator, Report]
    with OperationDocumentation {

  override val name: String = "Grid Search"

  override val id: Id = "9163f706-eaaf-46f6-a5b0-4114d92032b7"

  override val description: String = "Uses Cross-validation to find the best set of parameters " +
    "for input estimator. User can specify a list of parameter values to test and compare."

  override val since: Version = Version(1, 0, 0)

  val estimatorParams = new GridSearchParameter(
    name = "Parameters of input Estimator",
    description = Some("These parameters are rendered dynamically, depending on type of Estimator."),
    inputPort = 0
  )

  setDefault(estimatorParams, JsNull)

  val evaluatorParams = new DynamicParameter(
    name = "Parameters of input Evaluator",
    description = Some("These parameters are rendered dynamically, depending on type of Evaluator."),
    inputPort = 2
  )

  setDefault(evaluatorParams, JsNull)

  val numberOfFolds = new NumericParameter(
    name = "number of folds",
    description = None,
    validator = RangeValidator(begin = 2.0, end = Int.MaxValue, beginIncluded = true, step = Some(1.0))
  )

  setDefault(numberOfFolds, 2.0)

  def getNumberOfFolds: Int = $(numberOfFolds).toInt

  def setNumberOfFolds(numOfFolds: Int): this.type = set(numberOfFolds, numOfFolds.toDouble)

  def getEstimatorParams: JsValue = $(estimatorParams)

  def setEstimatorParams(jsValue: JsValue): this.type = set(estimatorParams, jsValue)

  def getEvaluatorParams: JsValue = $(evaluatorParams)

  def setEvaluatorParams(jsValue: JsValue): this.type = set(evaluatorParams, jsValue)

  override val specificParams: Array[ai.deepsense.deeplang.parameters.Parameter[_]] =
    Array(estimatorParams, evaluatorParams, numberOfFolds)

  override lazy val tTagTI_0: TypeTag[Estimator[Transformer]] = typeTag

  override lazy val tTagTI_1: TypeTag[DataFrame] = typeTag

  override lazy val tTagTI_2: TypeTag[Evaluator] = typeTag

  override lazy val tTagTO_0: TypeTag[Report] = typeTag

  override protected def execute(estimator: Estimator[Transformer], dataFrame: DataFrame, evaluator: Evaluator)(
      context: ExecutionContext
  ): Report = {

    val graphReader         = context.inferContext.graphReader
    val estimatorParams     = estimator.paramPairsFromJson(getEstimatorParams, graphReader)
    val estimatorWithParams = createEstimatorWithParams(estimator, estimatorParams)
    val evaluatorWithParams = createEvaluatorWithParams(evaluator, graphReader)
    validateDynamicParams(estimatorWithParams, evaluatorWithParams)

    val estimatorWrapper: EstimatorWrapper = new EstimatorWrapper(context, estimatorWithParams)
    val gridSearchParams: Array[ParamMap]  =
      createGridSearchParams(estimatorWrapper.uid, estimatorParams)
    val cv                                 = new CrossValidator()
      .setEstimator(estimatorWrapper)
      .setEvaluator(new EvaluatorWrapper(context, evaluatorWithParams))
      .setEstimatorParamMaps(gridSearchParams)
      .setNumFolds(getNumberOfFolds)
    val cvModel: CrossValidatorModel       = cv.fit(dataFrame.sparkDataFrame)
    createReport(gridSearchParams, cvModel.avgMetrics, evaluator.isLargerBetter)
  }

  override protected def inferKnowledge(
                                         estimatorKnowledge: Knowledge[Estimator[Transformer]],
                                         dataFrameKnowledge: Knowledge[DataFrame],
                                         evaluatorKnowledge: Knowledge[Evaluator]
  )(context: InferContext): (Knowledge[Report], InferenceWarnings) = {

    val estimator           = estimatorKnowledge.single
    val evaluator           = evaluatorKnowledge.single
    val graphReader         = context.graphReader
    val estimatorParams     = estimator.paramPairsFromJson(getEstimatorParams, graphReader)
    val estimatorWithParams = createEstimatorWithParams(estimator, estimatorParams)
    val evaluatorWithParams = createEvaluatorWithParams(evaluator, graphReader)

    validateDynamicParams(estimatorWithParams, evaluatorWithParams)

    dataFrameKnowledge.single.schema.foreach { case schema =>
      val transformer       = estimatorWithParams._fit_infer(Some(schema))
      val transformedSchema = transformer._transformSchema(schema, context)
      evaluatorWithParams._infer(Knowledge(DataFrame.forInference(transformedSchema)))
    }

    (Knowledge(Report()), InferenceWarnings.empty)
  }

  private def createReport(
      gridSearchParams: Array[ParamMap],
      metrics: Array[Double],
      isLargerMetricBetter: Boolean
  ): Report = {
    val paramsWithOrder: Seq[Param[_]] = gridSearchParams.head.toSeq.map(_.param).sortBy(_.name)
    val sortedMetrics: Seq[Metric]     =
      sortParamsByMetricValue(gridSearchParams, metrics, isLargerMetricBetter)
    Report(
      ReportContent(
        "Grid Search",
        ReportType.GridSearch,
        tables = Seq(
          bestParamsMetricsTable(paramsWithOrder, sortedMetrics.head),
          cvParamsMetricsTable(paramsWithOrder, sortedMetrics)
        )
      )
    )
  }

  private def sortParamsByMetricValue(
      gridSearchParams: Array[ParamMap],
      metricValues: Array[Double],
      isLargerMetricBetter: Boolean
  ): Seq[Metric] = {
    val metrics: Seq[Metric] = gridSearchParams.zip(metricValues).map(new Metric(_))
    val sorted               = metrics.sortBy(_.metricValue)
    if (isLargerMetricBetter)
      sorted.reverse
    else
      sorted
  }

  private def bestParamsMetricsTable(paramsWithOrder: Seq[Param[_]], bestMetric: Metric): Table = {
    Table(
      name = "Best Params",
      description = "Best Parameters Values",
      columnNames = metricsTableColumnNames(paramsWithOrder),
      columnTypes = metricsTableColumnTypes(paramsWithOrder),
      rowNames = None,
      values = List(metricsTableRow(bestMetric.params, paramsWithOrder, bestMetric.metricValue))
    )
  }

  private def cvParamsMetricsTable(paramsWithOrder: Seq[Param[_]], params: Seq[Metric]): Table = {
    Table(
      name = "Params",
      description = "Parameters Values",
      columnNames = metricsTableColumnNames(paramsWithOrder),
      columnTypes = metricsTableColumnTypes(paramsWithOrder),
      rowNames = None,
      values = params.map { case metric =>
        metricsTableRow(metric.params, paramsWithOrder, metric.metricValue)
      }.toList
    )
  }

  private def metricsTableRow(
      cvParamMap: ParamMap,
      paramsWithOrder: Seq[Param[_]],
      metric: Double
  ): List[Option[String]] =
    paramMapToTableRow(cvParamMap, paramsWithOrder) :+ Some(DoubleUtils.double2String(metric))

  private def metricsTableColumnTypes(paramsWithOrder: Seq[Param[_]]): List[ColumnType.Value] =
    (paramsWithOrder.map(_ => ColumnType.numeric) :+ ColumnType.numeric).toList

  private def metricsTableColumnNames(paramsWithOrder: Seq[Param[_]]): Some[List[String]] =
    Some((paramsWithOrder.map(_.name) :+ "Metric").toList)

  private def paramMapToTableRow(paramMap: ParamMap, orderedParams: Seq[ml.param.Param[_]]): List[Option[String]] =
    orderedParams.map(paramMap.get(_).map(_.toString)).toList

  private def createEstimatorWithParams(
      estimator: Estimator[Transformer],
      estimatorParams: Seq[ParamPair[_]]
  ): Estimator[Transformer] =
    estimator.replicate().set(estimatorParams: _*)

  private def createEvaluatorWithParams(evaluator: Evaluator, graphReader: GraphReader): Evaluator =
    evaluator.replicate().setParamsFromJson($(evaluatorParams), graphReader, ignoreNulls = true)

  private def createGridSearchParams(estimatorUID: String, params: Seq[ParamPair[_]]): Array[ml.param.ParamMap] = {

    params
      .filter(paramPair => paramPair.param.isGriddable)
      .foldLeft(new ParamGridBuilder()) { case (builder, paramPair) =>
        val sparkParam = new ParamWrapper(estimatorUID, paramPair.param)
        builder.addGrid(sparkParam, paramPair.values)
      }
      .build()
  }

  private case class Metric(params: ParamMap, metricValue: Double) {

    def this(pair: (ParamMap, Double)) = this(pair._1, pair._2)

  }

}
