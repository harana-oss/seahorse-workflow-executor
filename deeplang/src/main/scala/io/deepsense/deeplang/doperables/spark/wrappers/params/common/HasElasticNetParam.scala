package io.deepsense.deeplang.doperables.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import io.deepsense.deeplang.params.Params
import io.deepsense.deeplang.params.validators.RangeValidator
import io.deepsense.deeplang.params.wrappers.spark.DoubleParamWrapper

trait HasElasticNetParam extends Params {

  val elasticNetParam = new DoubleParamWrapper[ml.param.Params { val elasticNetParam: ml.param.DoubleParam }](
    name = "elastic net param",
    description = Some(
      "The ElasticNet mixing parameter. " +
        "For alpha = 0, the penalty is an L2 penalty. For alpha = 1, it is an L1 penalty."
    ),
    sparkParamGetter = _.elasticNetParam,
    validator = RangeValidator(0.0, 1.0)
  )

  setDefault(elasticNetParam, 0.0)

}
