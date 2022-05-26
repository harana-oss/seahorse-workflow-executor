package ai.deepsense.deeplang.doperations.examples

import ai.deepsense.deeplang.doperations.Join
import ai.deepsense.deeplang.doperations.Join.ColumnPair
import ai.deepsense.deeplang.params.selections.NameSingleColumnSelection
import ai.deepsense.deeplang.params.selections.NameColumnSelection

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
