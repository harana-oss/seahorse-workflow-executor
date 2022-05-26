package ai.deepsense.deeplang.doperables.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import ai.deepsense.deeplang.params.Params
import ai.deepsense.deeplang.params.selections.NameSingleColumnSelection
import ai.deepsense.deeplang.params.wrappers.spark.SingleColumnCreatorParamWrapper

trait HasOutputColumn extends Params {

  val outputColumn = new SingleColumnCreatorParamWrapper[ml.param.Params { val outputCol: ml.param.Param[String] }](
    name = "output column",
    description = Some("The output column name."),
    sparkParamGetter = _.outputCol
  )

  def setOutputColumn(value: String): this.type =
    set(outputColumn, value)

}
