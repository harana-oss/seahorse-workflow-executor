package io.deepsense.models.json.workflow

import org.apache.spark.sql.types._
import org.joda.time.DateTime
import org.mockito.Mockito._
import spray.json._

import io.deepsense.commons.models.Entity
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperables.descriptions.ParamsInferenceResult
import io.deepsense.deeplang.doperables.descriptions.DataFrameInferenceResult
import io.deepsense.deeplang.exceptions.DeepLangException
import io.deepsense.deeplang.inference.InferenceWarning
import io.deepsense.deeplang.inference.InferenceWarnings
import io.deepsense.deeplang.params.Params
import io.deepsense.deeplang.DKnowledge
import io.deepsense.deeplang.DOperable
import io.deepsense.graph.GraphKnowledge
import io.deepsense.graph.NodeInferenceResult
import io.deepsense.models.workflows._

class InferredStateJsonProtocolSpec extends WorkflowJsonTestSupport with InferredStateJsonProtocol {

  "InferredState" should {
    "be serializable to json" in {
      val (inferredState, json) = inferredStateFixture
      inferredState.toJson shouldBe json
    }
  }

  def inferredStateFixture: (InferredState, JsObject) = {
    val workflowId                           = Workflow.Id.randomId
    val (graphKnowledge, graphKnowledgeJson) = graphKnowledgeFixture

    val (executionStates, statesJson) = executionStatesFixture

    val workflow = InferredState(workflowId, graphKnowledge, executionStates)

    val workflowJson = JsObject(
      "id"        -> JsString(workflowId.toString),
      "knowledge" -> graphKnowledgeJson,
      "states"    -> statesJson
    )
    (workflow, workflowJson)
  }

  def graphKnowledgeFixture: (GraphKnowledge, JsObject) = {
    val parametricOperable    = mock[ParametricOperable]("ParametricOperable")
    val paramSchema: JsString = JsString("Js with ParamSchema")
    val paramValues: JsString = JsString("Js with ParamValues")
    when(parametricOperable.inferenceResult).thenReturn(
      Some(ParamsInferenceResult(paramSchema, paramValues))
    )

    val dataFrame = mock[DataFrame]
    val meta      = new MetadataBuilder().putString("someKey", "someValue").build()
    val dataFrameDescription = DataFrameInferenceResult(
      StructType(
        Seq(
          StructField("col1", StringType, nullable = true),
          StructField("col2", DoubleType, nullable = false, metadata = meta)
        )
      )
    )
    when(dataFrame.inferenceResult).thenReturn(Some(dataFrameDescription))

    val graphKnowledge = GraphKnowledge().addInference(
      node1.id,
      NodeInferenceResult(
        Vector(
          DKnowledge(Set(operable)),
          DKnowledge(Set(operable, parametricOperable)),
          DKnowledge(Set[DOperable](parametricOperable)),
          DKnowledge(Set[DOperable](dataFrame))
        ),
        InferenceWarnings(new InferenceWarning("warning1") {}, new InferenceWarning("warning2") {}),
        Vector(
          new DeepLangException("error1") {},
          new DeepLangException("error2") {}
        )
      )
    )

    def dOperableJsName(o: DOperable): JsString = JsString(o.getClass.getCanonicalName)
    val mockOperableName                        = dOperableJsName(operable)
    val parametricOperableName                  = dOperableJsName(parametricOperable)
    val dataFrameName                           = dOperableJsName(dataFrame)

    val knowledgeJson = JsObject(
      node1.id.toString -> JsObject(
        "ports" -> JsArray(
          JsObject(
            "types"  -> JsArray(mockOperableName),
            "result" -> JsNull
          ),
          JsObject(
            "types"  -> JsArray(mockOperableName, parametricOperableName),
            "result" -> JsNull
          ),
          JsObject(
            "types" -> JsArray(parametricOperableName),
            "result" -> JsObject(
              "params" -> JsObject(
                "schema" -> paramSchema,
                "values" -> paramValues
              )
            )
          ),
          JsObject(
            "types" -> JsArray(dataFrameName),
            "result" -> JsObject(
              "schema" -> JsObject(
                "fields" -> JsArray(
                  JsObject(
                    "name"         -> JsString("col1"),
                    "dataType"     -> JsString("string"),
                    "deeplangType" -> JsString("string"),
                    "nullable"     -> JsTrue
                  ),
                  JsObject(
                    "name"         -> JsString("col2"),
                    "dataType"     -> JsString("double"),
                    "deeplangType" -> JsString("numeric"),
                    "nullable"     -> JsFalse
                  )
                )
              )
            )
          )
        ),
        "warnings" -> JsArray(
          JsString("warning1"),
          JsString("warning2")
        ),
        "errors" -> JsArray(
          JsString("error1"),
          JsString("error2")
        )
      )
    )

    (graphKnowledge, knowledgeJson)
  }

  def executionStatesFixture: (ExecutionReport, JsObject) = {

    val startTimestamp  = "2015-05-12T21:11:09.000Z"
    val finishTimestamp = "2015-05-12T21:12:50.000Z"

    val entity1Id = Entity.Id.randomId
    val entity2Id = Entity.Id.randomId

    val executionStates = ExecutionReport.statesOnly(
      Map(
        node1.id -> io.deepsense.graph.nodestate.Completed(
          DateTime.parse(startTimestamp),
          DateTime.parse(finishTimestamp),
          Seq(entity1Id, entity2Id)
        )
      ),
      None
    )
    val executionStatesJson = JsObject(
      "error" -> JsNull,
      "nodes" -> JsObject(
        node1.id.toString -> JsObject(
          "status"  -> JsString("COMPLETED"),
          "started" -> JsString(startTimestamp),
          "ended"   -> JsString(finishTimestamp),
          "results" -> JsArray(
            JsString(entity1Id.toString),
            JsString(entity2Id.toString)
          ),
          "error" -> JsNull
        )
      ),
      "resultEntities" -> JsObject()
    )

    (executionStates, executionStatesJson)
  }

  abstract class ParametricOperable extends DOperable with Params

}
