package io.deepsense.commons

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

/** Standard base class for tests. Includes the following features:
  *
  *   - WordSpec style tests with Matcher DSL for assertions
  *
  *   - Support for testing Futures including the useful whenReady construct
  */
class StandardSpec extends AnyWordSpec with Matchers with ScalaFutures
