package ai.deepsense.deeplang.doperables

import org.apache.spark.sql.types._

import ai.deepsense.deeplang.params.choice.Choice

sealed abstract class TargetTypeChoice(val columnType: DataType) extends Choice {

  override val choiceOrder: List[Class[_ <: Choice]] = TargetTypeChoices.choiceOrder

  override val params: Array[ai.deepsense.deeplang.params.Param[_]] = Array()

  val name = columnType.simpleString

}

object TargetTypeChoices {

  val choiceOrder = List(
    StringTargetTypeChoice(),
    BooleanTargetTypeChoice(),
    TimestampTargetTypeChoice(),
    DoubleTargetTypeChoice(),
    FloatTargetTypeChoice(),
    LongTargetTypeChoice(),
    IntegerTargetTypeChoice()
  ).map(_.getClass)

  case class StringTargetTypeChoice() extends TargetTypeChoice(StringType)

  case class DoubleTargetTypeChoice() extends TargetTypeChoice(DoubleType)

  case class TimestampTargetTypeChoice() extends TargetTypeChoice(TimestampType)

  case class BooleanTargetTypeChoice() extends TargetTypeChoice(BooleanType)

  case class IntegerTargetTypeChoice() extends TargetTypeChoice(IntegerType)

  case class FloatTargetTypeChoice() extends TargetTypeChoice(FloatType)

  case class LongTargetTypeChoice() extends TargetTypeChoice(LongType)

}
