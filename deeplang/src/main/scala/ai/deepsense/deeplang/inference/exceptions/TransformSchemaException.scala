package ai.deepsense.deeplang.inference.exceptions

import ai.deepsense.deeplang.exceptions.FlowException

class TransformSchemaException(message: String, cause: Option[Exception] = None)
    extends FlowException(message, cause.orNull)
