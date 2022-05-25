package io.deepsense.commons.utils

import io.deepsense.commons.StandardSpec

class CollectionExtensionsSpec extends StandardSpec {

  "Seq with RichSeq conversion" should {
    import CollectionExtensions._

    "tell that it has unique value" in {
      val s = Seq(1, 3, 2)
      s.hasUniqueValues shouldBe true
      s.hasDuplicates shouldBe false
    }
    "tell that it has duplicates" in {
      val s = Seq(1, 3, 2, 3)
      s.hasUniqueValues shouldBe false
      s.hasDuplicates shouldBe true
    }
  }
}
