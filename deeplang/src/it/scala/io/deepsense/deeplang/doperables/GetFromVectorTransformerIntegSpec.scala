package io.deepsense.deeplang.doperables

import org.apache.spark.sql.Row
import org.apache.spark.sql.types._
import org.scalatest.Matchers
import org.scalatest.prop.GeneratorDrivenPropertyChecks

import io.deepsense.deeplang._
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperables.multicolumn.MultiColumnParams.SingleOrMultiColumnChoices.SingleColumnChoice
import io.deepsense.deeplang.doperables.spark.wrappers.transformers.TransformerSerialization
import io.deepsense.deeplang.doperations.exceptions.ColumnDoesNotExistException
import io.deepsense.deeplang.params.selections._
import io.deepsense.sparkutils.Linalg.Vectors

class GetFromVectorTransformerIntegSpec
  extends DeeplangIntegTestSupport
  with GeneratorDrivenPropertyChecks
  with Matchers
  with TransformerSerialization {

  import DeeplangIntegTestSupport._
  import TransformerSerialization._

  val columns = Seq(
    StructField("id", IntegerType),
    StructField("data", new io.deepsense.sparkutils.Linalg.VectorUDT()))
  def schema: StructType = StructType(columns)

  //         "id"/0  "a"/1
  val row1 = Seq(1, Vectors.dense(1.0, 10.0, 100.0))
  val row2 = Seq(2, Vectors.sparse(3, Seq((0, 2.0), (1, 20.0), (2, 200.0))))
  val row3 = Seq(3, null)
  val data = Seq(row1, row2, row3)
  val dataFrame = createDataFrame(data.map(Row.fromSeq), schema)

  "GetFromVectorTransformer" should {
    val expectedSchema = StructType(Seq(
      StructField("id", IntegerType),
      StructField("data", DoubleType)))
    val transformer = new GetFromVectorTransformer()
      .setIndex(1)
      .setSingleOrMultiChoice(
        SingleColumnChoice().setInputColumn(NameSingleColumnSelection("data")))

    "infer correct schema" in {
      val filteredSchema = transformer._transformSchema(schema)
      filteredSchema shouldBe Some(expectedSchema)
    }
    "select correctly data from vector" in {
      val transformed = transformer._transform(executionContext, dataFrame)
      val expectedData = data.map { r =>
        val vec = r(1)
        if (vec != null) {
          Seq(r.head, vec.asInstanceOf[io.deepsense.sparkutils.Linalg.Vector](1))
        } else {
          Seq(r.head, null)
        }
      }
      val expectedDataFrame = createDataFrame(expectedData.map(Row.fromSeq), expectedSchema)
      assertDataFramesEqual(transformed, expectedDataFrame)
      val projectedBySerializedTransformer = projectedUsingSerializedTransformer(transformer)
      assertDataFramesEqual(transformed, projectedBySerializedTransformer)
    }
    "throw an exception" when {
      "the selected column does not exist" when {
        val transformer = new GetFromVectorTransformer()
          .setIndex(1)
          .setSingleOrMultiChoice(SingleColumnChoice().setInputColumn(
            NameSingleColumnSelection("thisColumnDoesNotExist")))
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
    }
  }

  private def projectedUsingSerializedTransformer(transformer: Transformer): DataFrame = {
    transformer.loadSerializedTransformer(tempDir)._transform(executionContext, dataFrame)
  }
}
