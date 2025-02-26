package ai.deepsense.deeplang.actions.examples

import ai.deepsense.sparkutils.Linalg.Vectors

import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actions.spark.wrappers.transformers.DCT

class DCTExample extends AbstractOperationExample[DCT] {

  override def dOperation: DCT = {
    val op = new DCT()
    op.transformer
      .setSingleColumn("features", "output")
    op.set(op.transformer.extractParamMap())
  }

  override def inputDataFrames: Seq[DataFrame] = {
    val data =
      Seq(Vectors.dense(0.0, 1.0, -2.0, 3.0), Vectors.dense(-1.0, 2.0, 4.0, -7.0), Vectors.dense(14.0, -2.0, -5.0, 1.0))
        .map(Tuple1(_))
    Seq(DataFrame.fromSparkDataFrame(sparkSQLSession.createDataFrame(data).toDF("features")))
  }

}
