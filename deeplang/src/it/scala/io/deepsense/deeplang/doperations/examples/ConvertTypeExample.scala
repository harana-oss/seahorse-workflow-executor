package io.deepsense.deeplang.doperations.examples

import io.deepsense.deeplang.doperables.TargetTypeChoices.IntegerTargetTypeChoice
import io.deepsense.deeplang.doperations.ConvertType

class ConvertTypeExample extends AbstractOperationExample[ConvertType] {
  override def dOperation: ConvertType = {
    val op = new ConvertType()
    op.transformer.setSingleColumn("beds", "beds_int")
    op.transformer.setTargetType(IntegerTargetTypeChoice())
    op.set(op.transformer.extractParamMap())
  }

  override def fileNames: Seq[String] = Seq("example_city_beds_price")
}
