package io.deepsense.deeplang.doperables.spark.wrappers.params.common

import scala.language.reflectiveCalls

import io.deepsense.deeplang.params.{BooleanParam, Params}

trait HasIsLargerBetterParam extends Params {

  val isLargerBetterParam = BooleanParam(
    name = "is larger better",
    description =
      Some("""Indicates whether the returned metric
        |is better to be maximized or minimized.""".stripMargin))
  setDefault(isLargerBetterParam -> false)
}
