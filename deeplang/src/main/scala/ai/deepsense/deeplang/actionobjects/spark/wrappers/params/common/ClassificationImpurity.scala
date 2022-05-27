package ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common

import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.ClassificationImpurity.Gini
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.ClassificationImpurity.Entropy
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.choice.Choice

sealed abstract class ClassificationImpurity(override val name: String) extends Choice {

  override val params: Array[Parameter[_]] = Array()

  override val choiceOrder: List[Class[_ <: Choice]] = List(
    classOf[Entropy],
    classOf[Gini]
  )

}

object ClassificationImpurity {

  case class Entropy() extends ClassificationImpurity("entropy")

  case class Gini() extends ClassificationImpurity("gini")

}
