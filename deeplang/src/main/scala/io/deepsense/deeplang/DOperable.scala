package io.deepsense.deeplang

import io.deepsense.deeplang.doperables.descriptions.HasInferenceResult
import io.deepsense.deeplang.doperables.report.Report

/**
 * Objects of classes with this trait can be used in two ways.
 * 1. It can be object on which you can perform DOperations.
 * 2. It can be used to infer knowledge about objects that will be used later in workflow,
 * before it's execution.
 * DOperable that can be used for execution can ALWAYS be used for inference, but not vice-versa.
 */
trait DOperable extends HasInferenceResult {
  def report: Report = Report()
}
