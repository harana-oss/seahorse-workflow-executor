package ai.deepsense.deeplang.actions

import org.apache.spark.sql.Row
import org.apache.spark.sql.types._

import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.inference.SqlInferenceWarning
import ai.deepsense.deeplang.parameters.exceptions.ParamsEqualException
import ai.deepsense.deeplang.Knowledge
import ai.deepsense.deeplang.DeeplangIntegTestSupport

class SqlCombineSpec extends DeeplangIntegTestSupport {

  import DeeplangIntegTestSupport._

  private val leftName = "left"

  private val leftData = Seq(
    Row("c", 5.0, true),
    Row("a", 5.0, null),
    Row("b", null, false),
    Row(null, 2.1, true)
  )

  private val (firstLeftColumn, secondLeftColumn, thirdLeftColumn) = ("left1", "left2", "left3")

  private val leftSchema = StructType(
    Seq(
      StructField(firstLeftColumn, StringType),
      StructField(secondLeftColumn, DoubleType),
      StructField(thirdLeftColumn, BooleanType)
    )
  )

  private val leftDf = createDataFrame(leftData, leftSchema)

  private val rightName = "right"

  private val rightData = Seq(
    Row(5.0, "x"),
    Row(null, "y"),
    Row(null, null)
  )

  private val (firstRightColumn, secondRightColumn) = ("right1", "right2")

  private val rightSchema = StructType(
    Seq(
      StructField(firstRightColumn, DoubleType),
      StructField(secondRightColumn, StringType)
    )
  )

  private val rightDf = createDataFrame(rightData, rightSchema)

  "SqlCombine" should {
    "recognize left dataframe name" in {
      val expression = s"SELECT * FROM $leftName"
      val result     = executeSqlCombine(expression, leftName, leftDf, rightName, rightDf)
      assertDataFramesEqual(result, leftDf)
    }
    "recognize right dataFrame name" in {
      val expression = s"SELECT * FROM $rightName"
      val result     = executeSqlCombine(expression, leftName, leftDf, rightName, rightDf)
      assertDataFramesEqual(result, rightDf)
    }
    "allow an arbitrary operation using both dataFrames" in {
      val expression     =
        s"""SELECT l.$firstLeftColumn AS first_letter,
           |r.$secondRightColumn AS second_letter
           |FROM $leftName l INNER JOIN $rightName r ON l.$secondLeftColumn = r.$firstRightColumn
         """.stripMargin
      val result         = executeSqlCombine(expression, leftName, leftDf, rightName, rightDf)
      val expectedData   = Seq(
        Row("c", "x"),
        Row("a", "x")
      )
      val expectedSchema = StructType(
        Seq(
          StructField("first_letter", StringType),
          StructField("second_letter", StringType)
        )
      )
      val expected       = createDataFrame(expectedData, expectedSchema)
      assertDataFramesEqual(result, expected)
    }
    "fail validations if DataFrame names are the same" in {
      val expression = "SELECT * FROM x"
      val combine    = new SqlCombine()
        .setLeftTableName("x")
        .setRightTableName("x")
        .setSqlCombineExpression(expression)
      combine.validateParams should
        contain(ParamsEqualException("left dataframe id", "right dataframe id", "x"))
    }
    "infer schema" in {
      val expression         =
        s"""SELECT $leftName.$secondLeftColumn*$rightName.$firstRightColumn x,
           |$rightName.$secondRightColumn
           |FROM $leftName, $rightName
         """.stripMargin
      val (result, warnings) = inferSqlCombineSchema(expression, leftName, leftSchema, rightName, rightSchema)

      val expectedSchema = StructType(
        Seq(
          StructField("x", DoubleType),
          StructField(secondRightColumn, StringType)
        )
      )
      warnings shouldBe empty
      result shouldEqual expectedSchema
    }
    "fail schema inference for invalid expression" in {
      val expression    =
        s"""SELEC $leftName.$firstLeftColumn FROM $leftName"""
      val (_, warnings) = inferSqlCombineSchema(expression, leftName, leftSchema, rightName, rightSchema)

      warnings.warnings.length shouldBe 1
      val warning = warnings.warnings(0)
      warning shouldBe a[SqlInferenceWarning]
    }
    "not throw exception during inference when its parameters are not set" in {
      val expression           = s"""SELECT * from $leftName"""
      val parameterlessCombine = new SqlCombine()
      inferSqlCombineSchema(parameterlessCombine, expression, leftName, leftSchema, rightName, rightSchema)
    }
  }

  private def executeSqlCombine(
      expression: String,
      leftName: String,
      leftData: DataFrame,
      rightName: String,
      rightData: DataFrame
  ): DataFrame = {
    val combine = new SqlCombine()
      .setLeftTableName(leftName)
      .setRightTableName(rightName)
      .setSqlCombineExpression(expression)

    executeOperation(combine, leftData, rightData)
  }

  private def inferSqlCombineSchema(
      expression: String,
      leftName: String,
      leftSchema: StructType,
      rightName: String,
      rightSchema: StructType
  ): (StructType, InferenceWarnings) = {
    val combine = new SqlCombine()
      .setLeftTableName(leftName)
      .setRightTableName(rightName)
      .setSqlCombineExpression(expression)
    inferSqlCombineSchema(combine, expression, leftName, leftSchema, rightName, rightSchema)
  }

  private def inferSqlCombineSchema(
      combine: SqlCombine,
      expression: String,
      leftName: String,
      leftSchema: StructType,
      rightName: String,
      rightSchema: StructType
  ) = {
    val (knowledge, warnings) = combine.inferKnowledgeUntyped(
      Vector(
        Knowledge(DataFrame.forInference(leftSchema)),
        Knowledge(DataFrame.forInference(rightSchema))
      )
    )(mock[InferContext])

    val dataFrameKnowledge = knowledge.head.single.asInstanceOf[DataFrame]
    (dataFrameKnowledge.schema.get, warnings)
  }

}
