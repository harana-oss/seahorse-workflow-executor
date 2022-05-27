package ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common

import scala.language.reflectiveCalls

import ai.deepsense.deeplang.parameters.BooleanParameter
import ai.deepsense.deeplang.parameters.Params

trait HasIsLargerBetterParam extends Params {

  val isLargerBetterParam = BooleanParameter(
    name = "is larger better",
    description = Some("""Indicates whether the returned metric
                         |is better to be maximized or minimized.""".stripMargin)
  )

  setDefault(isLargerBetterParam -> false)

}
