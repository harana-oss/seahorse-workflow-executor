package ai.deepsense.deeplang.actions.examples

import ai.deepsense.deeplang.actionobjects.Projector.ColumnProjection
import ai.deepsense.deeplang.actionobjects.Projector.RenameColumnChoice.Yes
import ai.deepsense.deeplang.actions.Projection
import ai.deepsense.deeplang.parameters.selections.NameSingleColumnSelection

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
