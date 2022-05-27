package ai.deepsense.deeplang.actions.exceptions

case class SqlColumnTransformationExecutionException(
    inputColumnName: String,
    formula: String,
    outputColumnName: String,
    rootCause: Option[Throwable]
) extends DOperationExecutionException(
      s"Problem while executing SqlColumnTransformation with the following formula: '$formula'",
      rootCause
    )
