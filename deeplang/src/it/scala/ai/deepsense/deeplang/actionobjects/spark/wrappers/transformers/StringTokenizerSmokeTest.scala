package ai.deepsense.deeplang.actionobjects.spark.wrappers.transformers

import org.apache.spark.sql.types.ArrayType
import org.apache.spark.sql.types.DataType
import org.apache.spark.sql.types.StringType

import ai.deepsense.deeplang.actionobjects.multicolumn.MultiColumnParams.SingleOrMultiColumnChoices.SingleColumnChoice
import ai.deepsense.deeplang.actionobjects.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.NoInPlaceChoice
import ai.deepsense.deeplang.parameters.selections.NameSingleColumnSelection

class StringTokenizerSmokeTest
    extends AbstractTransformerWrapperSmokeTest[StringTokenizer]
    with MultiColumnTransformerWrapperTestSupport {

  override def transformerWithParams: StringTokenizer = {
    val inPlace = NoInPlaceChoice()
      .setOutputColumn("tokenized")

    val single = SingleColumnChoice()
      .setInputColumn(NameSingleColumnSelection("s"))
      .setInPlace(inPlace)

    val transformer = new StringTokenizer()
    transformer.set(
      Seq(
        transformer.singleOrMultiChoiceParam -> single
      ): _*
    )
  }

  override def testValues: Seq[(Any, Any)] = {
    val strings = Seq(
      "this is a test",
      "this values should be separated",
      "Bla bla bla!"
    )

    val tokenized = strings.map(_.toLowerCase.split("\\s"))
    strings.zip(tokenized)
  }

  override def inputType: DataType = StringType

  override def outputType: DataType = new ArrayType(StringType, true)

}
