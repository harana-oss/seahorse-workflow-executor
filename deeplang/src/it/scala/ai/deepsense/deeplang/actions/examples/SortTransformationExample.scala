package ai.deepsense.deeplang.actions.examples

import ai.deepsense.deeplang.actionobjects.SortColumnParam
import ai.deepsense.deeplang.actions.SortTransformation

class SortTransformationExample extends AbstractOperationExample[SortTransformation] {

  override val fileNames = Seq("example_city_beds_price")

  override def dOperation: SortTransformation = {
    val op = new SortTransformation
    op.transformer.setColumns(
      Seq(
        SortColumnParam("city", descending = false),
        SortColumnParam("price", descending = true)
      )
    )
    op.set(op.transformer.extractParamMap())
  }

}
