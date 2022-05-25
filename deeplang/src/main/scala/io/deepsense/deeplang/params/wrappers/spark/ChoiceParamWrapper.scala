package io.deepsense.deeplang.params.wrappers.spark

import scala.reflect.runtime.universe._

import org.apache.spark.ml
import org.apache.spark.sql.types.StructType

import io.deepsense.deeplang.params.choice.{Choice, ChoiceParam}

class ChoiceParamWrapper[P <: ml.param.Params, T <: Choice : TypeTag](
    override val name: String,
    override val description: Option[String],
    val sparkParamGetter: P => ml.param.Param[String])
  extends ChoiceParam[T](name, description)
  with SparkParamWrapper[P, String, T] {

  override def convert(value: T)(schema: StructType): String = value.name

  override def replicate(name: String): ChoiceParamWrapper[P, T] =
    new ChoiceParamWrapper[P, T](name, description, sparkParamGetter)
}
