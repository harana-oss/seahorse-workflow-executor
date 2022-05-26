package ai.deepsense.deeplang.doperables.multicolumn

import ai.deepsense.deeplang.doperables.multicolumn.MultiColumnParams.MultiColumnInPlaceChoices.MultiColumnYesInPlace
import ai.deepsense.deeplang.doperables.multicolumn.SingleColumnParams.SingleColumnInPlaceChoice
import ai.deepsense.deeplang.params._
import ai.deepsense.deeplang.params.choice.Choice
import ai.deepsense.deeplang.params.choice.ChoiceParam
import ai.deepsense.deeplang.params.selections.MultipleColumnSelection
import ai.deepsense.deeplang.params.selections.NameColumnSelection
import ai.deepsense.deeplang.params.selections.SingleColumnSelection

object MultiColumnParams {

  sealed abstract class MultiColumnInPlaceChoice extends Choice {

    override val choiceOrder = MultiColumnInPlaceChoices.choiceOrder

  }

  object MultiColumnInPlaceChoices {

    val choiceOrder: List[Class[_ <: Choice]] =
      List(classOf[MultiColumnYesInPlace], classOf[MultiColumnNoInPlace])

    case class MultiColumnYesInPlace() extends MultiColumnInPlaceChoice {

      override val name: String = "replace input columns"

      override val params: Array[Param[_]] = Array()

    }

    case class MultiColumnNoInPlace() extends MultiColumnInPlaceChoice {

      override val name: String = "append new columns"

      val outputColumnsPrefixParam = PrefixBasedColumnCreatorParam(
        name = "column name prefix",
        description = Some("Prefix for output columns.")
      )

      override val params: Array[Param[_]] = Array(outputColumnsPrefixParam)

      def getColumnsPrefix: String = $(outputColumnsPrefixParam)

      def setColumnsPrefix(prefix: String): this.type = set(outputColumnsPrefixParam, prefix)

    }

  }

  sealed abstract class SingleOrMultiColumnChoice extends Choice {

    override val choiceOrder = SingleOrMultiColumnChoices.choiceOrder

  }

  object SingleOrMultiColumnChoices {

    val choiceOrder: List[Class[_ <: Choice]] =
      List(classOf[SingleColumnChoice], classOf[MultiColumnChoice])

    case class SingleColumnChoice() extends SingleOrMultiColumnChoice with HasSingleInPlaceParam {

      override val name: String = "one column"

      val inputColumn = SingleColumnSelectorParam(
        name = "input column",
        description = Some("Column to transform."),
        portIndex = 0
      )

      override val params: Array[Param[_]] =
        Array(inputColumn, singleInPlaceChoice)

      def setInputColumn(value: SingleColumnSelection): this.type = set(inputColumn, value)

      def setInPlace(value: SingleColumnInPlaceChoice): this.type =
        set(singleInPlaceChoice, value)

      def getInputColumn: SingleColumnSelection = $(inputColumn)

      def getInPlace: SingleColumnInPlaceChoice = $(singleInPlaceChoice)

    }

    case class MultiColumnChoice() extends SingleOrMultiColumnChoice {

      override val name: String = "multiple columns"

      val inputColumnsParam = ColumnSelectorParam(
        name = "input columns",
        description = Some("Columns to transform."),
        portIndex = 0
      )

      val multiInPlaceChoiceParam = ChoiceParam[MultiColumnInPlaceChoice](
        name = "output",
        description = Some("Output generation mode.")
      )

      setDefault(multiInPlaceChoiceParam, MultiColumnYesInPlace())

      override val params: Array[Param[_]] = Array(
        inputColumnsParam,
        multiInPlaceChoiceParam
      )

      def getMultiInputColumnSelection: MultipleColumnSelection = $(inputColumnsParam)

      def getMultiInPlaceChoice: MultiColumnInPlaceChoice = $(multiInPlaceChoiceParam)

      def setInputColumnsParam(value: MultipleColumnSelection): this.type =
        set(inputColumnsParam, value)

      def setInputColumnsParam(inputColumnNames: Set[String]): this.type =
        setInputColumnsParam(MultipleColumnSelection(Vector(NameColumnSelection(inputColumnNames))))

      def setMultiInPlaceChoice(value: MultiColumnInPlaceChoice): this.type =
        set(multiInPlaceChoiceParam, value)

    }

    object MultiColumnChoice {

      def apply(inputColumnNames: Set[String]): MultiColumnChoice =
        MultiColumnChoice().setInputColumnsParam(inputColumnNames)

    }

  }

}
