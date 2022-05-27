package ai.deepsense.deeplang.actions.examples

import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actions.spark.wrappers.estimators.QuantileDiscretizer

class QuantileDiscretizerExample extends AbstractOperationExample[QuantileDiscretizer] {

  override def dOperation: QuantileDiscretizer = {
    val op = new QuantileDiscretizer()
    op.estimator
      .setInputColumn("features")
      .setNoInPlace("discretized_features")
      .setNumBuckets(3)
    op.set(op.estimator.extractParamMap())
  }

  override def inputDataFrames: Seq[DataFrame] = {
    val data = Array(1.0, 2.0, 3.0, 4.0, 5.0, 6.0).map(Tuple1(_))
    Seq(DataFrame.fromSparkDataFrame(sparkSQLSession.createDataFrame(data).toDF("features")))
  }

}
