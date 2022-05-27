package ai.deepsense.deeplang.actionobjects.serialization

import ai.deepsense.deeplang.ExecutionContext

trait Loadable {

  def load(ctx: ExecutionContext, path: String): this.type

}
