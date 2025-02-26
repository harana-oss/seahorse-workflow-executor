package ai.deepsense.graph

import ai.deepsense.deeplang.exceptions.FlowException
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.inference.exceptions.AllTypesNotCompilableException
import ai.deepsense.deeplang.inference.exceptions.NoInputEdgesException
import ai.deepsense.deeplang.inference.warnings.SomeTypesNotCompilableWarning
import ai.deepsense.deeplang.Knowledge
import ai.deepsense.deeplang.ActionObject
import ai.deepsense.graph.GraphKnowledge.InferenceErrors

/** Represents inferred information about Graph which is send and used by front-end. It contains mapping from nodes to
  * their inferred knowledge on output ports, as well as inferred warnings and errors.
  */
case class GraphKnowledge(private[graph] val resultsMap: Map[Node.Id, NodeInferenceResult]) {

  def addInference(id: Node.Id, inferenceResult: NodeInferenceResult): GraphKnowledge =
    GraphKnowledge(resultsMap + (id -> inferenceResult))

  def addInference(other: GraphKnowledge): GraphKnowledge = {
    require(
      this.resultsMap.keySet.intersect(other.resultsMap.keySet).isEmpty,
      "Each summed graph knowledge object must describe " +
        "different set of nodes so there is no ambiguity"
    )

    GraphKnowledge(this.resultsMap ++ other.resultsMap)
  }

  def containsNodeKnowledge(id: Node.Id): Boolean = resultsMap.contains(id)

  def getResult(id: Node.Id): NodeInferenceResult = resultsMap(id)

  def getKnowledge(id: Node.Id): Vector[Knowledge[ActionObject]] = getResult(id).ports

  def results: Map[Node.Id, NodeInferenceResult] = resultsMap

  /** Map from node ids to their errors. Contains only nodes that have errors. */
  lazy val errors: Map[Node.Id, InferenceErrors] = {
    val pairs = for {
      (nodeId, result) <- resultsMap
      errors            = result.errors
      if errors.nonEmpty
    } yield (nodeId, errors)
    pairs.toMap
  }

}

object GraphKnowledge {

  def apply(): GraphKnowledge = GraphKnowledge(Map.empty)

  type InferenceErrors = Vector[FlowException]

}

case class NodeInferenceResult(
                                ports: Vector[Knowledge[ActionObject]],
                                warnings: InferenceWarnings = InferenceWarnings.empty,
                                errors: InferenceErrors = Vector.empty
)

object NodeInferenceResult {

  def empty: NodeInferenceResult = NodeInferenceResult(Vector.empty)

}

/** Represents degree of accordance of given DKnowledge with an input port of some operation. DKnowledge tells about
  * objects that can be potentially put into a port. This port has a type qualifier. Thus, we can consider following
  * cases: * all types of objects in DKnowledge can be put into port * not all, but some types of objects can be put
  * into port * none of types can be put into port * DKnowledge was not provided for the port In each of this cases
  * different errors and warnings can be returned.
  */
object TypesAccordance {

  trait TypesAccordance {

    def errors: InferenceErrors = Vector.empty

    def warnings: InferenceWarnings = InferenceWarnings.empty

  }

  /** All of types injected to port meets this port's type qualifier requirements. */
  case class All() extends TypesAccordance

  /** Some (but not all) of types injected to port meets this port's type qualifier requirements. */
  case class Some(portIndex: Int) extends TypesAccordance {

    override def warnings: InferenceWarnings =
      InferenceWarnings(SomeTypesNotCompilableWarning(portIndex))

  }

  /** None of types injected to port meets this port's type qualifier requirements. */
  case class None(portIndex: Int) extends TypesAccordance {

    override def errors: InferenceErrors = Vector(AllTypesNotCompilableException(portIndex))

  }

  /** No knowledge was provided to port. */
  case class NotProvided(portIndex: Int) extends TypesAccordance {

    override def errors: InferenceErrors = Vector(NoInputEdgesException(portIndex))

  }

}
