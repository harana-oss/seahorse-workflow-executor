package io.deepsense.deeplang.doperables

import io.deepsense.deeplang.UnitSpec
import io.deepsense.deeplang.doperables.spark.wrappers.models.MultiColumnStringIndexerModel
import io.deepsense.deeplang.params.ParamMap

class MultiColumnModelSpec extends UnitSpec {

  "MultiColumnModel" should {
    "not fail during replicate" in {
      val model = new MultiColumnStringIndexerModel()
      model.setModels(Seq())
      model.replicate(ParamMap.empty)
    }
  }
}
