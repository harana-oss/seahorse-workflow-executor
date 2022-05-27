package ai.deepsense.deeplang.actionobjects

import ai.deepsense.deeplang.UnitSpec
import ai.deepsense.deeplang.actionobjects.spark.wrappers.models.MultiColumnStringIndexerModel
import ai.deepsense.deeplang.parameters.ParameterMap

class MultiColumnModelSpec extends UnitSpec {

  "MultiColumnModel" should {
    "not fail during replicate" in {
      val model = new MultiColumnStringIndexerModel()
      model.setModels(Seq())
      model.replicate(ParameterMap.empty)
    }
  }

}
