package ai.deepsense.deeplang.doperables.dataframe

object ColumnRole extends Enumeration {

  type ColumnRole = RichValue

  val Feature = RichValue("feature")

  val Label = RichValue("label")

  val Prediction = RichValue("prediction")

  val Id = RichValue("id")

  val Ignored = RichValue("ignored")

  case class RichValue(name: String) extends Val(nextId, name)

}
