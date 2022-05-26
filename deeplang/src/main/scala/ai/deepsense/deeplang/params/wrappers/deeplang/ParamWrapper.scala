package ai.deepsense.deeplang.params.wrappers.deeplang

import org.apache.spark.ml

import ai.deepsense.deeplang.params.Param

class ParamWrapper[T](parentId: String, val param: Param[T])
    extends ml.param.Param[T](parentId, param.name, param.description.getOrElse(""), (_: T) => true)

object ParamWrapper {

  def isValid[T](param: Param[T], value: T): Boolean = param.validate(value).isEmpty

}
