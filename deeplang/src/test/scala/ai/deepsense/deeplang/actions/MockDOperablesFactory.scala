package ai.deepsense.deeplang.actions

import org.apache.spark.sql.types.StructType
import org.scalatest.matchers.should.Matchers
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers.any

import ai.deepsense.deeplang._
import ai.deepsense.deeplang.actionobjects._
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actionobjects.report.Report
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.parameters.validators.RangeValidator
import ai.deepsense.deeplang.parameters.NumericParameter
import ai.deepsense.deeplang.parameters.Parameter

object MockActionObjectsFactory extends UnitSpec with DeeplangTestSupport {

  val DefaultForA = 1

  def mockTransformer(df: DataFrame, dfk: Knowledge[DataFrame]): Transformer = {
    val t                                                  = mock[Transformer]
    val transform: DMethod1To1[Unit, DataFrame, DataFrame] =
      mock[DMethod1To1[Unit, DataFrame, DataFrame]]
    when(transform.apply(any())(any())(any())).thenReturn(df)
    when(transform.infer(any())(any())(any())).thenReturn((dfk, InferenceWarnings.empty))
    when(t.transform).thenReturn(transform)
    t
  }

  def dataFrameKnowledge(s: StructType): Knowledge[DataFrame] =
    Knowledge(Seq(DataFrame.forInference(s)))

  val transformedDataFrame1 = createDataFrame()

  val transformedDataFrame2 = createDataFrame()

  val transformedDataFrameSchema1 = transformedDataFrame1.schema.get

  val transformedDataFrameSchema2 = transformedDataFrame2.schema.get

  val transformedDataFrameKnowledge1 = dataFrameKnowledge(transformedDataFrameSchema1)

  val transformedDataFrameKnowledge2 = dataFrameKnowledge(transformedDataFrameSchema2)

  val transformer1 = mockTransformer(transformedDataFrame1, transformedDataFrameKnowledge1)

  when(transformer1._transformSchema(any(), any())).thenReturn(Some(transformedDataFrameSchema1))

  val transformer2 = mockTransformer(transformedDataFrame2, transformedDataFrameKnowledge2)

  when(transformer2._transformSchema(any(), any())).thenReturn(Some(transformedDataFrameSchema2))

  val transformerKnowledge1 = Knowledge(transformer1)

  val transformerKnowledge2 = Knowledge(transformer2)

  val metricValue1 = MetricValue("name1", 0.1)

  val metricValue2 = MetricValue("name2", 0.2)

  val metricValueKnowledge1 = Knowledge(MetricValue.forInference("name1"))

  val metricValueKnowledge2 = Knowledge(MetricValue.forInference("name2"))

  class MockEstimator extends Estimator[Transformer] {

    val paramA = NumericParameter("b", Some("desc"), RangeValidator(0.0, Double.MaxValue))

    setDefault(paramA -> DefaultForA)

    override val params: Array[Parameter[_]] = Array(paramA)

    override def report(extended: Boolean = true): Report = ???

    override def fit: DMethod1To1[Unit, DataFrame, Transformer] = {
      new DMethod1To1[Unit, DataFrame, Transformer] {
        override def apply(context: ExecutionContext)(parameters: Unit)(t0: DataFrame): Transformer = {
          $(paramA) match {
            case 1      => transformer1
            case -2 | 2 => transformer2
          }
        }

        override def infer(
            context: InferContext
        )(parameters: Unit)(k0: Knowledge[DataFrame]): (Knowledge[Transformer], InferenceWarnings) = {
          $(paramA) match {
            case 1      => (transformerKnowledge1, InferenceWarnings.empty)
            case -2 | 2 => (transformerKnowledge2, InferenceWarnings.empty)
          }
        }
      }
    }

    override private[deeplang] def _fit_infer(schema: Option[StructType]): Transformer =
      transformer1

    // Not used in tests.
    override private[deeplang] def _fit(ctx: ExecutionContext, df: DataFrame): Transformer = ???

  }

  class MockEvaluator extends Evaluator {

    val paramA = NumericParameter("b", Some("desc"), RangeValidator(0.0, Double.MaxValue))

    setDefault(paramA -> DefaultForA)

    override val params: Array[Parameter[_]] = Array(paramA)

    override def report(extended: Boolean = true): Report = ???

    override def evaluate: DMethod1To1[Unit, DataFrame, MetricValue] = {
      new DMethod1To1[Unit, DataFrame, MetricValue] {
        override def apply(ctx: ExecutionContext)(p: Unit)(dataFrame: DataFrame): MetricValue = {
          $(paramA) match {
            case 1 => metricValue1
            case 2 => metricValue2
          }
        }

        override def infer(
            ctx: InferContext
        )(p: Unit)(k: Knowledge[DataFrame]): (Knowledge[MetricValue], InferenceWarnings) = {
          $(paramA) match {
            case 1 => (metricValueKnowledge1, InferenceWarnings.empty)
            case 2 => (metricValueKnowledge2, InferenceWarnings.empty)
          }
        }
      }
    }

    override private[deeplang] def _infer(k: Knowledge[DataFrame]): MetricValue =
      MetricValue.forInference("name1")

    // Not used in tests.
    private[deeplang] def _evaluate(context: ExecutionContext, dataFrame: DataFrame): MetricValue =
      ???

    override def isLargerBetter: Boolean = ???

  }

}
