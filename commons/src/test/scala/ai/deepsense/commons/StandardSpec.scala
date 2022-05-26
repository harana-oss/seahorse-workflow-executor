package ai.deepsense.commons

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.Matchers
import org.scalatest.WordSpec

/** Standard base class for tests. Includes the following features:
  *
  *   - WordSpec style tests with Matcher DSL for assertions
  *
  *   - Support for testing Futures including the useful whenReady construct
  */
class StandardSpec extends WordSpec with Matchers with ScalaFutures
