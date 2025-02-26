package ai.deepsense.deeplang.actionobjects

import scala.reflect.runtime.universe.TypeTag

import org.apache.spark.ml
import org.apache.spark.sql.types.StructType

import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrameColumnsGetter
import ai.deepsense.deeplang.actionobjects.multicolumn.MultiColumnParams.MultiColumnInPlaceChoices.MultiColumnNoInPlace
import ai.deepsense.deeplang.actionobjects.multicolumn.MultiColumnParams.MultiColumnInPlaceChoices.MultiColumnYesInPlace
import ai.deepsense.deeplang.actionobjects.multicolumn.MultiColumnParams.SingleOrMultiColumnChoices.MultiColumnChoice
import ai.deepsense.deeplang.actionobjects.multicolumn.MultiColumnParams.SingleOrMultiColumnChoices.SingleColumnChoice
import ai.deepsense.deeplang.actionobjects.multicolumn.SingleColumnParams.SingleColumnInPlaceChoice
import ai.deepsense.deeplang.actionobjects.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.NoInPlaceChoice
import ai.deepsense.deeplang.actionobjects.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.YesInPlaceChoice
import ai.deepsense.deeplang.parameters.selections.NameSingleColumnSelection
import ai.deepsense.deeplang.parameters.wrappers.spark.ParamsWithSparkWrappers
import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.utils.TypeUtils

/** SparkMultiColumnEstimatorWrapper represents an estimator that is backed up by a Spark estimator. The wrapped
  * estimator (and it's model) must operate on a single column. SparkMultiColumnEstimatorWrapper allows to create
  * (basing on a Spark estimator) an estimator that is capable of working on both single columns and multiple columns.
  * Depending on the mode it returns different types of models (SingleColumnModel or MultiColumnModel). Both of the
  * returned models have to have a common ancestor ("the parent model").
 *
  * @param ev1
  *   Evidence that the single column model is a subclass of the parent model.
  * @param ev2
  *   Evidence that the multi column model is a subclass of the parent model.
  * @tparam MD
  *   Spark model used in Single- and MultiColumnModel.
  * @tparam E
  *   The wrapped Spark estimator.
  * @tparam MP
  *   A common ancestor of the single and multi column models produced by the SparkMultiColumnEstimatorWrapper.
  * @tparam SMW
  *   Type of the model returned when the estimator is working on a single column.
  * @tparam EW
  *   Type of the single column estimator.
  * @tparam MMW
  *   Type of the model returned when the estimator is working on multiple columns.
  */
