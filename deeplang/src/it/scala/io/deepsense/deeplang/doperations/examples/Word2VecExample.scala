package io.deepsense.deeplang.doperations.examples

import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperations.spark.wrappers.estimators.Word2Vec

class Word2VecExample extends AbstractOperationExample[Word2Vec] {
  override def dOperation: Word2Vec = {
    val op = new Word2Vec()
    op.estimator
      .setInputColumn("words")
      .setNoInPlace("vectors")
      .setMinCount(2)
      .setVectorSize(5)
    op.set(op.estimator.extractParamMap())
  }

  override def inputDataFrames: Seq[DataFrame] = {
    val data = Seq(
      "Lorem ipsum at dolor".split(" "),
      "Nullam gravida non ipsum".split(" "),
      "Etiam at nunc lacinia".split(" ")
    ).map(Tuple1(_))
    Seq(DataFrame.fromSparkDataFrame(sparkSQLSession.createDataFrame(data).toDF("words")))
  }
}
