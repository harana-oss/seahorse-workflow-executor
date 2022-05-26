package ai.deepsense.deeplang.utils.aggregators

case class CountOccurenceAggregator[T](elementToCount: T) extends Aggregator[Long, T] {

  override def initialElement: Long = 0

  override def mergeValue(acc: Long, elem: T): Long = {
    if (elem == elementToCount)
      acc + 1
    else
      acc
  }

  override def mergeCombiners(left: Long, right: Long): Long = left + right

}
