package ai.deepsense.deeplang.actionobjects

import org.apache.spark.sql.types.StructType

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrameColumnsGetter
import ai.deepsense.deeplang.actionobjects.multicolumn.MultiColumnParams.MultiColumnInPlaceChoices.MultiColumnNoInPlace
import ai.deepsense.deeplang.actionobjects.multicolumn.MultiColumnParams.MultiColumnInPlaceChoices.MultiColumnYesInPlace
import ai.deepsense.deeplang.actionobjects.multicolumn.MultiColumnParams.SingleOrMultiColumnChoices.MultiColumnChoice
import ai.deepsense.deeplang.actionobjects.multicolumn.MultiColumnParams.SingleOrMultiColumnChoices.SingleColumnChoice
import ai.deepsense.deeplang.actionobjects.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.NoInPlaceChoice
import ai.deepsense.deeplang.actionobjects.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.YesInPlaceChoice
import ai.deepsense.deeplang.actionobjects.multicolumn._
import ai.deepsense.deeplang.inference.exceptions.TransformSchemaException
import ai.deepsense.deeplang.parameters.IOColumnsParameter
import ai.deepsense.deeplang.parameters.selections.MultipleColumnSelection
import ai.deepsense.deeplang.parameters.selections.NameSingleColumnSelection

/** MultiColumnTransformer is a [[ai.deepsense.deeplang.actionobjects.Transformer]] that can work on either a single column
  * or multiple columns. Also, it can also work in-place (by replacing columns) or not (new columns will be appended to
  * a [[ai.deepsense.deeplang.actionobjects.dataframe.DataFrame]]). When not working in-place and when working with a
  * single column one has to specify output column's name. When working with multiple columns and in not in-place mode
  * one has to specify output column names' prefix.
  */
abstract class MultiColumnTransformer extends Transformer with HasSpecificParams {

  import MultiColumnParams._

  val singleOrMultiChoiceParam = IOColumnsParameter()

  setDefault(singleOrMultiChoiceParam, SingleColumnChoice())

  override lazy val params: Array[ai.deepsense.deeplang.parameters.Parameter[_]] =
    Array(getSpecificParams :+ singleOrMultiChoiceParam: _*)

  def setSingleOrMultiChoice(value: SingleOrMultiColumnChoice): this.type =
    set(singleOrMultiChoiceParam, value)

  def setSingleColumn(inputColumnName: String, outputColumnName: String): this.type = {
    val choice = SingleColumnChoice()
      .setInPlace(NoInPlaceChoice().setOutputColumn(outputColumnName))
      .setInputColumn(NameSingleColumnSelection(inputColumnName))
    set(singleOrMultiChoiceParam, choice)
  }

  /** Transforms 'inputColumn' and stores the results in 'outputColumn'. This method should throw an exception when the
    * outputColumn already exists (especially when outputColumn equals inputColumn). The has the same contract as
    * transform() Spark
    * [[http://spark.apache.org/docs/latest/api/scala/index.html#org.apache.spark.ml.UnaryTransformer]]'s one. If the
    * contract in UnaryTransformer change then this method will have to change, too. Eg. when UnaryTransformer cease to
    * throw on duplicated columns, the contract of this method should be changed accordingly.
    */
  def transformSingleColumn(
      inputColumn: String,
      outputColumn: String,
      context: ExecutionContext,
      dataFrame: DataFrame
  ): DataFrame

  /** Transforms schema to reflect changes that transformSingleColumn(..) would do (including throwing exceptions). */
  def transformSingleColumnSchema(inputColumn: String, outputColumn: String, schema: StructType): Option[StructType]

  def setSelectedColumns(value: MultipleColumnSelection): this.type = {
    val multiChoice = MultiColumnChoice()
      .setInputColumnsParam(value)
      .setMultiInPlaceChoice(MultiColumnYesInPlace())
    setSingleOrMultiChoice(multiChoice)
  }

  override protected def applyTransform(ctx: ExecutionContext, df: DataFrame): DataFrame = {
    $(singleOrMultiChoiceParam) match {
      case single: SingleColumnChoice =>
        handleSingleColumnChoice(ctx, df, single)
      case multi: MultiColumnChoice   =>
        handleMultiColumnChoice(ctx, df, multi)
    }
  }

  override protected def applyTransformSchema(schema: StructType): Option[StructType] = {
    $(singleOrMultiChoiceParam) match {
      case single: SingleColumnChoice =>
        handleSingleColumnChoiceSchema(schema, single)
      case multi: MultiColumnChoice   =>
        handleMultiColumnChoiceSchema(schema, multi)
    }
  }

