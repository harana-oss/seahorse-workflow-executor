package ai.deepsense.deeplang.actions.examples

import ai.deepsense.deeplang.actions.Split
import ai.deepsense.deeplang.actions.SplitModeChoice

class SplitExample extends AbstractOperationExample[Split] {

  override def dOperation: Split =
    new Split()
      .setSplitMode(
        SplitModeChoice
          .Random()
          .setSeed(0)
          .setSplitRatio(0.2)
      )

  override def fileNames: Seq[String] = Seq("example_city_beds_price")

}
