package ai.deepsense.deeplang.actions

import spray.json.JsNumber
import spray.json.JsObject

import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actionobjects.report.Report
import ai.deepsense.deeplang.actions.MockActionObjectsFactory.MockEstimator
import ai.deepsense.deeplang.actions.MockActionObjectsFactory.MockEvaluator
import ai.deepsense.deeplang.exceptions.DeepLangMultiException
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.Knowledge
import ai.deepsense.deeplang.DeeplangTestSupport
import ai.deepsense.deeplang.UnitSpec

class GridSearchSpec extends UnitSpec with DeeplangTestSupport {

  "GridSearch" should {
    "infer knowledge when dynamic parameters are valid" in {
      val inputDF   = DataFrame.forInference(createSchema())
      val estimator = new MockEstimator
      val evaluator = new MockEvaluator

      val gridSearch = GridSearch()
      gridSearch.inferKnowledgeUntyped(Vector(Knowledge(estimator), Knowledge(inputDF), Knowledge(evaluator)))(
        mock[InferContext]
      ) shouldBe
        (Vector(Knowledge(Report())), InferenceWarnings.empty)
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

  private def checkMultiException(estimatorParamValue: Option[Double], evaluatorParamValue: Option[Double]): Unit = {

    val inputDF   = DataFrame.forInference(createSchema())
    val estimator = new MockEstimator
    val evaluator = new MockEvaluator

    val gridSearch = GridSearch()
      .setEstimatorParams(prepareParamDictionary(estimator.paramA.name, estimatorParamValue))
      .setEvaluatorParams(prepareParamDictionary(evaluator.paramA.name, evaluatorParamValue))

    val multiException = the[DeepLangMultiException] thrownBy {
      gridSearch.inferKnowledgeUntyped(Vector(Knowledge(estimator), Knowledge(inputDF), Knowledge(evaluator)))(
        mock[InferContext]
      )
    }

    val invalidParamCount =
      estimatorParamValue.map(_ => 1).getOrElse(0) +
        evaluatorParamValue.map(_ => 1).getOrElse(0)

    multiException.exceptions should have size invalidParamCount
  }

  private def prepareParamDictionary(paramName: String, maybeValue: Option[Double]): JsObject = {
    val jsonEntries = maybeValue
      .map(value => Seq(paramName -> JsNumber(value)))
      .getOrElse(Seq())
    JsObject(jsonEntries: _*)
  }

}
