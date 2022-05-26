package ai.deepsense.models.json.graph

import org.mockito.Mockito._
import org.scalatestplus.mockito.MockitoSugar
import org.scalatest.matchers.should.Matchers
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import spray.json.DefaultJsonProtocol
import spray.json.JsObject

import ai.deepsense.deeplang.DOperation
import ai.deepsense.graph.Endpoint

trait GraphJsonTestSupport extends AnyWordSpec with MockitoSugar with DefaultJsonProtocol with Matchers {

  def assertEndpointMatchesJsObject(edgeEnd: Endpoint, edgeEndJs: JsObject): Unit = {
    assert(edgeEndJs.fields("nodeId").convertTo[String] == edgeEnd.nodeId.value.toString)
    assert(edgeEndJs.fields("portIndex").convertTo[Int] == edgeEnd.portIndex)
  }

  def endpointMatchesJsObject(edgeEnd: Endpoint, edgeEndJs: JsObject): Boolean =
    edgeEndJs.fields("nodeId").convertTo[String] == edgeEnd.nodeId.value.toString &&
      edgeEndJs.fields("portIndex").convertTo[Int] == edgeEnd.portIndex

  def mockOperation(inArity: Int, outArity: Int, id: DOperation.Id, name: String): DOperation = {

    val dOperation = mock[DOperation]
    when(dOperation.inArity).thenReturn(inArity)
    when(dOperation.outArity).thenReturn(outArity)
    when(dOperation.id).thenReturn(id)
    when(dOperation.name).thenReturn(name)
    dOperation
  }

}
