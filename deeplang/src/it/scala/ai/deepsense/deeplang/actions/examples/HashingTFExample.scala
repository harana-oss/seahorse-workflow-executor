package ai.deepsense.deeplang.actions.examples

import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actions.spark.wrappers.transformers.HashingTF

class HashingTFExample extends AbstractOperationExample[HashingTF] {

  override def dOperation: HashingTF = {
    val op = new HashingTF()
    op.transformer
      .setSingleColumn("signal", "hash")
      .setNumFeatures(5)
    op.set(op.transformer.extractParamMap())
  }

  override def inputDataFrames: Seq[DataFrame] = {
    val data = Seq("a a b b c d".split(" ").toSeq).map(Tuple1(_))
    Seq(DataFrame.fromSparkDataFrame(sparkSQLSession.createDataFrame(data).toDF("signal")))
  }

}
