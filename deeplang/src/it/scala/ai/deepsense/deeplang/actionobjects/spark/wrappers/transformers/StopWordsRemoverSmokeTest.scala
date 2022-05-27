package ai.deepsense.deeplang.actionobjects.spark.wrappers.transformers

import org.apache.spark.sql.types.ArrayType
import org.apache.spark.sql.types.DataType
import org.apache.spark.sql.types.StringType

import ai.deepsense.deeplang.actionobjects.multicolumn.MultiColumnParams.SingleOrMultiColumnChoices.SingleColumnChoice
import ai.deepsense.deeplang.actionobjects.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.NoInPlaceChoice
import ai.deepsense.deeplang.parameters.selections.NameSingleColumnSelection

class StopWordsRemoverSmokeTest
    extends AbstractTransformerWrapperSmokeTest[StopWordsRemover]
    with MultiColumnTransformerWrapperTestSupport {

  override def transformerWithParams: StopWordsRemover = {
    val inPlace = NoInPlaceChoice()
      .setOutputColumn("stopWordsRemoverOutput")
    val single  = SingleColumnChoice()
      .setInputColumn(NameSingleColumnSelection("as"))
      .setInPlace(inPlace)

    val stopWordsRemover = new StopWordsRemover()
    stopWordsRemover.set(stopWordsRemover.singleOrMultiChoiceParam -> single, stopWordsRemover.caseSensitive -> false)
  }

  override def testValues: Seq[(Any, Any)] = {
    val inputNumbers  = Seq(Array("a", "seahorse", "The", "Horseshoe", "Crab"))
    val outputNumbers = Seq(Array("seahorse", "Horseshoe", "Crab"))
    inputNumbers.zip(outputNumbers)
  }

  override def inputType: DataType = ArrayType(StringType)

  override def outputType: DataType = ArrayType(StringType)

}
