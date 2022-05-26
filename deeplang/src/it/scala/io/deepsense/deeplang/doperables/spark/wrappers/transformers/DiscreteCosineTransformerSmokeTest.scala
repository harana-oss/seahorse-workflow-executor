package io.deepsense.deeplang.doperables.spark.wrappers.transformers

import io.deepsense.sparkutils.Linalg.Vectors
import org.apache.spark.sql.types.DataType

import io.deepsense.deeplang.doperables.multicolumn.MultiColumnParams.SingleOrMultiColumnChoices.SingleColumnChoice
import io.deepsense.deeplang.doperables.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.NoInPlaceChoice
import io.deepsense.deeplang.params.selections.NameSingleColumnSelection

class DiscreteCosineTransformerSmokeTest
    extends AbstractTransformerWrapperSmokeTest[DiscreteCosineTransformer]
    with MultiColumnTransformerWrapperTestSupport {

  override def transformerWithParams: DiscreteCosineTransformer = {
    val inPlace = NoInPlaceChoice()
      .setOutputColumn("dct")

    val single = SingleColumnChoice()
      .setInputColumn(NameSingleColumnSelection("v"))
      .setInPlace(inPlace)

    val transformer = new DiscreteCosineTransformer()
    transformer.set(
      Seq(
        transformer.singleOrMultiChoiceParam -> single,
        transformer.inverse                  -> false
      ): _*
    )
  }

  override def testValues: Seq[(Any, Any)] = {
    val input = Seq(
      Vectors.dense(0.0),
      Vectors.dense(1.0),
      Vectors.dense(2.0)
    )
    val inputAfterDCT = Seq(
      Vectors.dense(0.0),
      Vectors.dense(1.0),
      Vectors.dense(2.0)
    )
    input.zip(inputAfterDCT)
  }

  override def inputType: DataType = new io.deepsense.sparkutils.Linalg.VectorUDT

  override def outputType: DataType = new io.deepsense.sparkutils.Linalg.VectorUDT

}
