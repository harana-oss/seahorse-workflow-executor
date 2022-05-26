package io.deepsense.deeplang.doperations.examples

import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperations.spark.wrappers.transformers.TokenizeWithRegex

class TokenizeWithRegexExample extends AbstractOperationExample[TokenizeWithRegex] {

  override def dOperation: TokenizeWithRegex = {
    val op = new TokenizeWithRegex()
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
