package io.deepsense.deeplang.doperables.spark.wrappers.transformers

import io.deepsense.sparkutils.Linalg.Vectors
import org.apache.spark.sql.types.DataType

import io.deepsense.deeplang.doperables.multicolumn.MultiColumnParams.SingleOrMultiColumnChoices.SingleColumnChoice
import io.deepsense.deeplang.doperables.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.NoInPlaceChoice
import io.deepsense.deeplang.params.selections.NameSingleColumnSelection

class PolynomialExpanderSmokeTest
  extends AbstractTransformerWrapperSmokeTest[PolynomialExpander]
  with MultiColumnTransformerWrapperTestSupport {

  override def transformerWithParams: PolynomialExpander = {
    val inPlace = NoInPlaceChoice()
      .setOutputColumn("polynomial")

    val single = SingleColumnChoice()
      .setInputColumn(NameSingleColumnSelection("v"))
      .setInPlace(inPlace)

    val transformer = new PolynomialExpander()
    transformer.set(Seq(
      transformer.singleOrMultiChoiceParam -> single,
      transformer.degree -> 3
    ): _*)
  }

  override def testValues: Seq[(Any, Any)] = {
    val input = Seq(
      Vectors.dense(1.0),
      Vectors.dense(1.0, 2.0)
    )
    val inputAfterDCT = Seq(
      // x, x^2, x^3
      Vectors.dense(1.0, 1.0, 1.0),
      // x, x^2, x^3, y, x * y, x^2 * y, x * y^2, y^2, y^3
      Vectors.dense(1.0, 1.0, 1.0, 2.0, 2.0, 2.0, 4.0, 4.0, 8.0)
    )
    input.zip(inputAfterDCT)
  }

  override def inputType: DataType = new io.deepsense.sparkutils.Linalg.VectorUDT

  override def outputType: DataType = new io.deepsense.sparkutils.Linalg.VectorUDT
}
