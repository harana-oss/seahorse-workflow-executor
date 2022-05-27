package ai.deepsense.deeplang.parameters.wrappers.spark

import scala.reflect.runtime.universe._

import org.apache.spark.ml
import org.apache.spark.sql.types.StructType

import ai.deepsense.deeplang.parameters.choice.Choice
import ai.deepsense.deeplang.parameters.choice.MultipleChoiceParameter

class MultipleChoiceParameterWrapper[P <: ml.param.Params, T <: Choice: TypeTag](
    override val name: String,
    override val description: Option[String],
    val sparkParamGetter: P => ml.param.StringArrayParam
) extends MultipleChoiceParameter[T](name, description)
    with SparkParameterWrapper[P, Array[String], Set[T]] {

  override def convert(value: Set[T])(schema: StructType): Array[String] = value.map(_.name).toArray

  override def replicate(name: String): MultipleChoiceParameterWrapper[P, T] =
    new MultipleChoiceParameterWrapper[P, T](name, description, sparkParamGetter)

}
