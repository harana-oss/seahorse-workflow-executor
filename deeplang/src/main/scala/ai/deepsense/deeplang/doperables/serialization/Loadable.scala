package ai.deepsense.deeplang.doperables.serialization

import ai.deepsense.deeplang.ExecutionContext

trait Loadable {

  def load(ctx: ExecutionContext, path: String): this.type

}
