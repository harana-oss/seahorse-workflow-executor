package io.deepsense.deeplang.doperables

import scala.language.reflectiveCalls

import org.apache.spark.ml
import org.apache.spark.ml.param.{Params, ParamMap => SparkParamMap}
import org.apache.spark.sql.types.StructType

import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.doperables.dataframe.{DataFrame, DataFrameColumnsGetter}
import io.deepsense.deeplang.doperables.multicolumn.SingleColumnParams.SingleColumnInPlaceChoice
import io.deepsense.deeplang.doperables.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.{NoInPlaceChoice, YesInPlaceChoice}
import io.deepsense.deeplang.doperables.multicolumn._
import io.deepsense.deeplang.doperables.spark.wrappers.params.common.{HasInputColumn, HasOutputColumn}
import io.deepsense.deeplang.params.Param
import io.deepsense.deeplang.params.wrappers.spark.ParamsWithSparkWrappers

abstract class SparkSingleColumnModelWrapper[
    MD <: ml.Model[MD]{ val outputCol: ml.param.Param[String]},
    E <: ml.Estimator[MD]{ val outputCol: ml.param.Param[String]}]
  extends SparkModelWrapper[MD, E]
  with ParamsWithSparkWrappers
  with HasInputColumn
  with HasSingleInPlaceParam
  with HasSpecificParams {

  def convertInputNumericToVector: Boolean = false
  def convertOutputVectorToDouble: Boolean = false

  private var outputColumnValue: Option[String] = None

  override lazy val params: Array[Param[_]] =
    Array(inputColumn, singleInPlaceChoice) ++ getSpecificParams

  override private[deeplang] def _transform(ctx: ExecutionContext, df: DataFrame): DataFrame = {
    val schema = df.schema.get
    val inputColumnName = DataFrameColumnsGetter.getColumnName(schema, $(inputColumn))
    val conversionDoubleToVectorIsNecessary = convertInputNumericToVector &&
      NumericToVectorUtils.isColumnNumeric(schema, inputColumnName)
    val convertedDataFrame = if (conversionDoubleToVectorIsNecessary) {
      // Automatically convert numeric input column to one-element vector column
      DataFrame.fromSparkDataFrame(NumericToVectorUtils.convertDataFrame(df, inputColumnName, ctx))
    } else {
      df
    }

    val transformedDataFrame = $(singleInPlaceChoice) match {
      case YesInPlaceChoice() =>
        SingleColumnTransformerUtils.transformSingleColumnInPlace(
          convertedDataFrame.getColumnName($(inputColumn)),
          convertedDataFrame,
          ctx,
          transformTo(ctx, convertedDataFrame))
      case no: NoInPlaceChoice =>
        transformTo(ctx, convertedDataFrame)(no.getOutputColumn)
    }

    if(conversionDoubleToVectorIsNecessary && convertOutputVectorToDouble) {
      val expectedSchema = _transformSchema(schema)
      val revertedTransformedDf =
        NumericToVectorUtils.revertDataFrame(
          transformedDataFrame.sparkDataFrame,
          expectedSchema.get,
          inputColumnName,
          getOutputColumnName(inputColumnName),
          ctx,
          convertOutputVectorToDouble)
      DataFrame.fromSparkDataFrame(revertedTransformedDf)
    } else {
      transformedDataFrame
    }
  }

  override private[deeplang] def _transformSchema(schema: StructType): Option[StructType] = {
    val inputColumnName = DataFrameColumnsGetter.getColumnName(schema, $(inputColumn))
    val conversionDoubleToVectorIsNecessary = convertInputNumericToVector &&
      NumericToVectorUtils.isColumnNumeric(schema, inputColumnName)
    val convertedSchema = if (conversionDoubleToVectorIsNecessary) {
      // Automatically convert numeric input column to one-element vector column
      NumericToVectorUtils.convertSchema(schema, inputColumnName)
    } else {
      schema
    }

    val transformedSchemaOption = $(singleInPlaceChoice) match {
      case YesInPlaceChoice() =>
        val temporaryColumnName =
          DataFrameColumnsGetter.uniqueSuffixedColumnName(inputColumnName)
        val temporarySchema: Option[StructType] =
          transformSchemaTo(convertedSchema, temporaryColumnName)

        temporarySchema.map { schema =>
          StructType(schema.collect {
            case field if field.name == inputColumnName =>
              schema(temporaryColumnName).copy(name = inputColumnName)
            case field if field.name != temporaryColumnName =>
              field
          })
        }
      case no: NoInPlaceChoice =>
        transformSchemaTo(convertedSchema, no.getOutputColumn)
    }

    if(conversionDoubleToVectorIsNecessary && convertOutputVectorToDouble) {
      transformedSchemaOption.map { case transformedSchema =>
        NumericToVectorUtils.revertSchema(
          transformedSchema,
          inputColumnName,
          getOutputColumnName(inputColumnName),
          convertOutputVectorToDouble)
      }
    } else {
      transformedSchemaOption
    }
  }

  override def sparkParamMap(sparkEntity: Params, schema: StructType): SparkParamMap = {
    val map = super.sparkParamMap(sparkEntity, schema).put(
      ml.param.ParamPair(
        parentEstimator.sparkEstimator.outputCol, outputColumnValue.orNull))

    if (serializableModel != null) {
      map.put(ml.param.ParamPair(sparkModel.outputCol, outputColumnValue.orNull))
    } else {
      map
    }
  }

  def setSingleInPlaceParam(value: SingleColumnInPlaceChoice): this.type = {
    set(singleInPlaceChoice -> value)
  }

  private def transformTo(
    ctx: ExecutionContext,
    df: DataFrame)(outputColumnName: String): DataFrame = {
    withOutputColumnValue(outputColumnName) {
      super._transform(ctx, df)
    }
  }

  private def transformSchemaTo(
      schema: StructType,
      temporaryColumnName: String): Option[StructType] = {
    withOutputColumnValue(temporaryColumnName) {
      super._transformSchema(schema)
    }
  }

  private def withOutputColumnValue[T](columnName: String)(f: => T): T = {
    outputColumnValue = Some(columnName)
    try {
      f
    } finally {
      outputColumnValue = None
    }
  }

  private def getOutputColumnName(inputColumnName: String): String = {
    $(singleInPlaceChoice) match {
      case YesInPlaceChoice() => inputColumnName
      case no: NoInPlaceChoice => no.getOutputColumn
    }
  }

  override def replicate(
      extra: io.deepsense.deeplang.params.ParamMap): SparkSingleColumnModelWrapper.this.type = {
    val model = super.replicate(extractParamMap(extra))
    model.outputColumnValue = outputColumnValue
    model
  }
}
