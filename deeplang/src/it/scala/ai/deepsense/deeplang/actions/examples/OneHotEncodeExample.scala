package ai.deepsense.deeplang.actions.examples

import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actions.spark.wrappers.estimators.StringIndexer
import ai.deepsense.deeplang.actions.spark.wrappers.transformers.OneHotEncode

class OneHotEncodeExample extends AbstractOperationExample[OneHotEncode] {

  override def dOperation: OneHotEncode = {
    val op = new OneHotEncode()
    op.transformer
      .setSingleColumn("labels", "encoded")
    op.set(op.transformer.extractParamMap())
  }

  override def inputDataFrames: Seq[DataFrame] = {
    val data         = "a a b c a b a a c".split(" ").map(Tuple1(_))
    val rawDataFrame =
      DataFrame.fromSparkDataFrame(sparkSQLSession.createDataFrame(data).toDF("features"))
    val x            = new StringIndexer()
      .setSingleColumn("features", "labels")
      .executeUntyped(Vector(rawDataFrame))(executionContext)
    Seq(x.head.asInstanceOf[DataFrame])
  }

}
