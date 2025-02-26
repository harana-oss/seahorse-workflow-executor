package ai.deepsense.deeplang.actions

import org.apache.spark.ml
import org.apache.spark.ml.evaluation
import org.apache.spark.ml.tuning.CrossValidator
import org.apache.spark.ml.tuning.ParamGridBuilder
import spray.json._

import ai.deepsense.commons.utils.DoubleUtils
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actionobjects.report.Report
import ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators.LinearRegression
import ai.deepsense.deeplang.actionobjects.spark.wrappers.evaluators.RegressionEvaluator
import ai.deepsense.deeplang.actions.exceptions.ColumnDoesNotExistException
import ai.deepsense.deeplang.Knowledge
import ai.deepsense.deeplang.DeeplangIntegTestSupport
import ai.deepsense.sparkutils.Linalg
import ai.deepsense.sparkutils.Linalg.Vectors

class GridSearchIntegSpec extends DeeplangIntegTestSupport with DefaultJsonProtocol {

  private val regularizationParams = Array(0.01, 0.5, 5.0)

  "GridSearch" should {
    "find best params" in {
      val gridSearch = new GridSearch()
      val estimator  = new LinearRegression()
      val dataFrame  = buildDataFrame()
      val evaluator  = new RegressionEvaluator()
      gridSearch.setEstimatorParams(estimatorParams)
      gridSearch.setNumberOfFolds(2)

      val results = gridSearch.executeUntyped(Vector(estimator, dataFrame, evaluator))(executionContext)
      val report  = results.head.asInstanceOf[Report]

      val tables = report.content.tables

      val expectedMetrics: Array[Double] = pureSparkImplementation()
      val expectedBestMetric             = expectedMetrics.toList.min

      val bestMetricsTable = tables.head
      bestMetricsTable.values.size shouldBe 1
      bestMetricsTable.values shouldBe
        List(List(Some("10.0"), Some("5.0"), doubleToCell(expectedBestMetric)))

      val expectedMetricsTable = List(
        List(Some("10.0"), Some("5.0"), doubleToCell(expectedMetrics(2))),
        List(Some("10.0"), Some("0.5"), doubleToCell(expectedMetrics(1))),
        List(Some("10.0"), Some("0.01"), doubleToCell(expectedMetrics(0)))
      )
      val metricsTable         = tables(1)
      metricsTable.values.size shouldBe 3
      metricsTable.values shouldBe expectedMetricsTable
    }

    "throw an exception in inference" when {
      "estimator params are invalid" in {
        val gridSearch = new GridSearch()
        val estimator  = new LinearRegression()
        val dataFrame  = buildDataFrame()
        val evaluator  = new RegressionEvaluator()
        val params     = JsObject(
          estimatorParams.fields.updated(
            "features column",
            JsObject(
              "type"  -> JsString("column"),
              "value" -> JsString("invalid")
            )
          )
        )
        gridSearch.setEstimatorParams(params)
        gridSearch.setNumberOfFolds(2)

        a[ColumnDoesNotExistException] should be thrownBy {
          gridSearch.inferKnowledgeUntyped(Vector(Knowledge(estimator), Knowledge(dataFrame), Knowledge(evaluator)))(
            executionContext.inferContext
          )
        }
      }
      "evaluator params are invalid" in {
        val gridSearch = new GridSearch()
        val estimator  = new LinearRegression()
        val dataFrame  = buildDataFrame()
        val evaluator  = new RegressionEvaluator()
        val params     = JsObject(
          evaluator.paramValuesToJson.asJsObject.fields.updated(
            "label column",
            JsObject(
              "type"  -> JsString("column"),
              "value" -> JsString("invalid")
            )
          )
        )
        gridSearch.setEvaluatorParams(params)
        gridSearch.setNumberOfFolds(2)

        a[ColumnDoesNotExistException] should be thrownBy {
          gridSearch.inferKnowledgeUntyped(Vector(Knowledge(estimator), Knowledge(dataFrame), Knowledge(evaluator)))(
            executionContext.inferContext
          )
        }
      }
    }
  }

  private val estimatorParams = JsObject(
    "regularization param" -> seqParam(Seq(0.01, 0.5, 5.0)),
    "features column"      -> JsObject(
      "type"  -> JsString("column"),
      "value" -> JsString("features")
    ),
    "max iterations"       -> JsNumber(10.0)
  )

  private def seqParam(values: Seq[Double]): JsObject = {
    JsObject(
      "values" -> JsArray(
        JsObject(
          "type"  -> JsString("seq"),
          "value" -> JsObject("sequence" -> values.toJson)
        )
      )
    )
  }

  private def buildDataFrame(): DataFrame = {
    val districtFactors = Seq(0.6, 0.8, 1.0)
    val priceForMeterSq = 7000
    val apartments      = Range(40, 300, 5).map { case flatSize =>
      val districtFactor = districtFactors(flatSize % districtFactors.length)
      Apartment(Vectors.dense(flatSize, districtFactor), (flatSize * districtFactor * priceForMeterSq).toLong)
    }
    DataFrame.fromSparkDataFrame(sparkSQLSession.createDataFrame(apartments))
  }

  private def pureSparkImplementation(): Array[Double] = {
    val lr        = new ml.regression.LinearRegression()
    val paramGrid = new ParamGridBuilder()
      .addGrid(lr.regParam, regularizationParams)
      .build()
    val cv        = new CrossValidator()
      .setEstimator(lr)
      .setEvaluator(new evaluation.RegressionEvaluator())
      .setEstimatorParamMaps(paramGrid)
      .setNumFolds(2)
    val cvModel   = cv.fit(buildDataFrame().sparkDataFrame)
    cvModel.avgMetrics
  }

  private def doubleToCell(d: Double) = Some(DoubleUtils.double2String(d))

}

private case class Apartment(features: Linalg.Vector, label: Double)
