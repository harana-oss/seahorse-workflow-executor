package ai.deepsense.deeplang.actions.examples

import ai.deepsense.sparkutils.Linalg.Vectors

import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actions.spark.wrappers.transformers.Normalize

class NormalizeExample extends AbstractOperationExample[Normalize] {

  override def dOperation: Normalize = {
    val op = new Normalize()
    op.transformer
      .setSingleColumn("features", "normalized")
    op.set(op.transformer.extractParamMap())
  }

  override def inputDataFrames: Seq[DataFrame] = {
    val data = Seq(
      Vectors.sparse(3, Seq((0, -2.0), (1, 2.3))).toDense,
      Vectors.dense(0.0, 0.0, 0.0),
      Vectors.dense(0.6, -1.1, -3.0),
      Vectors.sparse(3, Seq((1, 0.91), (2, 3.2))).toDense,
      Vectors.sparse(3, Seq((0, 5.7), (1, 0.72), (2, 2.7))).toDense,
      Vectors.sparse(3, Seq()).toDense
    ).map(Tuple1(_))
    Seq(DataFrame.fromSparkDataFrame(sparkSQLSession.createDataFrame(data).toDF("features")))
  }

}
