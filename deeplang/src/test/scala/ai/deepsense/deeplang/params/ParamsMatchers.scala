package ai.deepsense.deeplang.params

import org.scalatest.matchers.HavePropertyMatchResult
import org.scalatest.matchers.HavePropertyMatcher

object ParamsMatchers {

  def theSameParamsAs(right: Params): HavePropertyMatcher[Params, Params] =
    new HavePropertyMatcher[Params, Params] {

      def apply(left: Params) = HavePropertyMatchResult(
        left.sameAs(right),
        "param values",
        right,
        left
      )

    }

}
