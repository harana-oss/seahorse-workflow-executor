package io.deepsense.deeplang.doperables

import org.apache.spark.sql.Row
import org.apache.spark.sql.types._

import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.sparkutils.Linalg.Vectors

/**
 * Provides helper methods for automatic conversion of double columns to vector columns.
 */
object NumericToVectorUtils {

  /**
   * Returns schema with modified datatype of specified column (by name).
   * Does not have side-effects.
   */
  private def updateSchema(schema: StructType, colName: String, dataType: DataType): StructType = {
    updateSchema(schema, schema.fieldIndex(colName), dataType)
  }

  /**
   * Returns schema with modified datatype of specified column (by index).
   * Does not have side-effects.
   */
  private def updateSchema(schema: StructType, idx: Int, dataType: DataType): StructType = {
    schema.copy(schema.fields.clone().updated(idx, schema(idx).copy(dataType = dataType)))
  }

  /**
   * Returns true if `colName` type is numeric, false otherwise
   */
  def isColumnNumeric(schema: StructType, colName: String): Boolean = {
    schema(colName).dataType.isInstanceOf[NumericType]
  }

  /**
   * Converts schema by changing `inputColumn` type to vector
   */
  def convertSchema(schema: StructType, inputColumn: String): StructType = {
    updateSchema(schema, inputColumn, new io.deepsense.sparkutils.Linalg.VectorUDT())
  }

  /**
   * Converts Schema by changing `inputColumn` type to double.
   * `convertOutputVectorToDouble` allows converting `outputColumn` to double
   */
  def revertSchema(
      schema: StructType,
      inputColumn: String,
      outputColumn: String,
      convertOutputVectorToDouble: Boolean): StructType = {
    val unconvertedSchema = updateSchema(schema, inputColumn, DoubleType)
    if (convertOutputVectorToDouble) {
      // Automatically convert one-element vector output column to double column
      updateSchema(unconvertedSchema, outputColumn, DoubleType)
    } else {
      unconvertedSchema
    }
  }

  /**
   * Converts DataFrame by changing `inputColumn` type to vector
   */
  def convertDataFrame(
      dataFrame: DataFrame,
      inputColumn: String,
      // outputColumn: String,
      context: ExecutionContext): org.apache.spark.sql.DataFrame = {
    val inputColumnIdx = dataFrame.schema.get.fieldIndex(inputColumn)
    val convertedRdd = dataFrame.sparkDataFrame.rdd.map { r =>
      val value = r.get(inputColumnIdx)
      if (value != null) {
        Row.fromSeq(r.toSeq.updated(inputColumnIdx, Vectors.dense(value.asInstanceOf[Double])))
      } else {
        Row.fromSeq(r.toSeq.updated(inputColumnIdx, null))
      }
    }
    val convertedSchema = NumericToVectorUtils.convertSchema(dataFrame.schema.get, inputColumn)
    val convertedDf = context.sparkSQLSession.createDataFrame(convertedRdd, convertedSchema)
    convertedDf
  }

  /**
   * Converts DataFrame by changing `inputColumn` type to double.
   * `convertOutputVectorToDouble` allows converting `outputColumn` to double
   */
  def revertDataFrame(
      dataFrame: org.apache.spark.sql.DataFrame,
      expectedSchema: StructType,
      inputColumn: String,
      outputColumn: String,
      context: ExecutionContext,
      convertOutputVectorToDouble: Boolean): org.apache.spark.sql.DataFrame = {
    val inputColumnIdx = dataFrame.schema.fieldIndex(inputColumn)
    val outputColumnIdx = dataFrame.schema.fieldIndex(outputColumn)
    val extractFirstValueFromVector = (columnIdx: Int) => (r: Row) => {
      val vector = r.get(columnIdx)
      if (vector != null) {
        Row.fromSeq(r.toSeq.updated(
          columnIdx,
          vector.asInstanceOf[io.deepsense.sparkutils.Linalg.Vector].apply(0)))
      } else {
        Row.fromSeq(r.toSeq.updated(columnIdx, null))
      }
    }
    val transformedInputColumnRdd = dataFrame.rdd.map(extractFirstValueFromVector(inputColumnIdx))
    val transformedRdd =
      if (convertOutputVectorToDouble && inputColumnIdx != outputColumnIdx) {
        transformedInputColumnRdd.map(extractFirstValueFromVector(outputColumnIdx))
      } else {
        transformedInputColumnRdd
      }
    context.sparkSQLSession.createDataFrame(transformedRdd, expectedSchema)
  }
}
