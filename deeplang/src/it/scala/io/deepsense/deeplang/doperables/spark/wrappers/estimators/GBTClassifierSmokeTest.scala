package io.deepsense.deeplang.doperables.spark.wrappers.estimators

import org.apache.spark.sql.types.{DoubleType, Metadata, StructType}

import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperables.spark.wrappers.params.common.ClassificationImpurity
import io.deepsense.deeplang.params.ParamPair
import io.deepsense.deeplang.params.selections.NameSingleColumnSelection
import io.deepsense.deeplang.utils.DataFrameUtils

class GBTClassifierSmokeTest
  extends AbstractEstimatorModelWrapperSmokeTest {

  override def className: String = "GBTClassifier"

  override val estimator = new GBTClassifier()

  private val labelColumnName = "myRating"

  import estimator.vanillaGBTClassifier._

  override val estimatorParams: Seq[ParamPair[_]] = Seq(
    featuresColumn -> NameSingleColumnSelection("myFeatures"),
    impurity -> ClassificationImpurity.Entropy(),
    labelColumn -> NameSingleColumnSelection(labelColumnName),
    lossType -> GBTClassifier.Logistic(),
    maxBins -> 2.0,
    maxDepth -> 6.0,
    maxIterations -> 10.0,
    minInfoGain -> 0.0,
    minInstancesPerNode -> 1,
    predictionColumn -> "prediction",
    seed -> 100.0,
    stepSize -> 0.11,
    subsamplingRate -> 0.999
  )

  override def assertTransformedDF(dataFrame: DataFrame): Unit = {
    val possibleValues = DataFrameUtils.collectValues(dataFrame, labelColumnName)
    val actualValues = DataFrameUtils.collectValues(dataFrame, "prediction")

    actualValues.diff(possibleValues) shouldBe empty
  }

  override def assertTransformedSchema(schema: StructType): Unit = {
    val predictionColumn = schema.fields.last
    predictionColumn.name shouldBe "prediction"
    predictionColumn.dataType shouldBe DoubleType
    predictionColumn.metadata shouldBe Metadata.empty
  }
}
