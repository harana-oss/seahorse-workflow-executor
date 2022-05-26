package ai.deepsense.deeplang.documentation

import ai.deepsense.deeplang.DOperation

/** Represents operation's documentation attributes. */
trait OperationDocumentation extends Documentable { self: DOperation =>

  final override def hasDocumentation: Boolean = true

}
