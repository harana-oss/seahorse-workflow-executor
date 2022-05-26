package io.deepsense.deeplang.documentation

import io.deepsense.deeplang.DOperation

/** Represents operation's documentation attributes. */
trait OperationDocumentation extends Documentable { self: DOperation =>

  final override def hasDocumentation: Boolean = true

}
