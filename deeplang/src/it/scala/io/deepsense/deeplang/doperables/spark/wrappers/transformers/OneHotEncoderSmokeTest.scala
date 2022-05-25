package io.deepsense.deeplang.doperables.spark.wrappers.transformers

import io.deepsense.sparkutils.Linalg.Vectors
import org.apache.spark.sql.types.{DataType, DoubleType}

import io.deepsense.deeplang.doperables.multicolumn.MultiColumnParams.SingleOrMultiColumnChoices.SingleColumnChoice
import io.deepsense.deeplang.doperables.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.NoInPlaceChoice
import io.deepsense.deeplang.params.selections.NameSingleColumnSelection

class OneHotEncoderSmokeTest
    extends AbstractTransformerWrapperSmokeTest[OneHotEncoder]
    with MultiColumnTransformerWrapperTestSupport  {

  override def transformerWithParams: OneHotEncoder = {
    val inPlace = NoInPlaceChoice()
      .setOutputColumn("oneHotEncoderOutput")
    val single = SingleColumnChoice()
      .setInputColumn(NameSingleColumnSelection("d"))
      .setInPlace(inPlace)

    val oneHotEncoder = new OneHotEncoder()
    oneHotEncoder.set(
      oneHotEncoder.singleOrMultiChoiceParam -> single,
      oneHotEncoder.dropLast -> false)
  }

  override def testValues: Seq[(Any, Any)] = {
    val inputNumbers = Seq(0.0, 1.0)
    val outputNumbers = Seq(Vectors.dense(1.0, 0.0), Vectors.dense(0.0, 1.0))
    inputNumbers.zip(outputNumbers)
  }

  override def inputType: DataType = DoubleType

  override def outputType: DataType = new io.deepsense.sparkutils.Linalg.VectorUDT
}