  private def handleSingleColumnChoice(ctx: ExecutionContext, df: DataFrame, single: SingleColumnChoice): DataFrame = {
    val inputColumn = df.getColumnName(single.getInputColumn)
    single.getInPlace match {
      case no: NoInPlaceChoice =>
        transformSingleColumn(inputColumn, no.getOutputColumn, ctx, df)
      case YesInPlaceChoice()  =>
        transformSingleColumnInPlace(ctx, df, inputColumn)
    }
  }

  private def handleMultiColumnChoice(ctx: ExecutionContext, df: DataFrame, multi: MultiColumnChoice): DataFrame = {
    val inputColumnsSelection                   = multi.getMultiInputColumnSelection
    val inputColumns                            = df.getColumnNames(inputColumnsSelection)
    val inPlaceChoice: MultiColumnInPlaceChoice = multi.getMultiInPlaceChoice
    inPlaceChoice match {
      case MultiColumnYesInPlace()          =>
        inputColumns.foldLeft(df) { case (partialResult, inputColumn) =>
          transformSingleColumnInPlace(ctx, partialResult, inputColumn)
        }
      case newColumns: MultiColumnNoInPlace =>
        inputColumns.foldLeft(df) { case (partialResult, inputColumn) =>
          val outputColumn =
            DataFrameColumnsGetter.prefixedColumnName(inputColumn, newColumns.getColumnsPrefix)
          transformSingleColumn(inputColumn, outputColumn, ctx, partialResult)
        }
    }
  }

  private def handleMultiColumnChoiceSchema(schema: StructType, multi: MultiColumnChoice): Option[StructType] = {
    val inputColumns                            =
      DataFrameColumnsGetter.getColumnNames(schema, multi.getMultiInputColumnSelection)
    val inPlaceChoice: MultiColumnInPlaceChoice = multi.getMultiInPlaceChoice
    val someSchema: Option[StructType]          = Some(schema)
    inPlaceChoice match {
      case MultiColumnYesInPlace()  =>
        inputColumns.foldLeft(someSchema) { case (partialResult, inputColumn) =>
          partialResult.flatMap(r => transformSingleColumnSchemaInPlace(inputColumn, r))
        }
      case no: MultiColumnNoInPlace =>
        val columnPrefix = no.getColumnsPrefix
        inputColumns.foldLeft(someSchema) { case (partialResult, inputColumn) =>
          partialResult.flatMap { schema =>
            val outputColumn =
              DataFrameColumnsGetter.prefixedColumnName(inputColumn, columnPrefix)
            transformSingleColumnSchema(inputColumn, outputColumn, schema)
          }
        }
    }
  }

  private def handleSingleColumnChoiceSchema(schema: StructType, single: SingleColumnChoice): Option[StructType] = {
    val inputColumn = DataFrameColumnsGetter.getColumnName(schema, single.getInputColumn)
    single.getInPlace match {
      case no: NoInPlaceChoice =>
        transformSingleColumnSchema(inputColumn, no.getOutputColumn, schema)
      case YesInPlaceChoice()  =>
        transformSingleColumnSchemaInPlace(inputColumn, schema)
    }
  }

  private def transformSingleColumnInPlace(ctx: ExecutionContext, df: DataFrame, inputColumn: String): DataFrame = {

    SingleColumnTransformerUtils.transformSingleColumnInPlace(
      inputColumn,
      df,
      ctx,
      outputColumn => transformSingleColumn(inputColumn, outputColumn, ctx, df)
    )
  }

  private def transformSingleColumnSchemaInPlace(inputColumn: String, schema: StructType): Option[StructType] = {
    val temporaryColumnName =
      DataFrameColumnsGetter.uniqueSuffixedColumnName(inputColumn)
    val temporarySchema     =
      transformSingleColumnSchema(inputColumn, temporaryColumnName, schema)

    temporarySchema.map { schema =>
      StructType(schema.collect {
        case field if field.name == inputColumn         =>
          schema(temporaryColumnName).copy(name = inputColumn)
        case field if field.name != temporaryColumnName =>
          field
      })
    }
  }

}

object MultiColumnTransformer {

  def assertColumnDoesNotExist(outputColumn: String, schema: StructType): Unit =
    if (schema.fieldNames.contains(outputColumn))
      throw new TransformSchemaException(s"Output column '$outputColumn' already exists.")

  def assertColumnExist(inputColumn: String, schema: StructType): Unit =
    if (!schema.fieldNames.contains(inputColumn))
      throw new TransformSchemaException(s"Input column '$inputColumn' does not exist.")

}
