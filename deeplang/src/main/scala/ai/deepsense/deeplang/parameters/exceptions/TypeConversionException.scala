package ai.deepsense.deeplang.parameters.exceptions

case class TypeConversionException(source: Any, targetTypeName: String)
    extends ValidationException(s"Cannot convert ${source.getClass} to $targetTypeName.")
