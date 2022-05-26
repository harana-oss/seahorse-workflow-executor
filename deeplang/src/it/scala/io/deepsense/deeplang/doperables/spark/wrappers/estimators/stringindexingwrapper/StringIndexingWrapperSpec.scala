package io.deepsense.deeplang.doperables.spark.wrappers.estimators.stringindexingwrapper

import io.deepsense.deeplang.UnitSpec
import io.deepsense.deeplang.doperables.spark.wrappers.estimators.GBTClassifier
import io.deepsense.deeplang.doperables.spark.wrappers.estimators.VanillaGBTClassifier

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
