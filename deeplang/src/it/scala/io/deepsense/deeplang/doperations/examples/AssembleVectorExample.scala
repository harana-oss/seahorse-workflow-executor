package io.deepsense.deeplang.doperations.examples

import io.deepsense.deeplang.doperations.spark.wrappers.transformers.AssembleVector

class AssembleVectorExample extends AbstractOperationExample[AssembleVector] {
  override def dOperation: AssembleVector = {
    val op = new AssembleVector()
    op.transformer
      .setInputColumns(Set("windspeed", "hum", "temp"))
      .setOutputColumn("assembled")
    op.set(op.transformer.extractParamMap())
  }

  override def fileNames: Seq[String] = Seq("example_datetime_windspeed_hum_temp")
}
