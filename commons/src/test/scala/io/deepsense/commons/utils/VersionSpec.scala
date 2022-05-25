package io.deepsense.commons.utils

import org.scalatest.matchers.{BeMatcher, MatchResult}

import io.deepsense.commons.{StandardSpec, UnitTestSupport}


class VersionSpec extends StandardSpec with UnitTestSupport {

  "Version" should {
    "be comparable with other version" in {
      val v = Version(1, 2, 3, "")
      v should be (compatibleWith(v))

      val versionPatchLower = v.copy(fix = v.fix - 1)
      val versionPatchHigher = v.copy(fix = v.fix + 1)
      v should be (compatibleWith(versionPatchLower))
      v should be (compatibleWith(versionPatchHigher))

      val versionMinorLower = v.copy(minor = v.minor - 1)
      val versionMinorHigher = v.copy(minor = v.minor + 1)
      v should be (incompatibleWith(versionMinorLower))
      v should be (incompatibleWith(versionMinorHigher))

      val versionMajorLower = v.copy(major = v.major - 1)
      val versionMajorHigher = v.copy(major = v.major + 1)
      v should be (incompatibleWith(versionMajorLower))
      v should be (incompatibleWith(versionMajorHigher))
    }

    "be parse strings" in {
      Version("1.2.3") shouldBe Version(1, 2, 3, "")
      Version("1.2.3.4") shouldBe Version(1, 2, 3, ".4")
      Version("1.2.3.a") shouldBe Version(1, 2, 3, ".a")
      Version("1.2.3-x") shouldBe Version(1, 2, 3, "-x")
      Version("1.2.3-numberhere:1") shouldBe Version(1, 2, 3, "-numberhere:1")

      a [VersionException] shouldBe thrownBy (Version("1"))
      a [VersionException] shouldBe thrownBy (Version("1."))
      a [VersionException] shouldBe thrownBy (Version("1.2"))
      a [VersionException] shouldBe thrownBy (Version("1.2."))
      a [VersionException] shouldBe thrownBy (Version("1.2.x"))
      a [VersionException] shouldBe thrownBy (Version("1x.2.3"))
      a [VersionException] shouldBe thrownBy (Version("1.2x.3"))
      a [VersionException] shouldBe thrownBy (Version("1x.2x.3"))
      a [VersionException] shouldBe thrownBy (Version("foo"))
    }
  }

  class VersionMatcher(right: Version) extends BeMatcher[Version] {
    def apply(left: Version): MatchResult = {
      MatchResult(left.compatibleWith(right),
        s"Version '${left.humanReadable}' was compatible with '${right.humanReadable}'",
        s"Version '${left.humanReadable}' was not compatible with '${right.humanReadable}'"
      )
    }
  }

  private def compatibleWith(version: Version) = new VersionMatcher(version)
  private def incompatibleWith(version: Version) = not(new VersionMatcher(version))
}
