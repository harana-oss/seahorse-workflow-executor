package ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml
import org.apache.spark.ml.regression.RandomForestRegressor

import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.validators.RangeValidator
import ai.deepsense.deeplang.parameters.wrappers.spark.IntParameterWrapper
import ai.deepsense.deeplang.parameters.wrappers.spark.DoubleParameterWrapper

trait HasMinInstancePerNodeParam extends Params {

  val minInstancesPerNode =
    new IntParameterWrapper[ml.param.Params { val minInstancesPerNode: ml.param.IntParam }](
      name = "min instances per node",
      description = Some(
        "The minimum number of instances each child must have after split. " +
          "If a split causes the left or right child to have fewer instances than the parameter's " +
          "value, the split will be discarded as invalid."
      ),
      sparkParamGetter = _.minInstancesPerNode,
      RangeValidator(1.0, Int.MaxValue, step = Some(1.0))
    )

  setDefault(minInstancesPerNode, 1.0)

}
