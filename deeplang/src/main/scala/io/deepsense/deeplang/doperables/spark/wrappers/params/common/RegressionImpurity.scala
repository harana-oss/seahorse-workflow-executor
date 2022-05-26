package io.deepsense.deeplang.doperables.spark.wrappers.params.common

import io.deepsense.deeplang.params.Param
import io.deepsense.deeplang.params.choice.Choice

sealed abstract class RegressionImpurity(override val name: String) extends Choice {

  import RegressionImpurity._

  override val params: Array[Param[_]] = Array()

  override val choiceOrder: List[Class[_ <: Choice]] = List(
    classOf[Variance]
  )

}

object RegressionImpurity {

  case class Variance() extends RegressionImpurity("variance")

}
