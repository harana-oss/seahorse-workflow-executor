package io.deepsense.deeplang.utils.aggregators

import scala.reflect.ClassTag

import org.apache.spark.rdd.RDD

import io.deepsense.deeplang.utils.aggregators.Aggregator.TransformedInputAggregator

trait Aggregator[U, T] {
  // Aggregators are send to nodes therefore it must be serializable.
  self: Serializable =>

  def execute(rdd: RDD[T])(implicit clazz: ClassTag[U]): U = {
    rdd.treeAggregate(initialElement)(mergeValue, mergeCombiners)
  }

  def initialElement: U

  /**
   * Merges elements of type T across one partition in element of type U.
   * Argument `acc` might be mutated and returned for better performance.
   */
  def mergeValue(acc: U, elem: T): U

  /**
   * Merges each partitions `seq` results into one U.
   * Argument `left` might be mutated and returned for better performance.
   */
  def mergeCombiners(left: U, right: U): U

  def mapInput[I](f: I => T): Aggregator[U, I] = TransformedInputAggregator(this, f)

}

object Aggregator {

  private [Aggregator] case class TransformedInputAggregator[I, U, T](
      aggregator: Aggregator[U, T],
      inputTransform: I => T)
    extends Aggregator[U, I] {

    override def initialElement: U = aggregator.initialElement

    override def mergeValue(acc: U, elem: I): U = {
      aggregator.mergeValue(acc, inputTransform(elem))
    }

    override def mergeCombiners(left: U, right: U): U = {
      aggregator.mergeCombiners(left, right)
    }

  }

}
