package io.deepsense.deeplang.utils

import org.apache.spark.sql.Row
import org.apache.spark.sql.types.StructType
import org.scalatest.Matchers

import io.deepsense.deeplang.doperables.dataframe.DataFrame

trait DataFrameMatchers extends Matchers {
  def assertDataFramesEqual(
      actualDf: DataFrame,
      expectedDf: DataFrame,
      checkRowOrder: Boolean = true,
      checkNullability: Boolean = true): Unit = {
    // Checks only semantic identity, not objects location in memory
    assertSchemaEqual(
      actualDf.sparkDataFrame.schema, expectedDf.sparkDataFrame.schema, checkNullability)
    val collectedRows1: Array[Row] = actualDf.sparkDataFrame.collect()
    val collectedRows2: Array[Row] = expectedDf.sparkDataFrame.collect()
    if (checkRowOrder) {
      collectedRows1 shouldBe collectedRows2
    } else {
      collectedRows1 should contain theSameElementsAs collectedRows2
    }
  }

  def assertSchemaEqual(
      actualSchema: StructType,
      expectedSchema: StructType,
      checkNullability: Boolean): Unit = {
    val (actual, expected) = if (checkNullability) {
      (actualSchema, expectedSchema)
    } else {
      val actualNonNull = StructType(actualSchema.map(_.copy(nullable = false)))
      val expectedNonNull = StructType(expectedSchema.map(_.copy(nullable = false)))
      (actualNonNull, expectedNonNull)
    }
    assertSchemaEqual(actual, expected)
  }

  def assertSchemaEqual(actualSchema: StructType, expectedSchema: StructType): Unit = {
    actualSchema.treeString shouldBe expectedSchema.treeString
  }
}

object DataFrameMatchers extends DataFrameMatchers
