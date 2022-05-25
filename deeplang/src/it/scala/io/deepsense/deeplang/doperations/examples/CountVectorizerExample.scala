package io.deepsense.deeplang.doperations.examples

import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{ArrayType, StringType, StructField, StructType}

import io.deepsense.deeplang.doperables.dataframe.{DataFrame, DataFrameBuilder}
import io.deepsense.deeplang.doperations.spark.wrappers.estimators.CountVectorizer

class CountVectorizerExample extends AbstractOperationExample[CountVectorizer]{
  override def dOperation: CountVectorizer = {
    val op = new CountVectorizer()
    op.estimator
      .setInputColumn("lines")
      .setNoInPlace("lines_out")
      .setMinTF(3)
    op.set(op.estimator.extractParamMap())
  }

  override def inputDataFrames: Seq[DataFrame] = {
    val rows = Seq(
      Row("a a a b b c c c d ".split(" ").toSeq),
      Row("c c c c c c".split(" ").toSeq),
      Row("a".split(" ").toSeq),
      Row("e e e e e".split(" ").toSeq))
    val rdd = sparkContext.parallelize(rows)
    val schema = StructType(Seq(StructField("lines", ArrayType(StringType, containsNull = true))))
    Seq(DataFrameBuilder(sparkSQLSession).buildDataFrame(schema, rdd))
  }
}
