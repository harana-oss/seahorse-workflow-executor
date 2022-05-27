package ai.deepsense.deeplang.actions.examples

import ai.deepsense.sparkutils.Linalg.Vectors

import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actions.spark.wrappers.estimators.IDF

class IDFExample extends AbstractOperationExample[IDF] {

  override def dOperation: IDF = {
    val op = new IDF()
    op.estimator
      .setInputColumn("features")
      .setNoInPlace("values")
    op.set(op.estimator.extractParamMap())
  }

  override def inputDataFrames: Seq[DataFrame] = {
    val numOfFeatures = 4
    val data          = Seq(
      Vectors.sparse(numOfFeatures, Array(1, 3), Array(1.0, 2.0)).toDense,
      Vectors.dense(0.0, 1.0, 2.0, 3.0),
      Vectors.sparse(numOfFeatures, Array(1), Array(1.0)).toDense
    ).map(Tuple1(_))
    Seq(DataFrame.fromSparkDataFrame(sparkSQLSession.createDataFrame(data).toDF("features")))
  }

}
