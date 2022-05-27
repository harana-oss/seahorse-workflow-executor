package ai.deepsense.deeplang.actionobjects.spark.wrappers.transformers

import ai.deepsense.sparkutils.Linalg.Vectors
import org.apache.spark.sql.types.DataType
import org.apache.spark.sql.types.DoubleType

import ai.deepsense.deeplang.actionobjects.multicolumn.MultiColumnParams.SingleOrMultiColumnChoices.SingleColumnChoice
import ai.deepsense.deeplang.actionobjects.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.NoInPlaceChoice
import ai.deepsense.deeplang.parameters.selections.NameSingleColumnSelection

class OneHotEncoderSmokeTest
    extends AbstractTransformerWrapperSmokeTest[OneHotEncoder]
    with MultiColumnTransformerWrapperTestSupport {

  override def transformerWithParams: OneHotEncoder = {
    val inPlace = NoInPlaceChoice()
      .setOutputColumn("oneHotEncoderOutput")
    val single  = SingleColumnChoice()
      .setInputColumn(NameSingleColumnSelection("d"))
      .setInPlace(inPlace)

    val oneHotEncoder = new OneHotEncoder()
    oneHotEncoder.set(oneHotEncoder.singleOrMultiChoiceParam -> single, oneHotEncoder.dropLast -> false)
  }

  override def testValues: Seq[(Any, Any)] = {
    val inputNumbers  = Seq(0.0, 1.0)
    val outputNumbers = Seq(Vectors.dense(1.0, 0.0), Vectors.dense(0.0, 1.0))
    inputNumbers.zip(outputNumbers)
  }

  override def inputType: DataType = DoubleType

  override def outputType: DataType = new ai.deepsense.sparkutils.Linalg.VectorUDT

}
