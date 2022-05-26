package ai.deepsense.deeplang.doperations.examples

import ai.deepsense.deeplang.doperations.FilterColumns

class FilterColumnsExample extends AbstractOperationExample[FilterColumns] {

  override def dOperation: FilterColumns = {
    val op = new FilterColumns()
    op.transformer.setSelectedColumns(Seq("city", "price"))
    op.set(op.transformer.extractParamMap())
  }

  override def fileNames: Seq[String] = Seq("example_city_beds_price")

}
