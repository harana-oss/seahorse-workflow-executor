package ai.deepsense.deeplang.actionobjects.spark.wrappers.transformers

import ai.deepsense.deeplang.actionobjects.MultiColumnTransformer
import ai.deepsense.deeplang.actionobjects.MultiColumnTransformerTestSupport

trait MultiColumnTransformerWrapperTestSupport extends MultiColumnTransformerTestSupport {
  self: AbstractTransformerWrapperSmokeTest[MultiColumnTransformer] =>

  override def transformerName: String = className

  override def transformer: MultiColumnTransformer = transformerWithParams

}
