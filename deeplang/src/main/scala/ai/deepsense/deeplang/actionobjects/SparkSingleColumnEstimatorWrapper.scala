package ai.deepsense.deeplang.actionobjects

import scala.reflect.runtime.universe.TypeTag

import org.apache.spark.ml
import org.apache.spark.sql.types.StructType

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrameColumnsGetter
import ai.deepsense.deeplang.actionobjects.multicolumn.SingleColumnParams.SingleColumnInPlaceChoice
import ai.deepsense.deeplang.actionobjects.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.NoInPlaceChoice
import ai.deepsense.deeplang.actionobjects.multicolumn.HasSingleInPlaceParam
import ai.deepsense.deeplang.actionobjects.multicolumn.HasSpecificParams
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.HasInputColumn
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.wrappers.spark.ParamsWithSparkWrappers

abstract class SparkSingleColumnEstimatorWrapper[MD <: ml.Model[
  MD
] { val outputCol: ml.param.Param[String] }, E <: ml.Estimator[
  MD
] { val outputCol: ml.param.Param[String] }, MW <: SparkSingleColumnModelWrapper[MD, E]](
    implicit override val modelWrapperTag: TypeTag[MW],
    implicit override val estimatorTag: TypeTag[E]
) extends SparkEstimatorWrapper[MD, E, MW]
    with ParamsWithSparkWrappers
    with HasInputColumn
    with HasSingleInPlaceParam
    with HasSpecificParams {

  override lazy val params: Array[Parameter[_]] =
    Array(inputColumn, singleInPlaceChoice) ++ getSpecificParams

  def setSingleInPlaceParam(value: SingleColumnInPlaceChoice): this.type =
    set(singleInPlaceChoice -> value)

  def setNoInPlace(outputColumn: String): this.type =
    setSingleInPlaceParam(NoInPlaceChoice().setOutputColumn(outputColumn))

  override private[deeplang] def _fit(ctx: ExecutionContext, df: DataFrame): MW = {
    val schema             = df.schema.get
    val inputColumnName    = DataFrameColumnsGetter.getColumnName(schema, $(inputColumn))
    val convertedDataFrame =
      if (
        convertInputNumericToVector
        && NumericToVectorUtils.isColumnNumeric(schema, inputColumnName)
      )
        // Automatically convert numeric input column to one-element vector column
        DataFrame.fromSparkDataFrame(NumericToVectorUtils.convertDataFrame(df, inputColumnName, ctx))
      else
        df
    super._fit(ctx, convertedDataFrame)
  }

  override private[deeplang] def _fit_infer(maybeSchema: Option[StructType]): MW = {
    maybeSchema match {
      case Some(schema) =>
        val inputColumnName = DataFrameColumnsGetter.getColumnName(schema, $(inputColumn))
        val convertedSchema =
          if (
            convertInputNumericToVector
            && NumericToVectorUtils.isColumnNumeric(schema, inputColumnName)
          )
            // Automatically convert numeric input column to one-element vector column
            NumericToVectorUtils.convertSchema(schema, inputColumnName)
          else
            schema
        super._fit_infer(Some(convertedSchema))
      case None         => super._fit_infer(None)
    }
  }

}
