package io.deepsense.deeplang.doperations.examples

import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperations.spark.wrappers.transformers.HashingTF

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
