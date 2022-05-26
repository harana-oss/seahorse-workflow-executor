package io.deepsense.deeplang.doperables.serialization

import io.deepsense.deeplang.ExecutionContext

trait Loadable {

  def load(ctx: ExecutionContext, path: String): this.type

}
