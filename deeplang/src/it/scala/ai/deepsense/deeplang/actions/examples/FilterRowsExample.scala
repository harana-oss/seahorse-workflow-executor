package ai.deepsense.deeplang.actions.examples

import ai.deepsense.deeplang.actions.FilterRows

class FilterRowsExample extends AbstractOperationExample[FilterRows] {

  override def dOperation: FilterRows = {
    val op = new FilterRows()
    op.transformer
      .setCondition("0.4 < temp AND windspeed < 0.3")
    op.set(op.transformer.extractParamMap())
  }

  override def fileNames: Seq[String] = Seq("example_datetime_windspeed_hum_temp")

}
