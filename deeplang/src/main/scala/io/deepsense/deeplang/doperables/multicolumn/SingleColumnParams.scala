package io.deepsense.deeplang.doperables.multicolumn

import io.deepsense.deeplang.params.choice.Choice
import io.deepsense.deeplang.params.SingleColumnCreatorParam
import io.deepsense.deeplang.params.Param
import io.deepsense.deeplang.params.StringParam

object SingleColumnParams {

  sealed abstract class SingleColumnInPlaceChoice extends Choice {

    override val choiceOrder = SingleTransformInPlaceChoices.choiceOrder

  }

  object SingleTransformInPlaceChoices {

    val choiceOrder: List[Class[_ <: Choice]] =
      List(classOf[YesInPlaceChoice], classOf[NoInPlaceChoice])

    case class YesInPlaceChoice() extends SingleColumnInPlaceChoice {

      override val name: String = "replace input column"

      override val params: Array[Param[_]] = Array()

    }

    case class NoInPlaceChoice() extends SingleColumnInPlaceChoice {

      val outputColumnCreatorParam = SingleColumnCreatorParam(
        name = "output column",
        description = Some("Column to save results to.")
      )

      override val name: String = "append new column"

      override val params: Array[Param[_]] = Array(outputColumnCreatorParam)

      def setOutputColumn(columnName: String): this.type = set(outputColumnCreatorParam, columnName)

      def getOutputColumn: String = $(outputColumnCreatorParam)

    }

  }

}
