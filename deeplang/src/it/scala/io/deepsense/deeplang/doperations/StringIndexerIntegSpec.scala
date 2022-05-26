package io.deepsense.deeplang.doperations

import org.apache.spark.sql.types._

import io.deepsense.deeplang.doperables.Transformer
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperables.multicolumn.MultiColumnParams.MultiColumnInPlaceChoices.MultiColumnNoInPlace
import io.deepsense.deeplang.doperables.multicolumn.MultiColumnParams.SingleOrMultiColumnChoices.MultiColumnChoice
import io.deepsense.deeplang.doperables.multicolumn.MultiColumnParams.SingleOrMultiColumnChoices.SingleColumnChoice
import io.deepsense.deeplang.doperables.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.NoInPlaceChoice
import io.deepsense.deeplang.doperables.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.YesInPlaceChoice
import io.deepsense.deeplang.doperables.spark.wrappers.models.MultiColumnStringIndexerModel
import io.deepsense.deeplang.doperables.spark.wrappers.models.SingleColumnStringIndexerModel
import io.deepsense.deeplang.doperations.spark.wrappers.estimators.StringIndexer
import io.deepsense.deeplang.exceptions.DeepLangException
import io.deepsense.deeplang.inference.InferenceWarnings
import io.deepsense.deeplang.params.selections.NameSingleColumnSelection
import io.deepsense.deeplang.DKnowledge
import io.deepsense.deeplang.DOperable
import io.deepsense.deeplang.DeeplangIntegTestSupport
import io.deepsense.deeplang.UnitSpec

class StringIndexerIntegSpec extends DeeplangIntegTestSupport {

  import StringIndexerIntegSpec._

  def validateSingleColumnParams(
      transformerKnowledge: DKnowledge[Transformer],
      inputColumn: Option[String],
      outputColumn: Option[String]
  ): Unit = {
    val t = validateSingleType[SingleColumnStringIndexerModel](
      transformerKnowledge.asInstanceOf[DKnowledge[SingleColumnStringIndexerModel]]
    )

    val choice         = singleColumnStringIndexerParams(inputColumn, outputColumn)
    val choiceParamMap = choice.extractParamMap()
    val paramMap       = t.extractParamMap()
    paramMap.get(t.inputColumn) shouldBe choiceParamMap.get(choice.inputColumn)
    paramMap.get(t.singleInPlaceChoice) shouldBe choiceParamMap.get(choice.singleInPlaceChoice)
  }

  def validateMultiColumnParams(
      transformerKnowledge: DKnowledge[Transformer],
      inputColumn: Option[String],
      outputColumn: Option[String]
  ): Unit = {
    val t = validateSingleType[MultiColumnStringIndexerModel](
      transformerKnowledge.asInstanceOf[DKnowledge[MultiColumnStringIndexerModel]]
    )

    val choice         = multiColumnStringIndexerParams(inputColumn, outputColumn)
    val choiceParamMap = choice.extractParamMap()
    val paramMap       = t.extractParamMap()
    paramMap.get(t.multiColumnChoice.inputColumnsParam) shouldBe
      choiceParamMap.get(choice.inputColumnsParam)
    paramMap.get(t.multiColumnChoice.multiInPlaceChoiceParam) shouldBe
      choiceParamMap.get(choice.multiInPlaceChoiceParam)
  }

