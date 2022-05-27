package ai.deepsense.deeplang.actions.examples

import ai.deepsense.deeplang.actions.spark.wrappers.estimators.StringIndexer

class StringIndexerExample extends AbstractOperationExample[StringIndexer] {

  override def dOperation: StringIndexer =
    new StringIndexer().setSingleColumn("city", "city_indexed")

  override def fileNames: Seq[String] = Seq("example_city_price")

}
