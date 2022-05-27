package ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.selections.NameSingleColumnSelection
import ai.deepsense.deeplang.parameters.wrappers.spark.SingleColumnCreatorParameterWrapper

trait HasOutputColumn extends Params {

  val outputColumn = new SingleColumnCreatorParameterWrapper[ml.param.Params { val outputCol: ml.param.Param[String] }](
    name = "output column",
    description = Some("The output column name."),
    sparkParamGetter = _.outputCol
  )

  def setOutputColumn(value: String): this.type =
    set(outputColumn, value)

}
