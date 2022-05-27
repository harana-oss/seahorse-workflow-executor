package ai.deepsense.deeplang.parameters.wrappers.spark

import scala.reflect.runtime.universe._

import org.apache.spark.ml
import org.apache.spark.sql.types.StructType

import ai.deepsense.deeplang.parameters.choice.Choice
import ai.deepsense.deeplang.parameters.choice.ChoiceParameter

class ChoiceParameterWrapper[P <: ml.param.Params, T <: Choice: TypeTag](
    override val name: String,
    override val description: Option[String],
    val sparkParamGetter: P => ml.param.Param[String]
) extends ChoiceParameter[T](name, description)
    with SparkParameterWrapper[P, String, T] {

  override def convert(value: T)(schema: StructType): String = value.name

  override def replicate(name: String): ChoiceParameterWrapper[P, T] =
    new ChoiceParameterWrapper[P, T](name, description, sparkParamGetter)

}
