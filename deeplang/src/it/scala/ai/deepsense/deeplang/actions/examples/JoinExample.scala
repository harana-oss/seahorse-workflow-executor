package ai.deepsense.deeplang.actions.examples

import ai.deepsense.deeplang.actions.Join
import ai.deepsense.deeplang.actions.Join.ColumnPair
import ai.deepsense.deeplang.parameters.selections.NameSingleColumnSelection
import ai.deepsense.deeplang.parameters.selections.NameColumnSelection

class JoinExample extends AbstractOperationExample[Join] {

  override def dOperation: Join = {
    new Join()
      .setLeftPrefix("left_")
      .setRightPrefix("right_")
      .setJoinColumns(
        Seq(
          ColumnPair()
            .setLeftColumn(NameSingleColumnSelection("city"))
            .setRightColumn(NameSingleColumnSelection("city"))
        )
      )
  }

  override def fileNames: Seq[String] = Seq("example_city_price", "example_city_beds")

}
