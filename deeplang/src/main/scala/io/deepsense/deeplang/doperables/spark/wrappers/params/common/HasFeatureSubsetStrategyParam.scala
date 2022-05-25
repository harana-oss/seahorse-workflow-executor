package io.deepsense.deeplang.doperables.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import io.deepsense.deeplang.params.choice.Choice
import io.deepsense.deeplang.params.wrappers.spark.ChoiceParamWrapper
import io.deepsense.deeplang.params.{Param, Params}

trait HasFeatureSubsetStrategyParam extends Params {

  val featureSubsetStrategy =
    new ChoiceParamWrapper[ml.param.Params {val featureSubsetStrategy: ml.param.Param[String]},
      FeatureSubsetStrategy.Option](
      name = "feature subset strategy",
      description = Some("The number of features to consider for splits at each tree node."),
      sparkParamGetter = _.featureSubsetStrategy)

  setDefault(featureSubsetStrategy, FeatureSubsetStrategy.Auto())

}

object FeatureSubsetStrategy {

  sealed abstract class Option(override val name: String) extends Choice {

    override val params: Array[Param[_]] = Array()

    override val choiceOrder: List[Class[_ <: Choice]] = List(
      classOf[Auto],
      classOf[OneThird],
      classOf[Sqrt],
      classOf[Log2]
    )
  }

  case class Auto() extends Option("auto")
  case class OneThird() extends Option("onethird")
  case class Sqrt() extends Option("sqrt")
  case class Log2() extends Option("log2")

}
