package ai.deepsense.deeplang.parameters

import spray.json.JsValue

import ai.deepsense.deeplang.UnitSpec
import ParameterType.ParameterType
import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphReader

class ParameterMapSpec extends UnitSpec {

  class MockParameter[T] extends Parameter[T] {

    // cannot use mockito, because asInstanceOf[Any] won't work
    override val name: String = "name"

    override val description: Option[String] = Some("description")

    override val parameterType: ParameterType = mock[ParameterType]

    override def valueToJson(value: T): JsValue = ???

    override def valueFromJson(jsValue: JsValue, graphReader: GraphReader): T = ???

    override def replicate(name: String): MockParameter[T] = new MockParameter[T]

  }

  val intParam = new MockParameter[Int]

  val intValue = 5

  val intParamPair = ParamPair(intParam, intValue)

  val stringParam = new MockParameter[String]

  val stringValue = "abc"

  val stringParamPair = ParamPair(stringParam, stringValue)

  val paramPairs = Seq(intParamPair, stringParamPair)

  "ParamMap".can {
    "be built from ParamPairs" in {
      val paramMap = ParameterMap(paramPairs: _*)
      ParameterMap().put(stringParamPair, intParamPair) shouldBe paramMap
      ParameterMap().put(intParam, intValue).put(stringParam, stringValue) shouldBe paramMap
    }
    "be merged with other param map" in {
      val map1 = ParameterMap(paramPairs(0))
      val map2 = ParameterMap(paramPairs(1))
      map1 ++ map2 shouldBe ParameterMap(paramPairs: _*)
    }
    "be updated with other param map" in {
      val map1 = ParameterMap(paramPairs(0))
      val map2 = ParameterMap(paramPairs(1))
      map1 ++= map2
      map1 shouldBe ParameterMap(paramPairs: _*)
    }
    "return sequence of included ParamPairs" in {
      val paramMap = ParameterMap(paramPairs: _*)
      paramMap.toSeq should contain theSameElementsAs paramPairs
    }
  }

  private def mapWithIntParam = ParameterMap(intParamPair)

  "ParamMap.put" should {
    "update value of param if it is already defined" in {
      val map      = mapWithIntParam
      val newValue = 7
      map.put(intParam, newValue)
      map shouldBe ParameterMap().put(intParam, newValue)
    }
  }
  "ParamMap.get" should {
    "return Some value" when {
      "param has value assigned" in {
        mapWithIntParam.get(intParam) shouldBe Some(intValue)
      }
    }
    "return None" when {
      "param has no value assigned" in {
        mapWithIntParam.get(stringParam) shouldBe None
      }
    }
  }

  "ParamMap.getOrElse" should {
    "return value" when {
      "param has value assigned" in {
        mapWithIntParam.getOrElse(intParam, 7) shouldBe intValue
      }
    }
    "return provided default" when {
      "param has no value assigned" in {
        val default = "xxx"
        mapWithIntParam.getOrElse(stringParam, default) shouldBe default
      }
    }
  }

  "ParamMap.apply" should {
    "return value" when {
      "param has value assigned" in {
        mapWithIntParam(intParam) shouldBe intValue
      }
    }
    "throw an Exception" when {
      "param has no value assigned" in {
        a[NoSuchElementException] shouldBe thrownBy {
          mapWithIntParam(stringParam)
        }
      }
    }
  }

  "ParamMap.contains" should {
    "return true" when {
      "param has value assigned" in {
        mapWithIntParam.contains(intParam) shouldBe true
      }
    }
    "return false" when {
      "param has no value assigned" in {
        mapWithIntParam.contains(stringParam) shouldBe false
      }
    }
  }

  "ParamMap.remove" should {
    "remove value and return it" when {
      "param has value assigned" in {
        val map = mapWithIntParam
        map.remove(intParam) shouldBe Some(intValue)
        map shouldBe ParameterMap.empty
      }
    }
    "return None" when {
      "param has no value assigned" in {
        mapWithIntParam.remove(stringParam) shouldBe None
      }
    }
  }

}
