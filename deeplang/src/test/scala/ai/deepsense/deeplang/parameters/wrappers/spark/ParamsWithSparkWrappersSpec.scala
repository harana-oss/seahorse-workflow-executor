package ai.deepsense.deeplang.parameters.wrappers.spark

import org.apache.spark.ml
import org.apache.spark.ml.param._
import org.apache.spark.sql.types.StructType
import org.scalatestplus.mockito.MockitoSugar
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import ai.deepsense.deeplang.parameters.BooleanParameter
import ai.deepsense.deeplang.parameters.choice.ChoiceParameter
import ai.deepsense.deeplang.parameters.choice.Choice

class ParamsWithSparkWrappersSpec extends AnyWordSpec with Matchers with MockitoSugar {

  import ParamsWithSparkWrappersSpec._

  "ParamsWithSparkWrappers" should {
    "calculate sparkParamWrappers" in {
      val paramsWithSparkWrappers = ParamsWithSparkWrappersClass()
      paramsWithSparkWrappers.sparkParamWrappers shouldBe
        Array(paramsWithSparkWrappers.paramA, paramsWithSparkWrappers.paramB)
    }
    "return parameter values" in {
      val paramsWithSparkWrappers = ParamsWithSparkWrappersClass().setParamA("a").setParamB(0.0)
      paramsWithSparkWrappers
        .sparkParamMap(paramsWithSparkWrappers.exampleSparkParams, StructType(Seq()))
        .toSeq
        .toSet shouldBe
        Set(
          paramsWithSparkWrappers.exampleSparkParams.sparkParamA -> "a",
          paramsWithSparkWrappers.exampleSparkParams.sparkParamB -> 0
        )
    }
    "return wrappers nested in choice parameter values" in {
      val paramsWithSparkWrappers = ParamsWithSparkWrappersClass()
        .setChoice(OneParamChoiceWithWrappers().setParamC("c"))
      paramsWithSparkWrappers
        .sparkParamMap(paramsWithSparkWrappers.exampleSparkParams, StructType(Seq()))
        .toSeq
        .toSet shouldBe
        Set(paramsWithSparkWrappers.exampleSparkParams.sparkParamC -> "c")
    }
  }

}

object ParamsWithSparkWrappersSpec {

  class ExampleSparkParams extends ml.param.Params {

    override val uid: String = "id"

    val sparkParamA = new Param[String]("", "paramA", "descA")

    val sparkParamB = new IntParam("", "paramB", "descB")

    val sparkParamC = new Param[String]("", "paramC", "descC")

    override def copy(extra: ParamMap): Params = ???

  }

  case class ParamsWithSparkWrappersClass() extends ParamsWithSparkWrappers {

    val exampleSparkParams = new ExampleSparkParams

    val paramA = new StringParameterWrapper[ExampleSparkParams]("paramA", Some("descA"), _.sparkParamA)

    val paramB = new IntParameterWrapper[ExampleSparkParams]("paramB", Some("descB"), _.sparkParamB)

    val choiceWithParamsInValues = new ChoiceParameter[ChoiceWithWrappers]("choice", Some("descChoice"))

    val notWrappedParam = BooleanParameter("booleanParamName", Some("booleanParamDescription"))

    val params: Array[ai.deepsense.deeplang.parameters.Parameter[_]] =
      Array(paramA, paramB, choiceWithParamsInValues, notWrappedParam)

    def setParamA(v: String): this.type = set(paramA, v)

    def setParamB(v: Double): this.type = set(paramB, v)

    def setChoice(v: ChoiceWithWrappers): this.type = set(choiceWithParamsInValues, v)

  }

  sealed trait ChoiceWithWrappers extends Choice with ParamsWithSparkWrappers {

    override val choiceOrder: List[Class[_ <: ChoiceWithWrappers]] =
      List(classOf[OneParamChoiceWithWrappers], classOf[EmptyChoiceWithWrappers])

  }

  case class OneParamChoiceWithWrappers() extends ChoiceWithWrappers {

    val paramC = new StringParameterWrapper[ExampleSparkParams]("paramC", Some("descC"), _.sparkParamC)

    def setParamC(v: String): this.type = set(paramC, v)

    override val name = "one param"

    val params: Array[ai.deepsense.deeplang.parameters.Parameter[_]] = Array(paramC)

  }

  case class EmptyChoiceWithWrappers() extends ChoiceWithWrappers {

    override val name = "no params"

    val params: Array[ai.deepsense.deeplang.parameters.Parameter[_]] = Array()

  }

}
