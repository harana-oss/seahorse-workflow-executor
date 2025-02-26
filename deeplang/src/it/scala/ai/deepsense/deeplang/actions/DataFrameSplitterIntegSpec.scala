package ai.deepsense.deeplang.actions

import ai.deepsense.deeplang._
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{IntegerType, StructField, StructType}
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.collection.JavaConverters._

class DataFrameSplitterIntegSpec extends DeeplangIntegTestSupport with ScalaCheckDrivenPropertyChecks with Matchers {

  "SplitDataFrame" should {
    "split randomly one df into two df in given range" in {

      val input = Range(1, 100)

      val parameterPairs = List((0.0, 0), (0.3, 1), (0.5, 2), (0.8, 3), (1.0, 4))

      for ((splitRatio, seed) <- parameterPairs) {
        val rdd        = createData(input)
        val df         = executionContext.dataFrameBuilder.buildDataFrame(createSchema, rdd)
        val (df1, df2) = executeOperation(
          executionContext,
          new Split()
            .setSplitMode(
              SplitModeChoice
                .Random()
                .setSplitRatio(splitRatio)
                .setSeed(seed / 2)
            )
        )(df)
        validateSplitProperties(df, df1, df2)
      }
    }

    "split conditionally one df into two df in given range" in {

      val input = Range(1, 100)

      val condition                 = "value > 20"
      val predicate: Int => Boolean = _ > 20

      val (expectedDF1, expectedDF2) =
        (input.filter(predicate), input.filter(!predicate(_)))

      val rdd        = createData(input)
      val df         = executionContext.dataFrameBuilder.buildDataFrame(createSchema, rdd)
      val (df1, df2) = executeOperation(
        executionContext,
        new Split()
          .setSplitMode(
            SplitModeChoice
              .Conditional()
              .setCondition(condition)
          )
      )(df)
      df1.sparkDataFrame.collect().map(_.get(0)) should contain theSameElementsAs expectedDF1
      df2.sparkDataFrame.collect().map(_.get(0)) should contain theSameElementsAs expectedDF2
      validateSplitProperties(df, df1, df2)
    }
  }

  private def createSchema: StructType = {
    StructType(
      List(
        StructField("value", IntegerType, nullable = false)
      )
    )
  }

  private def createData(data: Seq[Int]): RDD[Row] =
    sparkContext.parallelize(data.map(Row(_)))

  private def executeOperation(context: ExecutionContext, operation: Action)(
      dataFrame: DataFrame
  ): (DataFrame, DataFrame) = {
    val operationResult = operation.executeUntyped(Vector[ActionObject](dataFrame))(context)
    val df1             = operationResult.head.asInstanceOf[DataFrame]
    val df2             = operationResult.last.asInstanceOf[DataFrame]
    (df1, df2)
  }

  def validateSplitProperties(inputDF: DataFrame, outputDF1: DataFrame, outputDF2: DataFrame): Unit = {
    val dfCount   = inputDF.sparkDataFrame.count()
    val df1Count  = outputDF1.sparkDataFrame.count()
    val df2Count  = outputDF2.sparkDataFrame.count()
    val rowsDf    = inputDF.sparkDataFrame.collectAsList().asScala
    val rowsDf1   = outputDF1.sparkDataFrame.collectAsList().asScala
    val rowsDf2   = outputDF2.sparkDataFrame.collectAsList().asScala
    val intersect = rowsDf1.intersect(rowsDf2)
    intersect.size shouldBe 0
    (df1Count + df2Count) shouldBe dfCount
    rowsDf.toSet shouldBe rowsDf1.toSet.union(rowsDf2.toSet)
  }

}
