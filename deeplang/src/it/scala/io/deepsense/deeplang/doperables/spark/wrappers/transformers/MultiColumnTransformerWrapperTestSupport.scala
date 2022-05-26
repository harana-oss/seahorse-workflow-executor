package io.deepsense.deeplang.doperables.spark.wrappers.transformers

import io.deepsense.deeplang.doperables.MultiColumnTransformer
import io.deepsense.deeplang.doperables.MultiColumnTransformerTestSupport

trait MultiColumnTransformerWrapperTestSupport extends MultiColumnTransformerTestSupport {
  self: AbstractTransformerWrapperSmokeTest[MultiColumnTransformer] =>

  override def transformerName: String = className

  override def transformer: MultiColumnTransformer = transformerWithParams

}
