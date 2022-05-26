package ai.deepsense.commons

import org.scalatest.mockito.MockitoSugar

/** Extends StandardSpec with features to aid unit testing including:
  *   - Support for mockito mocking
  *   - Defines implicits required for future, akka and spray route testing
  */
trait UnitTestSupport extends MockitoSugar {
  suite: StandardSpec =>
}
