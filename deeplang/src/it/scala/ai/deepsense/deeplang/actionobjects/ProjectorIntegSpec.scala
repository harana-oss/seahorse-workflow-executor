package ai.deepsense.deeplang.actionobjects

import java.sql.Timestamp
import org.apache.spark.sql.Row
import org.apache.spark.sql.types._
import org.joda.time.DateTime
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import ai.deepsense.deeplang._
import ai.deepsense.deeplang.actionobjects.Projector.ColumnProjection
import ai.deepsense.deeplang.actionobjects.Projector.RenameColumnChoice.Yes
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actionobjects.spark.wrappers.transformers.TransformerSerialization
import ai.deepsense.deeplang.actions.exceptions.ColumnDoesNotExistException
import ai.deepsense.deeplang.actions.exceptions.DuplicatedColumnsException
import ai.deepsense.deeplang.parameters.selections._
import org.scalatest.matchers.should.Matchers

class ProjectorIntegSpec
    extends DeeplangIntegTestSupport
    with ScalaCheckDrivenPropertyChecks
    with Matchers
    with TransformerSerialization {

  import DeeplangIntegTestSupport._
  import TransformerSerialization._

  val specialCharactersName = "a'a-z"

  val columns = Seq(
    StructField("c", IntegerType),
    StructField("b", StringType),
    StructField(specialCharactersName, DoubleType),
    StructField("x", TimestampType),
    StructField("z", BooleanType)
  )

  def schema: StructType = StructType(columns)

  //         "c"/0  "b"/1   "a"/2 "x"/3                                  "z"/4
  val row1 = Seq(1, "str1", 10.0, new Timestamp(DateTime.now.getMillis), true)

  val row2 = Seq(2, "str2", 20.0, new Timestamp(DateTime.now.getMillis), false)

  val row3 = Seq(3, "str3", 30.0, new Timestamp(DateTime.now.getMillis), false)

  val data = Seq(row1, row2, row3)

  val dataFrame = createDataFrame(data.map(Row.fromSeq), schema)

  "Projector" should {
    val expectedSchema = StructType(
      Seq(StructField(specialCharactersName, DoubleType), StructField(s"renamed_$specialCharactersName", DoubleType))
    )
    val transformer    = new Projector().setProjectionColumns(
      Seq(
        ColumnProjection().setOriginalColumn(NameSingleColumnSelection(specialCharactersName)),
        ColumnProjection()
          .setOriginalColumn(NameSingleColumnSelection(specialCharactersName))
          .setRenameColumn(new Yes().setColumnName(s"renamed_$specialCharactersName"))
      )
    )

    "select correctly the same column multiple times" in {
      val projected                        = transformer._transform(executionContext, dataFrame)
      val expectedData                     = data.map(r => Seq(r(2), r(2)))
      val expectedDataFrame                = createDataFrame(expectedData.map(Row.fromSeq), expectedSchema)
      assertDataFramesEqual(projected, expectedDataFrame)
      val projectedBySerializedTransformer = projectedUsingSerializedTransformer(transformer)
      assertDataFramesEqual(projected, projectedBySerializedTransformer)
    }
    "infer correct schema" in {
      val filteredSchema = transformer._transformSchema(schema)
      filteredSchema shouldBe Some(expectedSchema)
    }
    "throw an exception" when {
      "the columns selected by name does not exist" when {
        val transformer = new Projector().setProjectionColumns(
          Seq(
            ColumnProjection().setOriginalColumn(NameSingleColumnSelection("thisColumnDoesNotExist"))
          )
        )
        "transforming a DataFrame" in {
          intercept[ColumnDoesNotExistException] {
            transformer._transform(executionContext, dataFrame)
          }
        }
        "transforming a schema" in {
          intercept[ColumnDoesNotExistException] {
            transformer._transformSchema(schema)
          }
        }
      }
      "the columns selected by index does not exist" when {
        val transformer = new Projector().setProjectionColumns(
          Seq(
            ColumnProjection().setOriginalColumn(IndexSingleColumnSelection(1000))
          )
        )
        "transforming a DataFrame" in {
          intercept[ColumnDoesNotExistException] {
            transformer._transform(executionContext, dataFrame)
          }
        }
        "transforming a schema" in {
          intercept[ColumnDoesNotExistException] {
            transformer._transformSchema(schema)
          }
        }
      }
      "the output DataFrame has duplicated columns" when {
        val transformer = new Projector().setProjectionColumns(
          Seq(
            ColumnProjection()
              .setOriginalColumn(NameSingleColumnSelection(specialCharactersName))
              .setRenameColumn(new Yes().setColumnName("duplicatedName")),
            ColumnProjection()
              .setOriginalColumn(NameSingleColumnSelection("b"))
              .setRenameColumn(new Yes().setColumnName("duplicatedName"))
          )
        )
        "transforming a DataFrame" in {
          intercept[DuplicatedColumnsException] {
            transformer._transform(executionContext, dataFrame)
          }
        }
      }
      "the transformer uses output column name with backticks" when {
        val transformer = new Projector().setProjectionColumns(
          Seq(
            ColumnProjection()
              .setOriginalColumn(NameSingleColumnSelection(specialCharactersName))
              .setRenameColumn(new Yes().setColumnName("columnName")),
            ColumnProjection()
              .setOriginalColumn(NameSingleColumnSelection("b"))
              .setRenameColumn(new Yes().setColumnName("column`Name`With``Backticks`"))
          )
        )
        "transforming a DataFrame" in {
          // TODO: Spark 1.6 does not have ParseException, which is thrown in Spark 2.0
          intercept[Exception] {
            transformer._transform(executionContext, dataFrame)
          }
        }
      }
    }
  }
  it when {
    "selection is empty" should {
      val emptyProjector = new Projector().setProjectionColumns(Seq())
      "produce an empty DataFrame" in {
        val emptyDataFrame = emptyProjector._transform(executionContext, dataFrame)
        emptyDataFrame.sparkDataFrame.collectAsList() shouldBe empty
      }
      "produce an empty schema" in {
        val Some(inferredSchema) = emptyProjector._transformSchema(schema)
        inferredSchema.fields shouldBe empty
      }
    }
  }

  private def projectedUsingSerializedTransformer(transformer: Transformer): DataFrame =
    transformer.loadSerializedTransformer(tempDir)._transform(executionContext, dataFrame)

}
