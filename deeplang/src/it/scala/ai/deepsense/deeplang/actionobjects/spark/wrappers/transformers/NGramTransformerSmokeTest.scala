package ai.deepsense.deeplang.actionobjects.spark.wrappers.transformers

import org.apache.spark.sql.types.ArrayType
import org.apache.spark.sql.types.DataType
import org.apache.spark.sql.types.StringType

import ai.deepsense.deeplang.actionobjects.multicolumn.MultiColumnParams.SingleOrMultiColumnChoices.SingleColumnChoice
import ai.deepsense.deeplang.actionobjects.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.NoInPlaceChoice
import ai.deepsense.deeplang.parameters.selections.NameSingleColumnSelection

class NGramTransformerSmokeTest
    extends AbstractTransformerWrapperSmokeTest[NGramTransformer]
    with MultiColumnTransformerWrapperTestSupport {

  override def transformerWithParams: NGramTransformer = {
    val inPlace = NoInPlaceChoice()
      .setOutputColumn("ngrams")

    val single = SingleColumnChoice()
      .setInputColumn(NameSingleColumnSelection("as"))
      .setInPlace(inPlace)

    val transformer = new NGramTransformer()
    transformer.set(
      Seq(
        transformer.singleOrMultiChoiceParam -> single,
        transformer.n                        -> 2
      ): _*
    )
  }

  override def testValues: Seq[(Any, Any)] = {
    val strings = Seq(
      Array("a", "b", "c"),
      Array("d", "e", "f")
    )

    val ngrams = Seq(
      Array("a b", "b c"),
      Array("d e", "e f")
    )
    strings.zip(ngrams)
  }

  override def inputType: DataType = new ArrayType(StringType, true)

  override def outputType: DataType = new ArrayType(StringType, false)

}
