package io.deepsense.deeplang.doperables.spark.wrappers.estimators

import org.apache.spark.ml.recommendation.{ALS => SparkALS, ALSModel => SparkALSModel}
import org.apache.spark.sql.types.StructType

import io.deepsense.commons.types.ColumnType
import io.deepsense.deeplang.doperables.SparkEstimatorWrapper
import io.deepsense.deeplang.doperables.dataframe.DataFrameColumnsGetter
import io.deepsense.deeplang.doperables.spark.wrappers.models.ALSModel
import io.deepsense.deeplang.doperables.spark.wrappers.params.common._
import io.deepsense.deeplang.params.Param
import io.deepsense.deeplang.params.selections.NameSingleColumnSelection
import io.deepsense.deeplang.params.validators.RangeValidator
import io.deepsense.deeplang.params.wrappers.spark._

class ALS
  extends SparkEstimatorWrapper[SparkALSModel, SparkALS, ALSModel]
  with HasItemColumnParam
  with HasPredictionColumnCreatorParam
  with HasUserColumnParam
  with HasMaxIterationsParam
  with HasSeedParam
  with HasRegularizationParam
  with HasCheckpointIntervalParam {

  val alpha = new DoubleParamWrapper[SparkALS](
    name = "alpha",
    description = Some("The alpha parameter in the implicit preference formulation."),
    sparkParamGetter = _.alpha,
    validator = RangeValidator(0.0, Double.PositiveInfinity))
  setDefault(alpha, 1.0)

  val implicitPrefs = new BooleanParamWrapper[SparkALS](
    name = "implicit prefs",
    description = Some("Whether to use implicit preference."),
    sparkParamGetter = _.implicitPrefs)
  setDefault(implicitPrefs, false)

  val nonnegative = new BooleanParamWrapper[SparkALS](
    name = "nonnegative",
    description = Some("Whether to apply nonnegativity constraints for least squares."),
    sparkParamGetter = _.nonnegative)
  setDefault(nonnegative, true)

  val numItemBlocks = new IntParamWrapper[SparkALS](
    name = "num item blocks",
    description = Some("The number of item blocks."),
    sparkParamGetter = _.numItemBlocks,
    validator = RangeValidator(begin = 1.0, end = Int.MaxValue, step = Some(1.0)))
  setDefault(numItemBlocks, 10.0)

  val numUserBlocks = new IntParamWrapper[SparkALS](
    name = "num user blocks",
    description = Some("The number of user blocks."),
    sparkParamGetter = _.numUserBlocks,
    validator = RangeValidator(begin = 1.0, end = Int.MaxValue, step = Some(1.0)))
  setDefault(numUserBlocks, 10.0)

  val rank = new IntParamWrapper[SparkALS](
    name = "rank",
    description = Some("The rank of the matrix factorization."),
    sparkParamGetter = _.rank,
    validator = RangeValidator(begin = 1.0, end = Int.MaxValue, step = Some(1.0)))
  setDefault(rank, 10.0)

  val ratingColumn = new SingleColumnSelectorParamWrapper[SparkALS](
    name = "rating column",
    description = Some("The column for ratings."),
    sparkParamGetter = _.ratingCol,
    portIndex = 0)
  setDefault(ratingColumn, NameSingleColumnSelection("rating"))

  override val params: Array[Param[_]] = Array(
    alpha,
    checkpointInterval,
    implicitPrefs,
    maxIterations,
    nonnegative,
    numItemBlocks,
    numUserBlocks,
    rank,
    ratingColumn,
    regularizationParam,
    seed,
    itemColumn,
    predictionColumn,
    userColumn)

  override private[deeplang] def _fit_infer(maybeSchema: Option[StructType]): ALSModel = {
    maybeSchema.map {
      schema =>
        DataFrameColumnsGetter.assertExpectedColumnType(schema, $(itemColumn), ColumnType.numeric)
        DataFrameColumnsGetter.assertExpectedColumnType(schema, $(userColumn), ColumnType.numeric)
        DataFrameColumnsGetter.assertExpectedColumnType(schema, $(ratingColumn), ColumnType.numeric)
    }
    super._fit_infer(maybeSchema)
  }
}
