package ai.deepsense.deeplang.params.wrappers.spark

import org.apache.spark.ml

import ai.deepsense.deeplang.params.StringParam
import ai.deepsense.deeplang.params.validators.AcceptAllRegexValidator
import ai.deepsense.deeplang.params.validators.Validator

class StringParamWrapper[P <: ml.param.Params](
    override val name: String,
    override val description: Option[String],
    val sparkParamGetter: P => ml.param.Param[String],
    override val validator: Validator[String] = new AcceptAllRegexValidator
) extends StringParam(name, description, validator)
    with ForwardSparkParamWrapper[P, String] {

  override def replicate(name: String): StringParamWrapper[P] =
    new StringParamWrapper[P](name, description, sparkParamGetter, validator)

}
