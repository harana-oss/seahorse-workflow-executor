package io.deepsense.deeplang.doperables

import org.apache.spark.sql.Row
import org.apache.spark.sql.types.DoubleType
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType

import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.params.ParamPair
import io.deepsense.deeplang.DKnowledge
import io.deepsense.deeplang.DeeplangIntegTestSupport
import io.deepsense.sparkutils.Linalg.Vectors

abstract class AbstractEvaluatorSmokeTest extends DeeplangIntegTestSupport {

  def className: String

  val evaluator: Evaluator

  val evaluatorParams: Seq[ParamPair[_]]

  val inputDataFrameSchema = StructType(
    Seq(
      StructField("s", StringType),
      StructField("prediction", DoubleType),
      StructField("rawPrediction", new io.deepsense.sparkutils.Linalg.VectorUDT),
      StructField("label", DoubleType)
    )
  )

  val inputDataFrame: DataFrame = {
    val rowSeq = Seq(
      Row("aAa bBb cCc dDd eEe f", 1.0, Vectors.dense(2.1, 2.2, 2.3), 3.0),
      Row("das99213 99721 8i!#@!", 4.0, Vectors.dense(5.1, 5.2, 5.3), 6.0)
    )
    createDataFrame(rowSeq, inputDataFrameSchema)
  }

  def setUpStubs(): Unit = ()

  className should {
    "successfully run _evaluate()" in {
      setUpStubs()
      evaluator.set(evaluatorParams: _*)._evaluate(executionContext, inputDataFrame)
    }
    "successfully run _infer()" in {
      evaluator.set(evaluatorParams: _*)._infer(DKnowledge(inputDataFrame))
    }
    "successfully run report" in {
      evaluator.set(evaluatorParams: _*).report
    }
  }

}
