package ai.deepsense.deeplang.actions

import org.apache.spark.sql.types._

import ai.deepsense.deeplang.actionobjects.Transformer
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actionobjects.multicolumn.MultiColumnParams.MultiColumnInPlaceChoices.MultiColumnNoInPlace
import ai.deepsense.deeplang.actionobjects.multicolumn.MultiColumnParams.SingleOrMultiColumnChoices.MultiColumnChoice
import ai.deepsense.deeplang.actionobjects.multicolumn.MultiColumnParams.SingleOrMultiColumnChoices.SingleColumnChoice
import ai.deepsense.deeplang.actionobjects.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.NoInPlaceChoice
import ai.deepsense.deeplang.actionobjects.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.YesInPlaceChoice
import ai.deepsense.deeplang.actionobjects.spark.wrappers.models.MultiColumnStringIndexerModel
import ai.deepsense.deeplang.actionobjects.spark.wrappers.models.SingleColumnStringIndexerModel
import ai.deepsense.deeplang.actions.spark.wrappers.estimators.StringIndexer
import ai.deepsense.deeplang.exceptions.DeepLangException
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.parameters.selections.NameSingleColumnSelection
import ai.deepsense.deeplang.Knowledge
import ai.deepsense.deeplang.ActionObject
import ai.deepsense.deeplang.DeeplangIntegTestSupport
import ai.deepsense.deeplang.UnitSpec

class StringIndexerIntegSpec extends DeeplangIntegTestSupport {

  import StringIndexerIntegSpec._

  def validateSingleColumnParams(
                                  transformerKnowledge: Knowledge[Transformer],
                                  inputColumn: Option[String],
                                  outputColumn: Option[String]
  ): Unit = {
    val t = validateSingleType[SingleColumnStringIndexerModel](
      transformerKnowledge.asInstanceOf[Knowledge[SingleColumnStringIndexerModel]]
    )

    val choice         = singleColumnStringIndexerParams(inputColumn, outputColumn)
    val choiceParamMap = choice.extractParamMap()
    val paramMap       = t.extractParamMap()
    paramMap.get(t.inputColumn) shouldBe choiceParamMap.get(choice.inputColumn)
    paramMap.get(t.singleInPlaceChoice) shouldBe choiceParamMap.get(choice.singleInPlaceChoice)
  }

  def validateMultiColumnParams(
                                 transformerKnowledge: Knowledge[Transformer],
                                 inputColumn: Option[String],
                                 outputColumn: Option[String]
  ): Unit = {
    val t = validateSingleType[MultiColumnStringIndexerModel](
      transformerKnowledge.asInstanceOf[Knowledge[MultiColumnStringIndexerModel]]
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
            val in        = "c1"
            val out       = "out_c1"
            val indexer   = singleColumnStringIndexer(Some(in), Some(out))
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
            val in        = "c1"
            val out       = "out_"
            val indexer   = multiColumnStringIndexer(Some(in), Some(out))
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
            val indexer   = singleColumnStringIndexer(None, None)
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
            val indexer   = multiColumnStringIndexer(None, None)
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
            val in        = "c1"
            val out       = "out_c1"
            val indexer   = singleColumnStringIndexer(Some(in), Some(out))
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
            val in        = "c1"
            val out       = "out_"
            val indexer   = multiColumnStringIndexer(Some(in), Some(out))
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
                                    dataFrameKnowledge: Knowledge[DataFrame],
                                    transformerKnowledge: Knowledge[Transformer],
                                    expectedSchema: Option[StructType]
  ): Unit = {
    val transformer = validateSingleType(transformerKnowledge)
    val knowledge   =
      transformer.transform.infer(executionContext.inferContext)(())(dataFrameKnowledge)
    validateSchemaEqual(validateSingleType[DataFrame](knowledge._1).schema, expectedSchema)
  }

}

object StringIndexerIntegSpec extends UnitSpec {

  def validateSchemaEqual(actual: Option[StructType], expected: Option[StructType]): Unit =
    actual.map(_.map(_.copy(metadata = Metadata.empty))) shouldBe expected

  def validateSchemasEqual(dKnowledge: Knowledge[DataFrame], expectedSchema: Option[StructType]): Unit =
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

  def validateSingleType[T <: ActionObject](knowledge: Knowledge[T]): T = {
    knowledge should have size 1
    knowledge.single
  }

  def validate(
      knowledge: (Vector[Knowledge[ActionObject]], InferenceWarnings)
  )(f: (Knowledge[DataFrame], Knowledge[Transformer]) => Unit): Unit = {
    val dfKnowledge    = knowledge._1(0).asInstanceOf[Knowledge[DataFrame]]
    val modelKnowledge = knowledge._1(1).asInstanceOf[Knowledge[Transformer]]
    f(dfKnowledge, modelKnowledge)
  }

  def dataframeKnowledge(schema: Option[StructType]): Knowledge[DataFrame] =
    Knowledge(Set[DataFrame](DataFrame.forInference(schema)))

  val schema = StructType(
    Seq(StructField("c1", StringType), StructField("c2", DoubleType), StructField("c3", StringType))
  )

  val knownSchemaKnowledge = dataframeKnowledge(Some(schema))

  val unknownSchemaKnowledge = dataframeKnowledge(None)

  val knownSchemaKnowledgeVector =
    Vector(knownSchemaKnowledge.asInstanceOf[Knowledge[ActionObject]])

  val unknownSchemaKnowledgeVector =
    Vector(unknownSchemaKnowledge.asInstanceOf[Knowledge[ActionObject]])

}
