package ai.deepsense.deeplang.actions.examples

import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actions.spark.wrappers.transformers.ConvertToNGrams

class ConvertToNGramsExample extends AbstractOperationExample[ConvertToNGrams] {

  override def dOperation: ConvertToNGrams = {
    val op = new ConvertToNGrams()
    op.transformer
      .setSingleColumn("words", "output")
      .setN(3)

    op.set(op.transformer.extractParamMap())
  }

  override def inputDataFrames: Seq[DataFrame] = {
    Seq(
      DataFrame.fromSparkDataFrame(
        sparkSQLSession
          .createDataFrame(
            Seq(
              (0, Array("Hi", "I", "heard", "about", "Spark")),
              (1, Array("I", "wish", "Java", "could", "use", "case", "classes")),
              (2, Array("Logistic", "regression", "models", "are", "neat"))
            )
          )
          .toDF("label", "words")
      )
    )
  }

}
