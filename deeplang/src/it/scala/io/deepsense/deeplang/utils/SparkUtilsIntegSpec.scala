package io.deepsense.deeplang.utils

import io.deepsense.deeplang.DeeplangIntegTestSupport

class SparkUtilsIntegSpec extends DeeplangIntegTestSupport {

  "countOccurrencesWithKeyLimit operation" should {

    "Return Some(result) if distinct value limit is not reached" in {
      val data           = Seq("A", "B", "C")
      val satisfiedLimit = 3
      val result         = execute(data, satisfiedLimit)
      result shouldBe defined
    }

    "Return None if distinct value limit is reached" in {
      val data             = Seq("A", "B", "C")
      val unsatisfiedLimit = 2
      val result           = execute(data, unsatisfiedLimit)
      result should not be defined
    }

    "Properly calculate amount of occurrences" in {
      val data   = Seq("A", "A", "A", "A", "A", "B", "B")
      val result = execute(data, 3)
      result shouldBe Some(Map("A" -> 5, "B" -> 2))
    }

  }

  private def execute(data: Seq[String], limit: Int) = {
    val rdd = sparkContext.parallelize(data)
    SparkUtils.countOccurrencesWithKeyLimit(rdd, limit)
  }

}
