package ai.deepsense.deeplang.actions.examples

import ai.deepsense.deeplang.actions.Union

class UnionExample extends AbstractOperationExample[Union] {

  override def dOperation: Union = new Union()

  override def fileNames: Seq[String] = Seq("example_union1", "example_union2")

}
