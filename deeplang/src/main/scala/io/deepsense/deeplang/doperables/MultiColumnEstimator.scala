package io.deepsense.deeplang.doperables

import scala.language.reflectiveCalls
import scala.reflect.runtime.universe.TypeTag

import org.apache.spark.sql.types.StructType

import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperables.multicolumn.HasSpecificParams
import io.deepsense.deeplang.doperables.multicolumn.MultiColumnParams.MultiColumnInPlaceChoices.{MultiColumnNoInPlace, MultiColumnYesInPlace}
import io.deepsense.deeplang.doperables.multicolumn.MultiColumnParams.SingleOrMultiColumnChoices.{MultiColumnChoice, SingleColumnChoice}
import io.deepsense.deeplang.doperables.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.{NoInPlaceChoice, YesInPlaceChoice}
import io.deepsense.deeplang.doperables.spark.wrappers.params.common.HasInputColumn
import io.deepsense.deeplang.params.IOColumnsParam
import io.deepsense.deeplang.params.selections.NameSingleColumnSelection

/**
 * MultiColumnEstimator is a [[io.deepsense.deeplang.doperables.Estimator]]
 * that can work on either a single column or multiple columns.
 * Also, it can also work in-place (by replacing columns) or
 * not (new columns will be appended to a [[io.deepsense.deeplang.doperables.dataframe.DataFrame]]).
 *
 * @tparam T Parent type of the returned transformers.
 * @tparam MC The type of the returned transformer when working on multiple columns.
 * @tparam SC The type of the returned transformer when working on a single column.
 */
abstract class MultiColumnEstimator[T <: Transformer, MC <: T, SC <: T with HasInputColumn]
(implicit val transformerTypeTag: TypeTag[T])
  extends Estimator[T] with HasSpecificParams {

  val singleOrMultiChoiceParam = IOColumnsParam()
  override lazy val params = getSpecificParams :+ singleOrMultiChoiceParam

  def setSingleColumn(inputColumnName: String, outputColumnName: String): this.type = {
    val choice = SingleColumnChoice()
      .setInPlace(NoInPlaceChoice().setOutputColumn(outputColumnName))
      .setInputColumn(NameSingleColumnSelection(inputColumnName))
    set(singleOrMultiChoiceParam, choice)
  }

  def setSingleColumnInPlace(inputColumnName: String): this.type = {
    val choice = SingleColumnChoice()
      .setInPlace(YesInPlaceChoice())
      .setInputColumn(NameSingleColumnSelection(inputColumnName))
    set(singleOrMultiChoiceParam, choice)
  }

  def setMultipleColumn(inputColumnNames: Set[String], outputColumnPrefix: String): this.type = {
    val choice = MultiColumnChoice(inputColumnNames)
      .setMultiInPlaceChoice(MultiColumnNoInPlace().setColumnsPrefix(outputColumnPrefix))
    set(singleOrMultiChoiceParam, choice)
  }

  def setMultipleColumnInPlace(inputColumnNames: Set[String]): this.type = {
    val choice = MultiColumnChoice(inputColumnNames)
      .setMultiInPlaceChoice(MultiColumnYesInPlace())
    set(singleOrMultiChoiceParam, choice)
  }

  def handleSingleColumnChoice(
    ctx: ExecutionContext,
    df: DataFrame,
    single: SingleColumnChoice): SC

  def handleMultiColumnChoice(
    ctx: ExecutionContext,
    df: DataFrame,
    multi: MultiColumnChoice): MC

  /**
   * Creates a Transformer based on a DataFrame.
   */
  override private[deeplang] def _fit(ctx: ExecutionContext, df: DataFrame): T = {
    $(singleOrMultiChoiceParam) match {
      case single: SingleColumnChoice =>
        handleSingleColumnChoice(ctx, df, single)
      case multi: MultiColumnChoice =>
        handleMultiColumnChoice(ctx, df, multi)
    }
  }

  def handleSingleColumnChoiceInfer(
    schema: Option[StructType],
    single: SingleColumnChoice): SC

  def handleMultiColumnChoiceInfer(
    schema: Option[StructType],
    multi: MultiColumnChoice): MC

  /**
   * Creates an instance of Transformer for inference.
   * @param schema the schema for inference, or None if it's unknown.
   */
  override private[deeplang] def _fit_infer(
      schema: Option[StructType]): T = {
    $(singleOrMultiChoiceParam) match {
      case single: SingleColumnChoice =>
        handleSingleColumnChoiceInfer(schema, single)
      case multi: MultiColumnChoice =>
        handleMultiColumnChoiceInfer(schema, multi)
    }
  }
}





