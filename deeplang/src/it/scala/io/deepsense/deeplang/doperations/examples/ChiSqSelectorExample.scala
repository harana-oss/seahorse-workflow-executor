package io.deepsense.deeplang.doperations.examples

import io.deepsense.sparkutils.Linalg.Vectors

import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperations.spark.wrappers.estimators.ChiSqSelector
import io.deepsense.deeplang.params.selections.NameSingleColumnSelection

class ChiSqSelectorExample extends AbstractOperationExample[ChiSqSelector] {

  override def dOperation: ChiSqSelector = {
    val op = new ChiSqSelector()
    op.estimator
      .setFeaturesColumn(NameSingleColumnSelection("features"))
      .setLabelColumn(NameSingleColumnSelection("label"))
      .setOutputColumn("selected_features")
      .setNumTopFeatures(1)
    op.set(op.estimator.extractParamMap())
  }

  override def inputDataFrames: Seq[DataFrame] = {
    val data = Seq(
      (Vectors.dense(0.0, 0.0, 18.0, 1.0), 1.0),
      (Vectors.dense(0.0, 1.0, 12.0, 0.0), 0.0),
      (Vectors.dense(1.0, 0.0, 15.0, 0.1), 0.0)
    )
    Seq(DataFrame.fromSparkDataFrame(sparkSQLSession.createDataFrame(data).toDF("features", "label")))
  }

}
