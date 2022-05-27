package ai.deepsense.deeplang.actionobjects.multicolumn

import ai.deepsense.deeplang.parameters.choice.Choice
import ai.deepsense.deeplang.parameters.SingleColumnCreatorParameter
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.StringParameter

object SingleColumnParams {

  sealed abstract class SingleColumnInPlaceChoice extends Choice {

    override val choiceOrder = SingleTransformInPlaceChoices.choiceOrder

  }

  object SingleTransformInPlaceChoices {

    val choiceOrder: List[Class[_ <: Choice]] =
      List(classOf[YesInPlaceChoice], classOf[NoInPlaceChoice])

    case class YesInPlaceChoice() extends SingleColumnInPlaceChoice {

      override val name: String = "replace input column"

      override val params: Array[Parameter[_]] = Array()

    }

    case class NoInPlaceChoice() extends SingleColumnInPlaceChoice {

      val outputColumnCreatorParam = SingleColumnCreatorParameter(
        name = "output column",
        description = Some("Column to save results to.")
      )

      override val name: String = "append new column"

      override val params: Array[Parameter[_]] = Array(outputColumnCreatorParam)

      def setOutputColumn(columnName: String): this.type = set(outputColumnCreatorParam, columnName)

      def getOutputColumn: String = $(outputColumnCreatorParam)

    }

  }

}
