package ai.deepsense.deeplang.utils.aggregators

import scala.collection.mutable

case class CountOccurrencesWithKeyLimitAggregator[T](limit: Long) extends Aggregator[Option[mutable.Map[T, Long]], T] {

  // This might be problematic performance-wise when elements T are HUGE
  // (for example huge texts). Some kind of document-field detection might be needed
  // to avoid calling this on such datasets.

  // Accumulate allows seq and comb function to mutate first argument and return it.
  // This approach saves memory allocations while aggregating data.

  override def initialElement: Option[mutable.Map[T, Long]] = Option(mutable.Map.empty[T, Long])

  override def mergeValue(accOpt: Option[mutable.Map[T, Long]], next: T): Option[mutable.Map[T, Long]] = {
    accOpt.foreach(acc => addOccurrencesToMap(acc, next, 1))
    replacedWithNoneIfLimitExceeded(accOpt)
  }

  override def mergeCombiners(
      leftOpt: Option[mutable.Map[T, Long]],
      rightOpt: Option[mutable.Map[T, Long]]
  ): Option[mutable.Map[T, Long]] = {
    for {
      left     <- leftOpt
      rightMap <- rightOpt
    } {
      rightMap.foreach { case (element, count) =>
        addOccurrencesToMap(left, element, count)
      }
    }
    replacedWithNoneIfLimitExceeded(leftOpt)
  }

  private def addOccurrencesToMap(occurrences: mutable.Map[T, Long], element: T, count: Long): Unit =
    occurrences(element) = occurrences.getOrElse(element, 0L) + count

  private def replacedWithNoneIfLimitExceeded(mapOpt: Option[mutable.Map[T, Long]]): Option[mutable.Map[T, Long]] = {
    mapOpt.flatMap { map =>
      if (map.size <= limit)
        mapOpt
      else
        None
    }
  }

}
