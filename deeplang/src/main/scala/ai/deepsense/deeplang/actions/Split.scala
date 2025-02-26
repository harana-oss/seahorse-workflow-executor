package ai.deepsense.deeplang.actions

import scala.concurrent.duration.Duration
import scala.concurrent.Await
import scala.concurrent.Future
import scala.reflect.runtime.{universe => ru}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Row

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.PortPosition._
import ai.deepsense.deeplang._
import ai.deepsense.deeplang.documentation.OperationDocumentation
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.HasSeedParam
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.parameters.StorageType.{apply => _}
import ai.deepsense.deeplang.parameters._
import ai.deepsense.deeplang.parameters.choice.Choice
import ai.deepsense.deeplang.parameters.choice.ChoiceParameter
import ai.deepsense.deeplang.parameters.validators.RangeValidator
import ai.deepsense.sparkutils.SQL

case class Split() extends Action1To2[DataFrame, DataFrame, DataFrame] with Params with OperationDocumentation {

  override val id: Action.Id = "d273c42f-b840-4402-ba6b-18282cc68de3"

  override val name: String = "Split"

  override val description: String =
    "Splits a DataFrame into two DataFrames"

  override val since: Version = Version(0, 4, 0)

  val splitMode = ChoiceParameter[SplitModeChoice](
    name = "split mode",
    description = Some("""There are two split modes:
                         |`RANDOM` where rows are split randomly with specified `ratio` and `seed`;
                         |`CONDITIONAL` where rows are split into two `DataFrames` -
                         |satisfying an SQL `condition` and not satisfying it.
                         |""".stripMargin)
  )

  setDefault(splitMode, SplitModeChoice.Random())

  def getSplitMode: SplitModeChoice = $(splitMode)

  def setSplitMode(value: SplitModeChoice): this.type = set(splitMode, value)

  val specificParams: Array[ai.deepsense.deeplang.parameters.Parameter[_]] = Array(splitMode)

  override def outPortsLayout: Vector[PortPosition] =
    Vector(PortPosition.Left, PortPosition.Right)

  override protected def execute(df: DataFrame)(context: ExecutionContext): (DataFrame, DataFrame) = {
    implicit val inputDataFrame   = df
    implicit val executionContext = context

    getSplitMode match {
      case randomChoice: SplitModeChoice.Random           =>
        executeRandomSplit(randomChoice)
      case conditionalChoice: SplitModeChoice.Conditional =>
        executeConditionalSplit(conditionalChoice)
    }
  }

  private def executeRandomSplit(
      randomChoice: SplitModeChoice.Random
  )(implicit context: ExecutionContext, df: DataFrame): (DataFrame, DataFrame) = {
    val Array(f1: RDD[Row], f2: RDD[Row]) =
      randomSplit(df, randomChoice.getSplitRatio, randomChoice.getSeed)
    val schema                            = df.sparkDataFrame.schema
    val dataFrame1                        = context.dataFrameBuilder.buildDataFrame(schema, f1)
    val dataFrame2                        = context.dataFrameBuilder.buildDataFrame(schema, f2)
    (dataFrame1, dataFrame2)
  }

  private def randomSplit(df: DataFrame, range: Double, seed: Long): Array[RDD[Row]] =
    df.sparkDataFrame.rdd.randomSplit(Array(range, 1.0 - range), seed)

  private def executeConditionalSplit(
      conditionalChoice: SplitModeChoice.Conditional
  )(implicit context: ExecutionContext, df: DataFrame): (DataFrame, DataFrame) = {

    import scala.concurrent.ExecutionContext.Implicits.global

    val condition = conditionalChoice.getCondition

    val inputDataFrameId =
      "split_conditional_" + java.util.UUID.randomUUID.toString.replace('-', '_')

    SQL.registerTempTable(df.sparkDataFrame, inputDataFrameId)
    logger.debug(s"Table '$inputDataFrameId' registered. Executing the expression")

    val selectFromExpression = s"SELECT * FROM $inputDataFrameId"

    // TODO Should we evaluate condition to additional column and then just select it for both dfs?
    lazy val (leftExpression, rightExpression) =
      (s"$selectFromExpression WHERE $condition", s"$selectFromExpression WHERE not ($condition)")

    def runExpression(expression: String): DataFrame = {
      val sqlResult = SQL.sparkSQLSession(df.sparkDataFrame).sql(expression)
      DataFrame.fromSparkDataFrame(sqlResult)
    }

    val results =
      Future.sequence(Seq(Future(runExpression(leftExpression)), Future(runExpression(rightExpression)))).map {
        case Seq(leftDataFrame, rightDataFrame) => (leftDataFrame, rightDataFrame)
      }

    results.onComplete { _ =>
      logger.debug(s"Unregistering the temporary table '$inputDataFrameId'")
      SQL.sparkSQLSession(df.sparkDataFrame).dropTempTable(inputDataFrameId)
    }

    Await.result(results, Duration.Inf)
  }

  override protected def inferKnowledge(knowledge: Knowledge[DataFrame])(
      context: InferContext
  ): ((Knowledge[DataFrame], Knowledge[DataFrame]), InferenceWarnings) =
    ((knowledge, knowledge), InferenceWarnings.empty)

  @transient
  override lazy val tTagTI_0: ru.TypeTag[DataFrame] = ru.typeTag[DataFrame]

  @transient
  override lazy val tTagTO_0: ru.TypeTag[DataFrame] = ru.typeTag[DataFrame]

  @transient
  override lazy val tTagTO_1: ru.TypeTag[DataFrame] = ru.typeTag[DataFrame]

}

sealed trait SplitModeChoice extends Choice {

  import SplitModeChoice._

  override val choiceOrder: List[Class[_ <: SplitModeChoice]] = List(classOf[Random], classOf[Conditional])

}

object SplitModeChoice {

  case class Random() extends SplitModeChoice with HasSeedParam {

    override val name: String = "RANDOM"

    val splitRatio = NumericParameter(
      name = "split ratio",
      description = Some("Percentage of rows that should end up in the first output DataFrame."),
      validator = RangeValidator(0.0, 1.0, beginIncluded = true, endIncluded = true)
    )

    setDefault(splitRatio, 0.5)

    def getSplitRatio: Double = $(splitRatio)

    def setSplitRatio(value: Double): this.type = set(splitRatio, value)

    def getSeed: Int = $(seed).toInt

    def setSeed(value: Int): this.type = set(seed, value.toDouble)

    override val params: Array[ai.deepsense.deeplang.parameters.Parameter[_]] = Array(splitRatio, seed)

  }

  case class Conditional() extends SplitModeChoice {

    override val name: String = "CONDITIONAL"

    val condition = CodeSnippetParameter(
      name = "condition",
      description = Some("""Condition used to split rows.
                           |Rows that satisfy condition will be placed in the first DataFrame
                           |and rows that do not satisfy it - in the second.
                           |Use SQL syntax.""".stripMargin),
      language = CodeSnippetLanguage(CodeSnippetLanguage.sql)
    )

    def getCondition: String = $(condition)

    def setCondition(value: String): this.type = set(condition, value)

    override val params: Array[ai.deepsense.deeplang.parameters.Parameter[_]] = Array(condition)

  }

}
