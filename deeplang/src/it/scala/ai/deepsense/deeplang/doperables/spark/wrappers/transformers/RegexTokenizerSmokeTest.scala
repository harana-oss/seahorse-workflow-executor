package ai.deepsense.deeplang.doperables.spark.wrappers.transformers

import org.apache.spark.sql.types.ArrayType
import org.apache.spark.sql.types.DataType
import org.apache.spark.sql.types.StringType

import ai.deepsense.deeplang.doperables.multicolumn.MultiColumnParams.SingleOrMultiColumnChoices.SingleColumnChoice
import ai.deepsense.deeplang.doperables.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.NoInPlaceChoice
import ai.deepsense.deeplang.params.selections.NameSingleColumnSelection

class RegexTokenizerSmokeTest
    extends AbstractTransformerWrapperSmokeTest[RegexTokenizer]
    with MultiColumnTransformerWrapperTestSupport {

  override def transformerWithParams: RegexTokenizer = {
    val inPlace = NoInPlaceChoice()
      .setOutputColumn("tokenized")

    val single = SingleColumnChoice()
      .setInputColumn(NameSingleColumnSelection("s"))
      .setInPlace(inPlace)

    val transformer = new RegexTokenizer()
    transformer.set(
      Seq(
        transformer.singleOrMultiChoiceParam -> single,
        transformer.gaps                     -> false,
        transformer.minTokenLength           -> 1,
        transformer.pattern                  -> "\\d+"
      ): _*
    )
  }

  override def testValues: Seq[(Any, Any)] = {
    val strings = Seq(
      "100 200 300",
      "400 500 600",
      "700 800 900"
    )

    val tokenized = strings.map(_.toLowerCase.split(" "))
    strings.zip(tokenized)
  }

  override def inputType: DataType = StringType

  override def outputType: DataType = new ArrayType(StringType, true)

}
