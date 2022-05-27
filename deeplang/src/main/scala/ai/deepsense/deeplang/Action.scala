package ai.deepsense.deeplang

import scala.reflect.runtime.{universe => ru}
import java.util.UUID

import ai.deepsense.commons.models
import ai.deepsense.commons.utils.CollectionExtensions
import ai.deepsense.commons.utils.Logging
import ai.deepsense.deeplang.Action.ReportParam
import ai.deepsense.deeplang.Action.ReportType
import ai.deepsense.deeplang.PortPosition.PortPosition
import ai.deepsense.deeplang.catalogs.actions.ActionCategory
import ai.deepsense.deeplang.documentation.OperationDocumentation
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.choice.Choice
import ai.deepsense.deeplang.parameters.choice.ChoiceParameter
import ai.deepsense.graph.GraphKnowledge
import ai.deepsense.graph.Operation

/** Action that receives and returns instances of ActionObject. Can infer its output type based on type knowledge. */
@SerialVersionUID(1L)
abstract class Action extends Operation with Serializable with Logging with Params {

  import CollectionExtensions._

  val inArity: Int

  val outArity: Int

  val id: Action.Id

  val name: String

  val description: String

  def specificParams: Array[Parameter[_]]

  def reportTypeParam: Option[Parameter[_]] = Option(reportType).filter(_ => outArity != 0)

  def params: Array[Parameter[_]] = specificParams ++ reportTypeParam

  def hasDocumentation: Boolean = false

  def inPortTypes: Vector[ru.TypeTag[_]]

  def inPortsLayout: Vector[PortPosition] = defaultPortLayout(inPortTypes, GravitateLeft)

  def outPortTypes: Vector[ru.TypeTag[_]]

  def outPortsLayout: Vector[PortPosition] = defaultPortLayout(outPortTypes, GravitateRight)

  def getDatasourcesIds: Set[UUID] = Set[UUID]()

  private def defaultPortLayout(portTypes: Vector[ru.TypeTag[_]], gravity: Gravity): Vector[PortPosition] = {
    import PortPosition._
    portTypes.size match {
      case 0     => Vector.empty
      case 1     => Vector(Center)
      case 2     =>
        gravity match {
          case GravitateLeft  => Vector(Left, Center)
          case GravitateRight => Vector(Center, Right)
        }
      case 3     => Vector(Left, Center, Right)
      case other => throw new IllegalStateException(s"Unsupported number of output ports: $other")
    }
  }

  def validate(): Unit = {
    require(outPortsLayout.size == outPortTypes.size, "Every output port must be laid out")
    require(!outPortsLayout.hasDuplicates, "Output port positions must be unique")
    require(inPortsLayout.size == inPortTypes.size, "Every input port must be laid out")
    require(!inPortsLayout.hasDuplicates, "Input port positions must be unique")
    require(inPortsLayout.isSorted, "Input ports must be laid out from left to right")
    require(outPortsLayout.isSorted, "Output ports must be laid out from left to right")
  }

  def executeUntyped(l: Vector[ActionObject])(context: ExecutionContext): Vector[ActionObject]

  /** Infers knowledge for this operation.
    *
    * @param context
    *   Infer context to be used in inference.
    * @param inputKnowledge
    *   Vector of knowledge objects to be put in input ports of this operation. This method assumes that size of this
    *   vector is equal to [[inArity]].
    * @return
    *   A tuple consisting of:
    *   - vector of knowledge object for each of operation's output port
    *   - inference warnings for this operation
    */
  def inferKnowledgeUntyped(inputKnowledge: Vector[Knowledge[ActionObject]])(
      context: InferContext
  ): (Vector[Knowledge[ActionObject]], InferenceWarnings)

  def inferGraphKnowledgeForInnerWorkflow(context: InferContext): GraphKnowledge = GraphKnowledge()

  def typeTag[T: ru.TypeTag]: ru.TypeTag[T] = ru.typeTag[T]

  val reportType: ChoiceParameter[ReportType] = ChoiceParameter[ReportType](
    name = "report type",
    description = Some("Output entities report type. Computing extended report can be time consuming.")
  )

  setDefault(reportType, ReportParam.Extended())

  def getReportType: ReportType = $(reportType)

  def setReportType(value: ReportType): this.type = set(reportType, value)

}

object Action {

  type Id = models.Id

  val Id = models.Id

  object ReportParam {

    case class Metadata() extends ReportType {

      override val name: String = "Metadata report"

      override val params: Array[Parameter[_]] = Array()

    }

    case class Extended() extends ReportType {

      override val name: String = "Extended report"

      override val params: Array[Parameter[_]] = Array()

    }

  }

  sealed trait ReportType extends Choice {

    import ai.deepsense.deeplang.Action.ReportParam.Extended
    import ai.deepsense.deeplang.Action.ReportParam.Metadata

    override val choiceOrder: List[Class[_ <: Choice]] = List(classOf[Metadata], classOf[Extended])

  }

}