  "StringIndexer" when {
    "schema is available" when {
      "parameters are set" when {
        "in single column mode" should {
          "infer SingleColumnStringIndexerModel with parameters" in {
            val in      = "c1"
            val out     = "out_c1"
            val indexer = singleColumnStringIndexer(Some(in), Some(out))
            val knowledge =
              indexer.inferKnowledgeUntyped(knownSchemaKnowledgeVector)(executionContext.inferContext)
            validate(knowledge) { case (dataFrameKnowledge, transformerKnowledge) =>
              val expectedSchema = StructType(
                Seq(
                  StructField("c1", StringType),
                  StructField("c2", DoubleType),
                  StructField("c3", StringType),
                  StructField("out_c1", DoubleType, nullable = false)
                )
              )

              validateSingleColumnParams(transformerKnowledge, Some(in), Some(out))
              validateSchemasEqual(dataFrameKnowledge, Some(expectedSchema))
              validateTransformerInference(knownSchemaKnowledge, transformerKnowledge, Some(expectedSchema))
            }
          }
        }
        "in multi column mode" should {
          "infer StringIndexerModel with parameters" in {
            val in      = "c1"
            val out     = "out_"
            val indexer = multiColumnStringIndexer(Some(in), Some(out))
            val knowledge =
              indexer.inferKnowledgeUntyped(knownSchemaKnowledgeVector)(executionContext.inferContext)
            validate(knowledge) { case (dataFrameKnowledge, transformerKnowledge) =>
              val expectedSchema = StructType(
                Seq(
                  StructField("c1", StringType),
                  StructField("c2", DoubleType),
                  StructField("c3", StringType),
                  StructField("out_c1", DoubleType, nullable = false)
                )
              )
              validateMultiColumnParams(transformerKnowledge, Some(in), Some(out))
              validateSchemasEqual(dataFrameKnowledge, Some(expectedSchema))
              validateTransformerInference(knownSchemaKnowledge, transformerKnowledge, Some(expectedSchema))
            }
          }
        }
      }
      "parameters are not set" when {
        "in single column mode" should {
          "infer SingleColumnStringIndexerModel without parameters" in pendingUntilFixed {
            val indexer = singleColumnStringIndexer(None, None)
            val knowledge =
              indexer.inferKnowledgeUntyped(knownSchemaKnowledgeVector)(executionContext.inferContext)
            validate(knowledge) { case (dataFrameKnowledge, transformerKnowledge) =>
              validateSingleColumnParams(transformerKnowledge, None, None)
              validateSchemasEqual(dataFrameKnowledge, None)
              validateTransformerInference(knownSchemaKnowledge, transformerKnowledge, None)
            }
          }
        }
        "in multi column mode" should {
          "infer StringIndexerModel without parameters" in pendingUntilFixed {
            val indexer = multiColumnStringIndexer(None, None)
            val knowledge =
              indexer.inferKnowledgeUntyped(knownSchemaKnowledgeVector)(executionContext.inferContext)
            validate(knowledge) { case (dataFrameKnowledge, transformerKnowledge) =>
              validateMultiColumnParams(transformerKnowledge, None, None)
              validateSchemasEqual(dataFrameKnowledge, None)
              validateTransformerInference(knownSchemaKnowledge, transformerKnowledge, None)
            }
          }
        }
      }
    }
    "schema is unavailable" when {
      "parameters are set" when {
        "in single column mode" should {
          "infer SingleColumnStringIndexerModel with parameters" in {
            val in      = "c1"
            val out     = "out_c1"
            val indexer = singleColumnStringIndexer(Some(in), Some(out))
            val knowledge =
              indexer.inferKnowledgeUntyped(unknownSchemaKnowledgeVector)(executionContext.inferContext)
            validate(knowledge) { case (dataFrameKnowledge, transformerKnowledge) =>
              validateSingleColumnParams(transformerKnowledge, Some(in), Some(out))
              validateSchemasEqual(dataFrameKnowledge, None)
              validateTransformerInference(unknownSchemaKnowledge, transformerKnowledge, None)
            }
          }
        }
        "in multi column mode" should {
          "infer StringIndexerModel with parameters" in {
            val in      = "c1"
            val out     = "out_"
            val indexer = multiColumnStringIndexer(Some(in), Some(out))
            val knowledge =
              indexer.inferKnowledgeUntyped(unknownSchemaKnowledgeVector)(executionContext.inferContext)
            validate(knowledge) { case (dataFrameKnowledge, transformerKnowledge) =>
              validateMultiColumnParams(transformerKnowledge, Some(in), Some(out))
              validateSchemasEqual(dataFrameKnowledge, None)
              validateTransformerInference(unknownSchemaKnowledge, transformerKnowledge, None)
            }
          }
        }
      }
      "parameters are not set" when {
        "in single column mode" should {
          "throw DeepLangException" in {
            val indexer = singleColumnStringIndexer(None, None)
            a[DeepLangException] shouldBe thrownBy(
              indexer.inferKnowledgeUntyped(unknownSchemaKnowledgeVector)(executionContext.inferContext)
            )
          }
        }
        "in multi column mode" should {
          "throw DeepLangException" in {
            val indexer = multiColumnStringIndexer(None, None)
            a[DeepLangException] shouldBe thrownBy(
              indexer.inferKnowledgeUntyped(unknownSchemaKnowledgeVector)(executionContext.inferContext)
            )
          }
        }
      }
    }
  }

