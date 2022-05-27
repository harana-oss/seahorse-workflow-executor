package ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml
import org.apache.spark.ml.param.{Param => SparkParam}

import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.choice.Choice
import ai.deepsense.deeplang.parameters.choice.ChoiceParameter
import ai.deepsense.deeplang.parameters.wrappers.spark.ParamsWithSparkWrappers
import ai.deepsense.deeplang.parameters.wrappers.spark.SingleColumnCreatorParameterWrapper

trait HasOptionalQuantilesColumnParam extends Params {

  val optionalQuantilesColumn =
    new ChoiceParameter[OptionalQuantilesColumnChoice.QuantilesColumnOption](
      name = "use custom quantiles",
      description = Some("""Param for quantiles column name.
                           |This column will output quantiles of corresponding
                           |quantileProbabilities if it is set.""".stripMargin)
    )

  setDefault(optionalQuantilesColumn, OptionalQuantilesColumnChoice.QuantilesColumnNoOption())

}

object OptionalQuantilesColumnChoice {

  sealed trait QuantilesColumnOption extends Choice with ParamsWithSparkWrappers {

    override val choiceOrder: List[Class[_ <: QuantilesColumnOption]] =
      List(classOf[QuantilesColumnNoOption], classOf[QuantilesColumnYesOption])

  }

  case class QuantilesColumnYesOption() extends QuantilesColumnOption {

    val quantilesColumn = new SingleColumnCreatorParameterWrapper[ml.param.Params { val quantilesCol: SparkParam[String] }](
      name = "quantiles column",
      description = Some("The quantiles column for a model."),
      sparkParamGetter = _.quantilesCol
    )

    setDefault(quantilesColumn, "quantiles")

    override val name = "yes"

    override val params: Array[ai.deepsense.deeplang.parameters.Parameter[_]] = Array(quantilesColumn)

  }

  case class QuantilesColumnNoOption() extends QuantilesColumnOption {

    override val name = "no"

    override val params: Array[ai.deepsense.deeplang.parameters.Parameter[_]] = Array()

  }

}
