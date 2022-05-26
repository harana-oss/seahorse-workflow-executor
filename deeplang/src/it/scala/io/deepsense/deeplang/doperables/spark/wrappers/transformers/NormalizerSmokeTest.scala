package io.deepsense.deeplang.doperables.spark.wrappers.transformers

import io.deepsense.sparkutils.Linalg.Vectors
import org.apache.spark.sql.types.DataType

import io.deepsense.deeplang.doperables.multicolumn.MultiColumnParams.SingleOrMultiColumnChoices.SingleColumnChoice
import io.deepsense.deeplang.doperables.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.NoInPlaceChoice
import io.deepsense.deeplang.params.selections.NameSingleColumnSelection

class NormalizerSmokeTest
    extends AbstractTransformerWrapperSmokeTest[Normalizer]
    with MultiColumnTransformerWrapperTestSupport {

  override def transformerWithParams: Normalizer = {
    val inPlace = NoInPlaceChoice()
      .setOutputColumn("normalize")

    val single = SingleColumnChoice()
      .setInputColumn(NameSingleColumnSelection("v"))
      .setInPlace(inPlace)

    val transformer = new Normalizer()
    transformer.set(
      Seq(
        transformer.singleOrMultiChoiceParam -> single,
        transformer.p                        -> 1.0
      ): _*
    )
  }

  override def testValues: Seq[(Any, Any)] = {
    val input = Seq(
      Vectors.dense(0.0, 100.0, 100.0),
      Vectors.dense(1.0, 1.0, 0.0),
      Vectors.dense(-3.0, 3.0, 0.0)
    )
    val inputAfterNormalize = Seq(
      Vectors.dense(0.0, 0.5, 0.5),
      Vectors.dense(0.5, 0.5, 0.0),
      Vectors.dense(-0.5, 0.5, 0.0)
    )
    input.zip(inputAfterNormalize)
  }

  override def inputType: DataType = new io.deepsense.sparkutils.Linalg.VectorUDT

  override def outputType: DataType = new io.deepsense.sparkutils.Linalg.VectorUDT

}
