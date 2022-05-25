package io.deepsense.deeplang.inference.exceptions

case class SparkTransformSchemaException(exception: Exception)
  extends TransformSchemaException(exception.getMessage, cause = Some(exception))
