package io.deepsense.deeplang.doperables.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml
import org.apache.spark.ml.param.{Param => SparkParam}

import io.deepsense.deeplang.params.choice.Choice
import io.deepsense.deeplang.params.wrappers.spark.ChoiceParamWrapper
import io.deepsense.deeplang.params.Param
import io.deepsense.deeplang.params.Params

trait HasSolverParam extends Params {

  val solver =
    new ChoiceParamWrapper[ml.param.Params { val solver: SparkParam[String] }, SolverChoice.SolverOption](
      name = "solver",
      sparkParamGetter = _.solver,
      description = Some("""Sets the solver algorithm used for optimization.
                           |Can be set to "l-bfgs", "normal" or "auto".
                           |"l-bfgs" denotes Limited-memory BFGS which is a limited-memory quasi-Newton
                           |optimization method. "normal" denotes Normal Equation. It is an analytical
                           |solution to the linear regression problem.
                           |The default value is "auto" which means that the solver algorithm is
                           |selected automatically.""".stripMargin)
    )

  setDefault(solver, SolverChoice.Auto())

}

object SolverChoice {

  sealed abstract class SolverOption(override val name: String) extends Choice {

    override val params: Array[Param[_]] = Array()

    override val choiceOrder: List[Class[_ <: SolverOption]] = List(
      classOf[Auto],
      classOf[Normal],
      classOf[LBFGS]
    )

  }

  case class Auto() extends SolverOption("auto")

  case class Normal() extends SolverOption("normal")

  case class LBFGS() extends SolverOption("l-bfgs")

}
