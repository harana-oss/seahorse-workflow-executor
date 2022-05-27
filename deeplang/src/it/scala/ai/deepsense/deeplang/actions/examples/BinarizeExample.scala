package ai.deepsense.deeplang.actions.examples

import ai.deepsense.deeplang.actions.spark.wrappers.transformers.Binarize

class BinarizeExample extends AbstractOperationExample[Binarize] {

  override def dOperation: Binarize = {
    val op = new Binarize()
    op.transformer
      .setSingleColumn("hum", "hum_bin")
      .setThreshold(0.5)
    op.set(op.transformer.extractParamMap())
  }

  override def fileNames: Seq[String] = Seq("example_datetime_windspeed_hum_temp")

}
