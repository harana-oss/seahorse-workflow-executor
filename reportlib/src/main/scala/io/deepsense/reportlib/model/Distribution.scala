package io.deepsense.reportlib.model

sealed abstract class Distribution(
    val name: String,
    val subtype: String,
    val description: String,
    val missingValues: Long
)

case class NoDistribution(override val name: String, override val description: String)
    extends Distribution(name, NoDistribution.subtype, description, 0)

object NoDistribution {

  val subtype = "no_distribution"

}

sealed abstract class UnivariateDistribution(
    name: String,
    subtype: String,
    description: String,
    missingValues: Long,
    buckets: Seq[String],
    counts: Seq[Long]
) extends Distribution(name, subtype, description, missingValues)

case class DiscreteDistribution(
    override val name: String,
    override val description: String,
    override val missingValues: Long,
    categories: Seq[String],
    counts: Seq[Long]
) extends UnivariateDistribution(name, DiscreteDistribution.subtype, description, missingValues, categories, counts) {

  require(
    categories.size == counts.size,
    "buckets size does not match count size. " +
      s"Buckets size is: ${categories.size}, counts size is: ${counts.size}"
  )

}

object DiscreteDistribution {

  val subtype = "discrete"

}

case class ContinuousDistribution(
    override val name: String,
    override val description: String,
    override val missingValues: Long,
    buckets: Seq[String],
    counts: Seq[Long],
    statistics: Statistics
) extends UnivariateDistribution(name, ContinuousDistribution.subtype, description, missingValues, buckets, counts)
    with ReportJsonProtocol {

  require(buckets.size > 1, "Buckets size must be larger than 1")

  require(
    buckets.size == counts.size + 1,
    "Buckets size should be equal to count size + 1. " +
      s"Buckets size is: ${buckets.size}, counts size is: ${counts.size}"
  )

}

object ContinuousDistribution {

  val subtype = "continuous"

}

case class Statistics(max: Option[String], min: Option[String], mean: Option[String])

object Statistics {

  def apply(): Statistics = new Statistics(None, None, None)

  def apply(max: String, min: String, mean: String): Statistics =
    Statistics(Option(max), Option(min), Option(mean))

}
