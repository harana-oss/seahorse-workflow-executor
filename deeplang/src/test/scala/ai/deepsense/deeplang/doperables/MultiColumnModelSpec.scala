package ai.deepsense.deeplang.doperables

import ai.deepsense.deeplang.UnitSpec
import ai.deepsense.deeplang.doperables.spark.wrappers.models.MultiColumnStringIndexerModel
import ai.deepsense.deeplang.params.ParamMap

class MultiColumnModelSpec extends UnitSpec {

  "MultiColumnModel" should {
    "not fail during replicate" in {
      val model = new MultiColumnStringIndexerModel()
      model.setModels(Seq())
      model.replicate(ParamMap.empty)
    }
  }

}
