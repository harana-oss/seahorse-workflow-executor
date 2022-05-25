package io.deepsense.workflowexecutor

import io.deepsense.deeplang.doperations.exceptions.EmptyDataframeException
import io.deepsense.deeplang.exceptions.DeepLangException

/**
  * Unfortunetely Spark exceptions are stringly typed. Spark does not have their exception classes.
  * This extractor hides Sparks strings and converts spark exceptions to deeplangs.
  */
object SparkExceptionAsDeeplangException {
  def unapply(exception: Exception): Option[DeepLangException] = exception match {
    case emptyCollectionEx if emptyCollectionEx.getMessage == "empty collection" =>
      Some(EmptyDataframeException)
    case unknown => None
  }
}
