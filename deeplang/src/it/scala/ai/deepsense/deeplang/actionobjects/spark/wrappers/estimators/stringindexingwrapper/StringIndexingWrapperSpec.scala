package ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators.stringindexingwrapper

import ai.deepsense.deeplang.UnitSpec
import ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators.GBTClassifier
import ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators.VanillaGBTClassifier

class StringIndexingWrapperSpec extends UnitSpec {

  "String indexing wrapper" should {
    "inherit default values from wrapped estimator" in {
      val someWrappedEstimator = new VanillaGBTClassifier()
      val someWrapper          = new GBTClassifier

      for (someParam <- someWrapper.params)
        someWrapper.getDefault(someParam) shouldEqual someWrappedEstimator.getDefault(someParam)
    }
  }

}
