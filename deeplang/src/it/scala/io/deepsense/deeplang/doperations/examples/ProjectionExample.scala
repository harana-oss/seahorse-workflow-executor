package io.deepsense.deeplang.doperations.examples

import io.deepsense.deeplang.doperables.Projector.ColumnProjection
import io.deepsense.deeplang.doperables.Projector.RenameColumnChoice.Yes
import io.deepsense.deeplang.doperations.Projection
import io.deepsense.deeplang.params.selections.NameSingleColumnSelection

class ProjectionExample extends AbstractOperationExample[Projection] {

  override def dOperation: Projection = {
    val op = new Projection()
    op.transformer.setProjectionColumns(
      Seq(
        ColumnProjection().setOriginalColumn(NameSingleColumnSelection("price")),
        ColumnProjection().setOriginalColumn(NameSingleColumnSelection("city")),
        ColumnProjection()
          .setOriginalColumn(NameSingleColumnSelection("city"))
          .setRenameColumn(new Yes().setColumnName("location"))
      )
    )
    op.set(op.transformer.extractParamMap())
  }

  override def fileNames: Seq[String] = Seq("example_city_beds_price")

}
