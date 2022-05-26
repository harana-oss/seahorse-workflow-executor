package io.deepsense.deeplang.doperables

import org.apache.spark.sql.types.DoubleType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType

import io.deepsense.deeplang.doperables.MultiColumnTransformerIntegSpec._
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperables.multicolumn.MultiColumnParams.MultiColumnInPlaceChoices.MultiColumnNoInPlace
import io.deepsense.deeplang.doperables.multicolumn.MultiColumnParams.MultiColumnInPlaceChoices.MultiColumnYesInPlace
import io.deepsense.deeplang.doperables.multicolumn.MultiColumnParams.SingleOrMultiColumnChoices.MultiColumnChoice
import io.deepsense.deeplang.doperables.multicolumn.MultiColumnParams.SingleOrMultiColumnChoices.SingleColumnChoice
import io.deepsense.deeplang.doperables.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.NoInPlaceChoice
import io.deepsense.deeplang.doperables.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.YesInPlaceChoice
import io.deepsense.deeplang.inference.InferContext
import io.deepsense.deeplang.params.selections.MultipleColumnSelection
import io.deepsense.deeplang.params.selections.NameColumnSelection
import io.deepsense.deeplang.params.selections.NameSingleColumnSelection
import io.deepsense.deeplang.params.NumericParam
import io.deepsense.deeplang.params.Param
import io.deepsense.deeplang.DKnowledge
import io.deepsense.deeplang.DeeplangIntegTestSupport
import io.deepsense.deeplang.ExecutionContext

class MultiColumnTransformerIntegSpec extends DeeplangIntegTestSupport {

  val magicConstant: Double = 1337d

  import DeeplangIntegTestSupport._

  "MultiColumnTransformer" should {
    "return also operation specific params in json" in {
      val t: AddAConstantTransformer = transformerWithMagicConstant
      t.params should contain(t.magicConstant)
    }
  }

  "MultiColumnTransformer" when {
    "working with multiple columns" when {
      val t = transformerWithMagicConstant
      t.setMultipleColumns(columns = Seq("x", "y"), inPlace = Some("magic_"))
      "in-place mode was not selected" should {
        "create columns with unique name (with prefix)" in {
          val transformedInPlace = t.transform(executionContext)(())(inputData)
          transformedInPlace.schema shouldBe expectedTransformedMulti.schema
          assertDataFramesEqual(transformedInPlace, expectedTransformedMulti)
        }
        "infer schema for columns with unique name (with prefix)" in {
          val (k, _) = t.transform
            .infer(mock[InferContext])(())(DKnowledge(DataFrame.forInference(inputSchema.get)))
          k.single shouldBe DataFrame.forInference(expectedTransformedMulti.schema.get)
        }
      }
      "in-place mode was selected" should {
        val t = transformerWithMagicConstant
        t.setMultipleColumns(columns = Seq("x", "y"), inPlace = None)
        "replace columns" in {
          val transformedInPlace = t.transform(executionContext)(())(inputData)
          transformedInPlace.schema shouldBe expectedMultiInPlace.schema
          assertDataFramesEqual(transformedInPlace, expectedMultiInPlace)
        }
        "replace columns schema" in {
          val (k, _) = t.transform
            .infer(mock[InferContext])(())(DKnowledge(DataFrame.forInference(inputSchema.get)))
          k.single shouldBe DataFrame.forInference(expectedMultiInPlace.schema.get)
        }
      }
    }
    "working with a single column" should {
      val t = transformerWithMagicConstant
      t.setSingleColumn(column = "y", inPlace = Some("updatedy"))
      "in-place mode was not selected" when {
        "create a new column" in {
          val transformedY = t.transform(executionContext)(())(inputData)
          transformedY.schema shouldBe expectedTransformedY.schema
          assertDataFramesEqual(transformedY, expectedTransformedY)
        }
        "infer schema for columns with unique name (with prefix)" in {
          val (k, _) = t.transform
            .infer(mock[InferContext])(())(DKnowledge(DataFrame.forInference(inputSchema.get)))
          k.single shouldBe DataFrame.forInference(expectedTransformedY.schema.get)
        }
      }
      "in-place mode was selected" when {
        val t = transformerWithMagicConstant
        t.setSingleColumn(column = "y", inPlace = None)
        "replace a column" in {
          val transformedY = t.transform(executionContext)(())(inputData)
          transformedY.schema shouldBe expectedTransformedYInPlace.schema
          assertDataFramesEqual(transformedY, expectedTransformedYInPlace)
        }
        "replace columns schema" in {
          val (k, _) = t.transform
            .infer(mock[InferContext])(())(DKnowledge(DataFrame.forInference(inputSchema.get)))
          k.single shouldBe DataFrame.forInference(expectedTransformedYInPlace.schema.get)
        }
      }
    }
  }

