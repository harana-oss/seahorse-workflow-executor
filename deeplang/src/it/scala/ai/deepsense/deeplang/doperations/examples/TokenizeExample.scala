package ai.deepsense.deeplang.doperations.examples

import ai.deepsense.deeplang.doperables.dataframe.DataFrame
import ai.deepsense.deeplang.doperations.spark.wrappers.transformers.Tokenize

class TokenizeExample extends AbstractOperationExample[Tokenize] {

  override def dOperation: Tokenize = {
    val op = new Tokenize()
    op.transformer
      .setSingleColumn("sentence", "tokenized")
    op.set(op.transformer.extractParamMap())
  }

  override def inputDataFrames: Seq[DataFrame] = {
    val sparkDataFrame = sparkSQLSession
      .createDataFrame(
        Seq(
          (0, "Hi I heard about Spark"),
          (1, "I wish Java could use case classes"),
          (2, "Logistic,regression,models,are,neat")
        )
      )
      .toDF("label", "sentence")
    Seq(DataFrame.fromSparkDataFrame(sparkDataFrame))
  }

}
