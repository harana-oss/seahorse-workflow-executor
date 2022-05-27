package ai.deepsense.deeplang.parameters.wrappers.deeplang

import org.apache.spark.ml

import ai.deepsense.deeplang.parameters.Parameter

class ParamWrapper[T](parentId: String, val param: Parameter[T])
    extends ml.param.Param[T](parentId, param.name, param.description.getOrElse(""), (_: T) => true)

object ParamWrapper {

  def isValid[T](param: Parameter[T], value: T): Boolean = param.validate(value).isEmpty

}
