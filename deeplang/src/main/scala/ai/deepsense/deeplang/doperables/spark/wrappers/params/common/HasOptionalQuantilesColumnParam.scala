package ai.deepsense.deeplang.doperables.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml
import org.apache.spark.ml.param.{Param => SparkParam}

import ai.deepsense.deeplang.params.Params
import ai.deepsense.deeplang.params.choice.Choice
import ai.deepsense.deeplang.params.choice.ChoiceParam
import ai.deepsense.deeplang.params.wrappers.spark.ParamsWithSparkWrappers
import ai.deepsense.deeplang.params.wrappers.spark.SingleColumnCreatorParamWrapper

trait HasOptionalQuantilesColumnParam extends Params {

  val optionalQuantilesColumn =
    new ChoiceParam[OptionalQuantilesColumnChoice.QuantilesColumnOption](
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

    val quantilesColumn = new SingleColumnCreatorParamWrapper[ml.param.Params { val quantilesCol: SparkParam[String] }](
      name = "quantiles column",
      description = Some("The quantiles column for a model."),
      sparkParamGetter = _.quantilesCol
    )

    setDefault(quantilesColumn, "quantiles")

    override val name = "yes"

    override val params: Array[ai.deepsense.deeplang.params.Param[_]] = Array(quantilesColumn)

  }

  case class QuantilesColumnNoOption() extends QuantilesColumnOption {

    override val name = "no"

    override val params: Array[ai.deepsense.deeplang.params.Param[_]] = Array()

  }

}