  def transformerWithMagicConstant: AddAConstantTransformer = {
    val t = AddAConstantTransformer()
    t.setMagicConstant(magicConstant)
    t
  }

  val rawInputData = Seq(
    InputData(3, "abc", 5, 23),
    InputData(14, "def", 5, 4),
    InputData(15, "ghi", 5, 89),
    InputData(29, "jkl", 5, 13)
  )

  val inputData = createDataFrame(rawInputData)

  val inputSchema = inputData.schema

  val expectedMultiInPlace = createDataFrame(
    rawInputData.map(d => InputDataDouble(d.x + magicConstant, d.a, d.y + magicConstant, d.z))
  )

  val expectedTransformedYInPlace = createDataFrame(
    rawInputData.map(d => InputDataDouble(d.x, d.a, d.y + magicConstant, d.z))
  )

  val expectedTransformedY = createDataFrame(
    rawInputData.map(d => InputDataUpdatedY(d.x, d.a, d.y, d.z, d.y + magicConstant))
  )

  val expectedTransformedMulti = createDataFrame(
    rawInputData.map(d => InputDataUpdatedMulti(d.x, d.a, d.y, d.z, d.x + magicConstant, d.y + magicConstant))
  )

}

object MultiColumnTransformerIntegSpec {

  case class InputData(x: Double, a: String, y: Int, z: Double)

  case class InputDataDouble(x: Double, a: String, y: Double, z: Double)

  case class InputDataUpdatedY(x: Double, a: String, y: Int, z: Double, updatedy: Double)

  case class InputDataUpdatedMulti(x: Double, a: String, y: Int, z: Double, magic_x: Double, magic_y: Double)

  case class AddAConstantTransformer() extends MultiColumnTransformer {

    val magicConstant = NumericParam(
      name = "aconstant",
      description = Some("Constant that will be added to columns")
    )

    def setMagicConstant(value: Double): this.type = set(magicConstant, value)

    override def getSpecificParams: Array[Param[_]] = Array(magicConstant)

    override def transformSingleColumn(
        inputColumn: String,
        outputColumn: String,
        context: ExecutionContext,
        dataFrame: DataFrame
    ): DataFrame = {

      transformSingleColumnSchema(inputColumn, outputColumn, dataFrame.sparkDataFrame.schema)
      val magicConstantValue = $(magicConstant)
      DataFrame.fromSparkDataFrame(
        dataFrame.sparkDataFrame
          .selectExpr("*", s"cast(`$inputColumn` as double) + $magicConstantValue as `$outputColumn`")
      )
    }

    override def transformSingleColumnSchema(
        inputColumn: String,
        outputColumn: String,
        schema: StructType
    ): Option[StructType] = {
      if (schema.fieldNames.contains(outputColumn))
        throw new IllegalArgumentException(s"Output column $outputColumn already exists.")
      val outputFields = schema.fields :+
        StructField(outputColumn, DoubleType, nullable = false)
      Some(StructType(outputFields))
    }

    def setSingleColumn(column: String, inPlace: Option[String]): this.type = {
      val inplaceChoice = inPlace match {
        case Some(x) => NoInPlaceChoice().setOutputColumn(x)
        case None    => YesInPlaceChoice()
      }

      val single = SingleColumnChoice()
        .setInputColumn(NameSingleColumnSelection(column))
        .setInPlace(inplaceChoice)

      setSingleOrMultiChoice(single)
    }

    def setMultipleColumns(columns: Seq[String], inPlace: Option[String]): this.type = {
      val inPlaceChoice = inPlace match {
        case Some(x) => MultiColumnNoInPlace().setColumnsPrefix(x)
        case None    => MultiColumnYesInPlace()
      }

      val columnSelection = NameColumnSelection(columns.toSet)
      val multiple = MultiColumnChoice()
        .setInputColumnsParam(MultipleColumnSelection(Vector(columnSelection)))
        .setMultiInPlaceChoice(inPlaceChoice)

      setSingleOrMultiChoice(multiple)
    }

  }

}
