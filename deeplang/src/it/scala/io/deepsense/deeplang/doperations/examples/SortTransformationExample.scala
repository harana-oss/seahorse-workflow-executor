package io.deepsense.deeplang.doperations.examples

import io.deepsense.deeplang.doperables.SortColumnParam
import io.deepsense.deeplang.doperations.SortTransformation

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
