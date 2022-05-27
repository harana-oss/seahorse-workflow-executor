package ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common

import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.choice.Choice

sealed abstract class RegressionImpurity(override val name: String) extends Choice {

  import RegressionImpurity._

  override val params: Array[Parameter[_]] = Array()

  override val choiceOrder: List[Class[_ <: Choice]] = List(
    classOf[Variance]
  )

}

object RegressionImpurity {

  case class Variance() extends RegressionImpurity("variance")

}
