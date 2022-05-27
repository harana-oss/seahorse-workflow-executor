package ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import ai.deepsense.deeplang.parameters.choice.Choice
import ai.deepsense.deeplang.parameters.wrappers.spark.ChoiceParameterWrapper
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.Params

trait HasFeatureSubsetStrategyParam extends Params {

  val featureSubsetStrategy =
    new ChoiceParameterWrapper[
      ml.param.Params { val featureSubsetStrategy: ml.param.Param[String] },
      FeatureSubsetStrategy.Option
    ](
      name = "feature subset strategy",
      description = Some("The number of features to consider for splits at each tree node."),
      sparkParamGetter = _.featureSubsetStrategy
    )

  setDefault(featureSubsetStrategy, FeatureSubsetStrategy.Auto())

}

object FeatureSubsetStrategy {

  sealed abstract class Option(override val name: String) extends Choice {

    override val params: Array[Parameter[_]] = Array()

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
