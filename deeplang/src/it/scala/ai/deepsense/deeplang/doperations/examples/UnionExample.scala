package ai.deepsense.deeplang.doperations.examples

import ai.deepsense.deeplang.doperations.Union

class UnionExample extends AbstractOperationExample[Union] {

  override def dOperation: Union = new Union()

  override def fileNames: Seq[String] = Seq("example_union1", "example_union2")

}
