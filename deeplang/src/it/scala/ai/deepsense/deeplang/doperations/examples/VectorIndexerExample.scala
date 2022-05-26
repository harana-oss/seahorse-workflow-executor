package ai.deepsense.deeplang.doperations.examples

import ai.deepsense.sparkutils.Linalg.Vectors

import ai.deepsense.deeplang.doperables.dataframe.DataFrame
import ai.deepsense.deeplang.doperations.spark.wrappers.estimators.VectorIndexer

class VectorIndexerExample extends AbstractOperationExample[VectorIndexer] {

  override def dOperation: VectorIndexer = {
    val op = new VectorIndexer()
    op.estimator
      .setMaxCategories(3)
      .setInputColumn("vectors")
      .setNoInPlace("indexed")
    op.set(op.estimator.extractParamMap())
  }

  override def inputDataFrames: Seq[DataFrame] = {
    val data =
      Seq(Vectors.dense(1.0, 1.0, 0.0, 1.0), Vectors.dense(0.0, 1.0, 1.0, 1.0), Vectors.dense(-1.0, 1.0, 2.0, 0.0))
        .map(Tuple1(_))
    Seq(DataFrame.fromSparkDataFrame(sparkSQLSession.createDataFrame(data).toDF("vectors")))
  }

}
