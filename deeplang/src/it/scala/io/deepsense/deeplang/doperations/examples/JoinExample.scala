package io.deepsense.deeplang.doperations.examples

import io.deepsense.deeplang.doperations.Join
import io.deepsense.deeplang.doperations.Join.ColumnPair
import io.deepsense.deeplang.params.selections.{NameSingleColumnSelection, NameColumnSelection}

class JoinExample extends AbstractOperationExample[Join] {
  override def dOperation: Join = {
    new Join()
      .setLeftPrefix("left_")
      .setRightPrefix("right_")
      .setJoinColumns(Seq(ColumnPair()
        .setLeftColumn(NameSingleColumnSelection("city"))
        .setRightColumn(NameSingleColumnSelection("city"))))
  }

  override def fileNames: Seq[String] = Seq("example_city_price", "example_city_beds")
}
