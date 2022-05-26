package io.deepsense.deeplang.doperations.examples

import io.deepsense.sparkutils.Linalg.Vectors

import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperations.spark.wrappers.estimators.PCA

class PCAExample extends AbstractOperationExample[PCA] {

  override def dOperation: PCA = {
    val op = new PCA()
    op.estimator
      .setInputColumn("features")
      .setNoInPlace("pca_features")
      .setK(3)
    op.set(op.estimator.extractParamMap())
  }

  override def inputDataFrames: Seq[DataFrame] = {
    val data = Array(
      Vectors.sparse(5, Seq((1, 1.0), (3, 7.0))).toDense,
      Vectors.dense(2.0, 0.0, 3.0, 4.0, 5.0),
      Vectors.dense(4.0, 0.0, 0.0, 6.0, 7.0)
    ).map(Tuple1(_))
    Seq(DataFrame.fromSparkDataFrame(sparkSQLSession.createDataFrame(data).toDF("features")))
  }

}
