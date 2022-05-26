package io.deepsense.deeplang.doperables.descriptions

trait HasInferenceResult {

  def inferenceResult: Option[InferenceResult] = None

}
