package io.deepsense.deeplang

import org.scalatest._
import org.scalatestplus.mockito.MockitoSugar
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

/** Base class for unit tests as advised: http://www.scalatest.org/user_guide/defining_base_classes */
abstract class UnitSpec
    extends AnyWordSpec
    with Matchers
    with OptionValues
    with Inside
    with Inspectors
    with MockitoSugar
