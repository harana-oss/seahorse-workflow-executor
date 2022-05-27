package ai.deepsense.deeplang.actions.examples

import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actions.spark.wrappers.transformers.RemoveStopWords

class RemoveStopWordsExample extends AbstractOperationExample[RemoveStopWords] {

  override def dOperation: RemoveStopWords = {
    val op = new RemoveStopWords()
    op.transformer
      .setSingleColumn("raw", "removed")
    op.set(op.transformer.extractParamMap())
  }

  override def inputDataFrames: Seq[DataFrame] = {
    val sparkDataFrame = sparkSQLSession
      .createDataFrame(
        Seq(
          (0, Seq("I", "saw", "the", "red", "baloon")),
          (1, Seq("Mary", "had", "a", "little", "lamb"))
        )
      )
      .toDF("id", "raw")
    Seq(DataFrame.fromSparkDataFrame(sparkDataFrame))
  }

}
