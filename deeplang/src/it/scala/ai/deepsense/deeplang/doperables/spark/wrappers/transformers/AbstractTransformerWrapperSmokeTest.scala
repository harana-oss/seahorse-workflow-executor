package ai.deepsense.deeplang.doperables.spark.wrappers.transformers

import ai.deepsense.sparkutils.Linalg.Vectors
import org.apache.spark.sql.Row
import org.apache.spark.sql.types._

import ai.deepsense.deeplang.DeeplangIntegTestSupport
import ai.deepsense.deeplang.doperables.Transformer
import ai.deepsense.deeplang.doperables.dataframe.DataFrame

abstract class AbstractTransformerWrapperSmokeTest[+T <: Transformer]
    extends DeeplangIntegTestSupport
    with TransformerSerialization {

  import DeeplangIntegTestSupport._
  import TransformerSerialization._

  def transformerWithParams: T

  def deserializedTransformer: Transformer =
    transformerWithParams.loadSerializedTransformer(tempDir)

  final def className: String = transformerWithParams.getClass.getSimpleName

  val inputDataFrameSchema = StructType(
    Seq(
      StructField("as", ArrayType(StringType, containsNull = true)),
      StructField("s", StringType),
      StructField("i", IntegerType),
      StructField("i2", IntegerType),
      StructField("d", DoubleType),
      StructField("v", new ai.deepsense.sparkutils.Linalg.VectorUDT)
    )
  )

  lazy val inputDataFrame: DataFrame = {
    val rowSeq = Seq(
      Row(Array("ala", "kot", "ala"), "aAa bBb cCc", 0, 0, 0.0, Vectors.dense(0.0, 0.0, 0.0)),
      Row(Array(null), "das99213 99721 8i!#@!", 1, 1, 1.0, Vectors.dense(1.0, 1.0, 1.0))
    )
    createDataFrame(rowSeq, inputDataFrameSchema)
  }

  className should {
    "successfully run _transform()" in {
      val transformed                        = transformerWithParams._transform(executionContext, inputDataFrame)
      val transformedBySerializedTransformer =
        deserializedTransformer._transform(executionContext, inputDataFrame)
      assertDataFramesEqual(transformed, transformedBySerializedTransformer)
    }
    "successfully run _transformSchema()" in {
      val transformedSchema  =
        transformerWithParams._transformSchema(inputDataFrame.sparkDataFrame.schema)
      val transformedSchema2 =
        deserializedTransformer._transformSchema(inputDataFrame.sparkDataFrame.schema)
      assertSchemaEqual(transformedSchema.get, transformedSchema2.get)
    }
    "succesfully run report" in {
      val report  = transformerWithParams.report()
      val report2 = deserializedTransformer.report()
      report shouldBe report2
    }
  }

}
