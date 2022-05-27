package ai.deepsense.deeplang.documentation

import ai.deepsense.deeplang.Action

/** Represents operation's documentation attributes. */
trait OperationDocumentation extends Documentable { self: Action =>

  final override def hasDocumentation: Boolean = true

}
