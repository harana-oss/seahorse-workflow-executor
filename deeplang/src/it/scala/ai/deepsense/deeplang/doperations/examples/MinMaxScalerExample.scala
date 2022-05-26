package ai.deepsense.deeplang.doperations.examples

import ai.deepsense.sparkutils.Linalg.Vectors

import ai.deepsense.deeplang.doperables.dataframe.DataFrame
import ai.deepsense.deeplang.doperations.spark.wrappers.estimators.MinMaxScaler

class MinMaxScalerExample extends AbstractOperationExample[MinMaxScaler] {

  override def dOperation: MinMaxScaler = {
    val op = new MinMaxScaler()
    op.estimator
      .setInputColumn("features")
      .setNoInPlace("scaled")
      .setMax(5)
      .setMin(-5)
    op.set(op.estimator.extractParamMap())
  }

  override def inputDataFrames: Seq[DataFrame] = {
    val data = Array(
      Vectors.dense(1, 0, Long.MinValue),
      Vectors.dense(2, 0, 0),
      Vectors.sparse(3, Array(0, 2), Array(3, Long.MaxValue)).toDense,
      Vectors.sparse(3, Array(0), Array(1.5)).toDense
    ).map(Tuple1(_))
    Seq(DataFrame.fromSparkDataFrame(sparkSQLSession.createDataFrame(data).toDF("features")))
  }

}
