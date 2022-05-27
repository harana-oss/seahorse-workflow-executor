package ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml
import org.apache.spark.ml.param.{Param => SparkParam}

import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.choice.Choice
import ai.deepsense.deeplang.parameters.choice.ChoiceParameter
import ai.deepsense.deeplang.parameters.selections.NameSingleColumnSelection
import ai.deepsense.deeplang.parameters.selections.SingleColumnSelection
import ai.deepsense.deeplang.parameters.wrappers.spark.ParamsWithSparkWrappers
import ai.deepsense.deeplang.parameters.wrappers.spark.SingleColumnSelectorParameterWrapper

trait HasOptionalWeightColumnParam extends Params {

  val optionalWeightColumn =
    new ChoiceParameter[OptionalWeightColumnChoice.WeightColumnOption](
      name = "use custom weights",
      description = Some("""Whether to over-/under-sample training instances according to the given weights in
                           |the `weight column`. If the `weight column` is not specified,
                           |all instances are treated equally with a weight 1.0.""".stripMargin)
    )

  setDefault(optionalWeightColumn, OptionalWeightColumnChoice.WeightColumnNoOption())

}

object OptionalWeightColumnChoice {

  sealed trait WeightColumnOption extends Choice with ParamsWithSparkWrappers {

    override val choiceOrder: List[Class[_ <: WeightColumnOption]] =
      List(classOf[WeightColumnNoOption], classOf[WeightColumnYesOption])

  }

  case class WeightColumnYesOption() extends WeightColumnOption {

    val weightColumn = new SingleColumnSelectorParameterWrapper[ml.param.Params { val weightCol: SparkParam[String] }](
      name = "weight column",
      description = Some("The weight column for a model."),
      sparkParamGetter = _.weightCol,
      portIndex = 0
    )

    setDefault(weightColumn, NameSingleColumnSelection("weight"))

    def getWeightColumn: SingleColumnSelection = $(weightColumn)

    def setWeightColumn(value: SingleColumnSelection): this.type = set(weightColumn -> value)

    override val name = "yes"

    override val params: Array[ai.deepsense.deeplang.parameters.Parameter[_]] = Array(weightColumn)

  }

  case class WeightColumnNoOption() extends WeightColumnOption {

    override val name = "no"

    override val params: Array[ai.deepsense.deeplang.parameters.Parameter[_]] = Array()

  }

}
