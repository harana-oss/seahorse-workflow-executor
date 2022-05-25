package io.deepsense.deeplang.doperables.spark.wrappers.estimators

import org.apache.spark.ml.clustering.{LDA => SparkLDA, LDAModel => SparkLDAModel}

import io.deepsense.deeplang.doperables.SparkEstimatorWrapper
import io.deepsense.deeplang.doperables.spark.wrappers.models.LDAModel
import io.deepsense.deeplang.doperables.spark.wrappers.params.common._
import io.deepsense.deeplang.params.choice.Choice
import io.deepsense.deeplang.params.validators.{ArrayLengthValidator, ComplexArrayValidator, RangeValidator}
import io.deepsense.deeplang.params.wrappers.spark._

class LDA extends SparkEstimatorWrapper[SparkLDAModel, SparkLDA, LDAModel]
  with HasCheckpointIntervalParam
  with HasFeaturesColumnParam
  with HasNumberOfClustersParam
  with HasMaxIterationsParam
  with HasSeedParam {

  import LDA._

  override lazy val maxIterationsDefault = 20.0

  val optimizer = new ChoiceParamWrapper[SparkLDA, LDAOptimizer](
    name = "optimizer",
    description =
      Some("""Optimizer or inference algorithm used to estimate the LDA model. Currently supported:
        |Online Variational Bayes, Expectation-Maximization""".stripMargin),
    sparkParamGetter = _.optimizer)
  setDefault(optimizer, OnlineLDAOptimizer())

  val subsamplingRate = new DoubleParamWrapper[SparkLDA](
    name = "subsampling rate",
    description =
      Some("""Fraction of the corpus to be sampled and used in each iteration of mini-batch gradient
        |descent. Note that this should be adjusted in synchronization with `max iterations` so the
        |entire corpus is used. Specifically, set both so that `max iterations` * `subsampling rate`
        |>= 1.
        |""".stripMargin),
    sparkParamGetter = _.subsamplingRate,
    validator = RangeValidator(0.0, 1.0, beginIncluded = false))
  setDefault(subsamplingRate, 0.05)

  val topicDistributionColumn = new SingleColumnCreatorParamWrapper[SparkLDA](
    name = "topic distribution column",
    description =
      Some("""Output column with estimates of the topic mixture distribution for each document
        |(often called \"theta\" in the literature). Returns a vector of zeros for
        |an empty document.""".stripMargin),
    sparkParamGetter = _.topicDistributionCol)
  setDefault(topicDistributionColumn, "topicDistribution")

  val params: Array[io.deepsense.deeplang.params.Param[_]] = Array(
    checkpointInterval,
    k,
    maxIterations,
    optimizer,
    subsamplingRate,
    topicDistributionColumn,
    featuresColumn,
    seed)
}

object LDA {

  class DocConcentrationParam(
      override val name: String,
      override val validator: ComplexArrayValidator)
    extends DoubleArrayParamWrapper[SparkLDA](
      name = name,
      description =
        Some("""Concentration parameter (commonly named "alpha") for the prior placed on documents'
          |distributions over topics ("theta"). This is the parameter to a Dirichlet distribution,
          |where larger values mean more smoothing (more regularization). If not set by the user,
          |then docConcentration is set automatically. If set to singleton vector [alpha], then
          |alpha is replicated to a vector of length k in fitting. Otherwise, the docConcentration
          |vector must be length k.""".stripMargin),
      sparkParamGetter = _.docConcentration,
      validator = validator)

  class TopicConcentrationParam(override val name: String, override val validator: RangeValidator)
    extends DoubleParamWrapper[SparkLDA](
      name = name,
      description =
        Some("""Concentration parameter (commonly named "beta" or "eta") for the prior placed on topics'
          |distributions over terms. This is the parameter to a symmetric Dirichlet distribution.
          |""".stripMargin),
      sparkParamGetter = _.topicConcentration,
      validator = validator)

  sealed trait LDAOptimizer extends Choice with ParamsWithSparkWrappers {

    val docConcentration = createDocumentConcentrationParam()

    val topicConcentration = createTopicConcentrationParam()

    def setDocConcentration(v: Array[Double]): this.type = set(docConcentration, v)

    def setTopicConcentration(v: Double): this.type = set(topicConcentration, v)

    protected def createDocumentConcentrationParam(): DocConcentrationParam

    protected def createTopicConcentrationParam(): TopicConcentrationParam

    override val choiceOrder: List[Class[_ <: LDAOptimizer]] = List(
      classOf[OnlineLDAOptimizer],
      classOf[ExpectationMaximizationLDAOptimizer])

    override val params: Array[io.deepsense.deeplang.params.Param[_]] = Array(docConcentration, topicConcentration)
  }

  case class OnlineLDAOptimizer() extends LDAOptimizer {
    override val name = "online"

    override def createDocumentConcentrationParam(): DocConcentrationParam =
      new DocConcentrationParam(
        name = "doc concentration",
        validator = ComplexArrayValidator(
          rangeValidator = RangeValidator(0.0, Double.MaxValue),
          lengthValidator = ArrayLengthValidator.withAtLeast(1)))
    setDefault(docConcentration, Array(0.5, 0.5))

    override def createTopicConcentrationParam(): TopicConcentrationParam =
      new TopicConcentrationParam(
        name = "topic concentration",
        validator = RangeValidator(0.0, Double.MaxValue))
    setDefault(topicConcentration, 0.5)
  }

  case class ExpectationMaximizationLDAOptimizer() extends LDAOptimizer {
    override val name = "em"

    override def createDocumentConcentrationParam(): DocConcentrationParam =
      new DocConcentrationParam(
        name = "doc concentration",
        validator = ComplexArrayValidator(
          rangeValidator = RangeValidator(1.0, Double.MaxValue, beginIncluded = false),
          lengthValidator = ArrayLengthValidator.withAtLeast(1)))
    setDefault(docConcentration, Array(26.0, 26.0))

    override def createTopicConcentrationParam(): TopicConcentrationParam =
      new TopicConcentrationParam(
        name = "topic concentration",
        validator = RangeValidator(1.0, Double.MaxValue, beginIncluded = false))
    setDefault(topicConcentration, 1.1)
  }
}
