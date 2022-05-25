package io.deepsense.deeplang.doperations

import spray.json.{JsNumber, JsObject}

import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperables.report.Report
import io.deepsense.deeplang.doperations.MockDOperablesFactory.{MockEstimator, MockEvaluator}
import io.deepsense.deeplang.exceptions.DeepLangMultiException
import io.deepsense.deeplang.inference.{InferContext, InferenceWarnings}
import io.deepsense.deeplang.{DKnowledge, DeeplangTestSupport, UnitSpec}

class GridSearchSpec extends UnitSpec with DeeplangTestSupport {
  "GridSearch" should {
    "infer knowledge when dynamic parameters are valid" in {
      val inputDF = DataFrame.forInference(createSchema())
      val estimator = new MockEstimator
      val evaluator = new MockEvaluator

      val gridSearch = GridSearch()
      gridSearch.inferKnowledgeUntyped(
          Vector(DKnowledge(estimator), DKnowledge(inputDF), DKnowledge(evaluator)))(mock[InferContext]) shouldBe
        (Vector(DKnowledge(Report())), InferenceWarnings.empty)
    }
    "throw Exception" when {
      "Estimator's dynamic parameters are invalid" in {
        checkMultiException(Some(-2), None)
      }
      "Evaluator's dynamic parameters are invalid" in {
        checkMultiException(None, Some(-2))
      }
      "Both Estimator's and Evaluator's dynamic parameters are invalid" in {
        checkMultiException(Some(-2), Some(-2))
      }
    }
  }

  private def checkMultiException(
      estimatorParamValue: Option[Double],
      evaluatorParamValue: Option[Double]): Unit = {

    val inputDF = DataFrame.forInference(createSchema())
    val estimator = new MockEstimator
    val evaluator = new MockEvaluator

    val gridSearch = GridSearch()
      .setEstimatorParams(prepareParamDictionary(estimator.paramA.name, estimatorParamValue))
      .setEvaluatorParams(prepareParamDictionary(evaluator.paramA.name, evaluatorParamValue))

    val multiException = the [DeepLangMultiException] thrownBy {
      gridSearch.inferKnowledgeUntyped(
        Vector(
          DKnowledge(estimator),
          DKnowledge(inputDF),
          DKnowledge(evaluator)))(mock[InferContext])
    }

    val invalidParamCount =
      estimatorParamValue.map(_ => 1).getOrElse(0) +
      evaluatorParamValue.map(_ => 1).getOrElse(0)

    multiException.exceptions should have size invalidParamCount
  }

  private def prepareParamDictionary(paramName: String, maybeValue: Option[Double]): JsObject = {
    val jsonEntries = maybeValue.map(
        value => Seq(paramName -> JsNumber(value)))
      .getOrElse(Seq())
    JsObject(jsonEntries: _*)
  }
}
