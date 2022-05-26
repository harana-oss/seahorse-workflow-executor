package io.deepsense.deeplang.doperables.spark.wrappers.params.common

import io.deepsense.deeplang.doperables.spark.wrappers.params.common.ClassificationImpurity.Gini
import io.deepsense.deeplang.doperables.spark.wrappers.params.common.ClassificationImpurity.Entropy
import io.deepsense.deeplang.params.Param
import io.deepsense.deeplang.params.choice.Choice

sealed abstract class ClassificationImpurity(override val name: String) extends Choice {

  override val params: Array[Param[_]] = Array()

  override val choiceOrder: List[Class[_ <: Choice]] = List(
    classOf[Entropy],
    classOf[Gini]
  )

}

object ClassificationImpurity {

  case class Entropy() extends ClassificationImpurity("entropy")

  case class Gini() extends ClassificationImpurity("gini")

}
