package ai.deepsense.deeplang.parameters.wrappers.spark

import org.apache.spark.ml

import ai.deepsense.deeplang.parameters.MultipleColumnCreatorParameter

class MultipleColumnCreatorParameterWrapper[P <: ml.param.Params](
    override val name: String,
    override val description: Option[String],
    val sparkParamGetter: P => ml.param.StringArrayParam
) extends MultipleColumnCreatorParameter(name, description)
    with ForwardSparkParameterWrapper[P, Array[String]] {

  override def replicate(name: String): MultipleColumnCreatorParameterWrapper[P] =
    new MultipleColumnCreatorParameterWrapper[P](name, description, sparkParamGetter)

}
