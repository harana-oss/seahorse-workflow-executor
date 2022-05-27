package ai.deepsense.deeplang.actions.examples

import ai.deepsense.sparkutils.Linalg.Vectors

import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actions.spark.wrappers.estimators.StandardScaler

class StandardScalerExample extends AbstractOperationExample[StandardScaler] {

  override def dOperation: StandardScaler = {
    val op = new StandardScaler()
    op.estimator
      .setInputColumn("features")
      .setNoInPlace("scaled")
    op.set(op.estimator.extractParamMap())
  }

  override def inputDataFrames: Seq[DataFrame] = {
    val data = Array(
      Vectors.dense(-2.0, 2.3, 0.0),
      Vectors.dense(0.0, -5.1, 1.0),
      Vectors.dense(1.7, -0.6, 3.3)
    ).map(Tuple1(_))
    Seq(DataFrame.fromSparkDataFrame(sparkSQLSession.createDataFrame(data).toDF("features")))
  }

}
