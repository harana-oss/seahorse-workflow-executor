package io.deepsense.deeplang.params.wrappers.spark

import scala.reflect.runtime.universe._

import org.apache.spark.ml
import org.apache.spark.sql.types.StructType

import io.deepsense.deeplang.params.choice.Choice
import io.deepsense.deeplang.params.choice.MultipleChoiceParam

class MultipleChoiceParamWrapper[P <: ml.param.Params, T <: Choice: TypeTag](
    override val name: String,
    override val description: Option[String],
    val sparkParamGetter: P => ml.param.StringArrayParam
) extends MultipleChoiceParam[T](name, description)
    with SparkParamWrapper[P, Array[String], Set[T]] {

  override def convert(value: Set[T])(schema: StructType): Array[String] = value.map(_.name).toArray

  override def replicate(name: String): MultipleChoiceParamWrapper[P, T] =
    new MultipleChoiceParamWrapper[P, T](name, description, sparkParamGetter)

}
