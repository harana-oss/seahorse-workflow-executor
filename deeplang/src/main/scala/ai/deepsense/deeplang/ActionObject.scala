package ai.deepsense.deeplang

import ai.deepsense.deeplang.actionobjects.descriptions.HasInferenceResult
import ai.deepsense.deeplang.actionobjects.report.Report

/** Objects of classes with this trait can be used in two ways.
  *   1. It can be object on which you can perform Actions. 2. It can be used to infer knowledge about objects that
  *      will be used later in workflow, before it's execution. ActionObject that can be used for execution can ALWAYS be
  *      used for inference, but not vice-versa.
  */
trait ActionObject extends HasInferenceResult {

  def report(extended: Boolean = true): Report = Report()

}