  def validateTransformerInference(
      dataFrameKnowledge: DKnowledge[DataFrame],
      transformerKnowledge: DKnowledge[Transformer],
      expectedSchema: Option[StructType]
  ): Unit = {
    val transformer = validateSingleType(transformerKnowledge)
    val knowledge =
      transformer.transform.infer(executionContext.inferContext)(())(dataFrameKnowledge)
    validateSchemaEqual(validateSingleType[DataFrame](knowledge._1).schema, expectedSchema)
  }

}

object StringIndexerIntegSpec extends UnitSpec {

  def validateSchemaEqual(actual: Option[StructType], expected: Option[StructType]): Unit =
    actual.map(_.map(_.copy(metadata = Metadata.empty))) shouldBe expected

  def validateSchemasEqual(dKnowledge: DKnowledge[DataFrame], expectedSchema: Option[StructType]): Unit =
    validateSchemaEqual(validateSingleType(dKnowledge).schema, expectedSchema)

  def multiColumnStringIndexerParams(inputColumn: Option[String], outputPrefix: Option[String]): MultiColumnChoice = {
    val choice = MultiColumnChoice().setMultiInPlaceChoice(MultiColumnNoInPlace())
    inputColumn.foreach { case ic =>
      choice.setInputColumnsParam(Set(ic))
    }
    outputPrefix.foreach { case prefix =>
      choice.setMultiInPlaceChoice(MultiColumnNoInPlace().setColumnsPrefix(prefix))
    }
    choice
  }

  def multiColumnStringIndexer(inputColumn: Option[String], outputPrefix: Option[String]): StringIndexer = {
    val operation = new StringIndexer()
    val choice    = multiColumnStringIndexerParams(inputColumn, outputPrefix)
    operation.estimator.set(operation.estimator.singleOrMultiChoiceParam -> choice)
    operation.set(operation.estimator.extractParamMap())
  }

  def singleColumnStringIndexerParams(inputColumn: Option[String], outputColumn: Option[String]): SingleColumnChoice = {
    val choice = SingleColumnChoice().setInPlace(NoInPlaceChoice())

    inputColumn.foreach { case ic =>
      choice.setInputColumn(NameSingleColumnSelection(ic))
    }

    outputColumn.foreach { case oc =>
      choice.setInPlace(NoInPlaceChoice().setOutputColumn(oc))
    }
    choice
  }

  def singleColumnStringIndexer(inputColumn: Option[String], outputColumn: Option[String]): StringIndexer = {
    val operation = new StringIndexer()
    val choice    = singleColumnStringIndexerParams(inputColumn, outputColumn)
    operation.estimator.set(operation.estimator.singleOrMultiChoiceParam -> choice)
    operation.set(operation.estimator.extractParamMap())
  }

  def singleColumnStringIndexerInPlace(inputColumn: Option[String]): StringIndexer = {
    val operation = new StringIndexer()
    val choice    = SingleColumnChoice().setInPlace(YesInPlaceChoice())
    inputColumn.foreach { case ic =>
      choice.setInputColumn(NameSingleColumnSelection(ic))
    }
    operation.estimator.set(operation.estimator.singleOrMultiChoiceParam -> choice)
    operation.set(operation.estimator.extractParamMap())
  }

  def validateSingleType[T <: DOperable](knowledge: DKnowledge[T]): T = {
    knowledge should have size 1
    knowledge.single
  }

  def validate(
      knowledge: (Vector[DKnowledge[DOperable]], InferenceWarnings)
  )(f: (DKnowledge[DataFrame], DKnowledge[Transformer]) => Unit): Unit = {
    val dfKnowledge    = knowledge._1(0).asInstanceOf[DKnowledge[DataFrame]]
    val modelKnowledge = knowledge._1(1).asInstanceOf[DKnowledge[Transformer]]
    f(dfKnowledge, modelKnowledge)
  }

  def dataframeKnowledge(schema: Option[StructType]): DKnowledge[DataFrame] =
    DKnowledge(Set[DataFrame](DataFrame.forInference(schema)))

  val schema = StructType(
    Seq(StructField("c1", StringType), StructField("c2", DoubleType), StructField("c3", StringType))
  )

  val knownSchemaKnowledge = dataframeKnowledge(Some(schema))

  val unknownSchemaKnowledge = dataframeKnowledge(None)

  val knownSchemaKnowledgeVector =
    Vector(knownSchemaKnowledge.asInstanceOf[DKnowledge[DOperable]])

  val unknownSchemaKnowledgeVector =
    Vector(unknownSchemaKnowledge.asInstanceOf[DKnowledge[DOperable]])

}
