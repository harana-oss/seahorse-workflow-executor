package ai.deepsense.deeplang.actions.examples

import ai.deepsense.sparkutils.Linalg.Vectors

import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actions.spark.wrappers.estimators.ChiSqSelector
import ai.deepsense.deeplang.parameters.selections.NameSingleColumnSelection

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
