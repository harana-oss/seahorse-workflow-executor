package io.deepsense.deeplang.doperables.spark.wrappers.transformers

import io.deepsense.deeplang.doperables.{MultiColumnTransformer, MultiColumnTransformerTestSupport}

trait MultiColumnTransformerWrapperTestSupport extends MultiColumnTransformerTestSupport {
  self: AbstractTransformerWrapperSmokeTest[MultiColumnTransformer] =>

  override def transformerName: String = className

  override def transformer: MultiColumnTransformer = transformerWithParams
}