abstract class SparkMultiColumnEstimatorWrapper[MD <: ml.Model[
  MD
] { val outputCol: ml.param.Param[String] }, E <: ml.Estimator[
  MD
] { val outputCol: ml.param.Param[String] }, MP <: Transformer, SMW <: SparkSingleColumnModelWrapper[
  MD,
  E
] with MP, EW <: SparkSingleColumnEstimatorWrapper[MD, E, SMW], MMW <: MultiColumnModel[MD, E, SMW] with MP](
    implicit val modelWrapperTag: TypeTag[SMW],
    implicit val estimatorTag: TypeTag[E],
    implicit val modelsParentTag: TypeTag[MP],
    implicit val estimatorWrapperTag: TypeTag[EW],
    implicit val multiColumnModelTag: TypeTag[MMW],
    implicit val ev1: SMW <:< MP,
    implicit val ev2: MMW <:< MP
) extends MultiColumnEstimator[MP, MMW, SMW]
    with ParamsWithSparkWrappers {

  val sparkEstimatorWrapper: EW = createEstimatorWrapperInstance()

  override def handleSingleColumnChoice(ctx: ExecutionContext, df: DataFrame, single: SingleColumnChoice): SMW = {

    val estimator = sparkEstimatorWrapper
      .replicate()
      .set(sparkEstimatorWrapper.inputColumn -> single.getInputColumn)
      .setSingleInPlaceParam(single.getInPlace)

    val mw = estimator._fit(ctx, df)
    mw.set(mw.inputColumn -> single.getInputColumn)
      .setSingleInPlaceParam(single.getInPlace)
  }

  override def handleMultiColumnChoice(ctx: ExecutionContext, df: DataFrame, multi: MultiColumnChoice): MMW = {
    val inputColumns         = df.getColumnNames(multi.getMultiInputColumnSelection)
    val multiToSingleDecoder = multiInPlaceToSingleInPlace(multi) _

    val models = inputColumns.map { case inputColumnName =>
      import sparkEstimatorWrapper._
      val estimator = sparkEstimatorWrapper
        .replicate()
        .set(inputColumn -> NameSingleColumnSelection(inputColumnName))
        .setSingleInPlaceParam(multiToSingleDecoder(inputColumnName))
      estimator
        ._fit(ctx, df)
        .setSingleInPlaceParam(multiToSingleDecoder(inputColumnName))
    }

    createMultiColumnModel()
      .setModels(models)
      .setInputColumns(multi.getMultiInputColumnSelection)
      .setInPlace(multi.getMultiInPlaceChoice)
  }

  override def handleSingleColumnChoiceInfer(schema: Option[StructType], single: SingleColumnChoice): SMW = {

    import sparkEstimatorWrapper._

    sparkEstimatorWrapper
      .replicate()
      .set(inputColumn -> single.getInputColumn)
      .setSingleInPlaceParam(single.getInPlace)
      ._fit_infer(schema)
      .setSingleInPlaceParam(single.getInPlace)
  }

  override def handleMultiColumnChoiceInfer(schema: Option[StructType], multi: MultiColumnChoice): MMW = {

    schema.map { s =>
      val inputColumns =
        DataFrameColumnsGetter.getColumnNames(s, multi.getMultiInputColumnSelection)

      val multiToSingleDecoder = multiInPlaceToSingleInPlace(multi) _

      val models = inputColumns.map { case inputColumnName =>
        import sparkEstimatorWrapper._
        sparkEstimatorWrapper
          .replicate()
          .set(inputColumn -> NameSingleColumnSelection(inputColumnName))
          .setSingleInPlaceParam(multiToSingleDecoder(inputColumnName))
          ._fit_infer(Some(s))
          .setSingleInPlaceParam(multiToSingleDecoder(inputColumnName))
      }

      createMultiColumnModel()
        .setModels(models)
        .setInputColumns(multi.getMultiInputColumnSelection)
        .setInPlace(multi.getMultiInPlaceChoice)
    }.getOrElse {
      val model = createMultiColumnModel()
        .setModels(Seq.empty)

      val inputColumnsParamValue = multi.getOrDefaultOption(multi.inputColumnsParam)
      val inPlaceParamValue      = multi.getOrDefaultOption(multi.multiInPlaceChoiceParam)

      inputColumnsParamValue.map(v => model.set(model.multiColumnChoice.inputColumnsParam -> v))
      inPlaceParamValue.map(v => model.set(model.multiColumnChoice.multiInPlaceChoiceParam -> v))

      model
    }

  }

  def createEstimatorWrapperInstance(): EW = TypeUtils.instanceOfType(estimatorWrapperTag)

  def createMultiColumnModel(): MMW = TypeUtils.instanceOfType(multiColumnModelTag)

  private def multiInPlaceToSingleInPlace(
      multi: MultiColumnChoice
  )(inputColumnName: String): SingleColumnInPlaceChoice = {
    multi.getMultiInPlaceChoice match {
      case MultiColumnYesInPlace()  => YesInPlaceChoice()
      case no: MultiColumnNoInPlace =>
        val outputColumnName =
          DataFrameColumnsGetter.prefixedColumnName(inputColumnName, no.getColumnsPrefix)
        NoInPlaceChoice().setOutputColumn(outputColumnName)
    }
  }

}
