package ai.deepsense.deeplang.catalogs.actions

import scala.reflect.runtime.universe.TypeTag
import scala.reflect.runtime.universe.typeTag

import org.scalatestplus.mockito.MockitoSugar
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import ai.deepsense.deeplang._
import ai.deepsense.deeplang.catalogs.SortPriority
import ai.deepsense.deeplang.catalogs.actions.exceptions._
import ai.deepsense.deeplang.actionobjects.ActionObjectMock
import ai.deepsense.deeplang.actions.UnknownOperation
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings

object DOperationCatalogTestResources {

  object CategoryTree {

    object IO extends ActionCategory(ActionCategory.Id.randomId, "Input/Output", SortPriority.coreDefault)

    object DataManipulation
        extends ActionCategory(ActionCategory.Id.randomId, "Data manipulation", IO.priority.nextCore)

    object ML
        extends ActionCategory(
          ActionCategory.Id.randomId,
          "Machine learning",
          DataManipulation.priority.nextCore
        ) {

      object Classification
          extends ActionCategory(ActionCategory.Id.randomId, "Classification", ML.priority.nextCore, ML)

      object Regression
          extends ActionCategory(ActionCategory.Id.randomId, "Regression", Classification.priority.nextCore, ML)

      object Clustering
          extends ActionCategory(ActionCategory.Id.randomId, "Clustering", Regression.priority.nextCore, ML)

      object Evaluation
          extends ActionCategory(ActionCategory.Id.randomId, "Evaluation", Clustering.priority.nextCore, ML)

    }

    object Utils
        extends ActionCategory(ActionCategory.Id.randomId, "Utilities", ML.Evaluation.priority.nextCore)

  }

  abstract class ActionMock extends Action {

    override def inPortTypes: Vector[TypeTag[_]] = Vector()

    override def outPortTypes: Vector[TypeTag[_]] = Vector()

    override def inferKnowledgeUntyped(l: Vector[Knowledge[ActionObject]])(
        context: InferContext
    ): (Vector[Knowledge[ActionObject]], InferenceWarnings) = ???

    override def executeUntyped(l: Vector[ActionObject])(context: ExecutionContext): Vector[ActionObject] = ???

    override val inArity: Int = 2

    override val outArity: Int = 3

    val specificParams: Array[ai.deepsense.deeplang.parameters.Parameter[_]] = Array()

  }

  case class X() extends ActionObjectMock

  case class Y() extends ActionObjectMock

  val XTypeTag = typeTag[X]

  val YTypeTag = typeTag[Y]

  val idA = Action.Id.randomId

  val idB = Action.Id.randomId

  val idC = Action.Id.randomId

  val idD = Action.Id.randomId

  val nameA = "nameA"

  val nameB = "nameB"

  val nameC = "nameC"

  val nameD = "nameD"

  val descriptionA = "descriptionA"

  val descriptionB = "descriptionB"

  val descriptionC = "descriptionC"

  val descriptionD = "descriptionD"

  val versionA = "versionA"

  val versionB = "versionB"

  val versionC = "versionC"

  val versionD = "versionD"

  case class ActionA() extends ActionMock {

    override val id = idA

    override val name = nameA

    override val description = descriptionA

  }

  case class ActionB() extends ActionMock {

    override val id = idB

    override val name = nameB

    override val description = descriptionB

  }

  case class ActionC() extends ActionMock {

    override val id = idC

    override val name = nameC

    override val description = descriptionC

  }

  case class DOperationD() extends ActionMock {

    override val id = idD

    override val name = nameD

    override val description = descriptionD

    override val inPortTypes: Vector[TypeTag[_]] = Vector(XTypeTag, YTypeTag)

    override val outPortTypes: Vector[TypeTag[_]] = Vector(XTypeTag)

  }

}

object ViewingTestResources extends MockitoSugar {

  import DOperationCatalogTestResources._

  val categoryA = CategoryTree.ML.Regression

  val categoryB = CategoryTree.ML.Regression

  val categoryC = CategoryTree.ML.Classification

  val categoryD = CategoryTree.ML

  val catalog = ActionCatalog()

  val priorityA = SortPriority(0)

  val priorityB = SortPriority(-1)

  val priorityC = SortPriority(2)

  val priorityD = SortPriority(3)

  catalog.registerDOperation(categoryA, () => new ActionA(), priorityA)
  catalog.registerDOperation(categoryB, () => new ActionB(), priorityB)
  catalog.registerDOperation(categoryC, () => new ActionC(), priorityC)
  catalog.registerDOperation(categoryD, () => new DOperationD(), priorityD)

  val operationD = catalog.createDOperation(idD)

  val expectedA = ActionDescriptor(
    idA,
    nameA,
    descriptionA,
    categoryA,
    priorityA,
    hasDocumentation = false,
    ActionA().paramsToJson,
    Nil,
    Vector.empty,
    Nil,
    Vector.empty
  )

  val expectedB = ActionDescriptor(
    idB,
    nameB,
    descriptionB,
    categoryB,
    priorityB,
    hasDocumentation = false,
    ActionB().paramsToJson,
    Nil,
    Vector.empty,
    Nil,
    Vector.empty
  )

  val expectedC = ActionDescriptor(
    idC,
    nameC,
    descriptionC,
    categoryC,
    priorityC,
    hasDocumentation = false,
    ActionC().paramsToJson,
    Nil,
    Vector.empty,
    Nil,
    Vector.empty
  )

  val expectedD = ActionDescriptor(
    idD,
    nameD,
    descriptionD,
    categoryD,
    priorityD,
    hasDocumentation = false,
    DOperationD().paramsToJson,
    List(XTypeTag.tpe, YTypeTag.tpe),
    operationD.inPortsLayout,
    List(XTypeTag.tpe),
    operationD.outPortsLayout
  )

}

class ActionCatalogSuite extends AnyFunSuite with Matchers with MockitoSugar {

  test("It is possible to create instance of registered DOperation") {
    import DOperationCatalogTestResources._
    val catalog  = ActionCatalog()
    catalog.registerDOperation(CategoryTree.ML.Regression, () => new ActionA(), ViewingTestResources.priorityA)
    val instance = catalog.createDOperation(idA)
    assert(instance == ActionA())
  }

  test("Attempt of creating unregistered DOperation raises exception") {
    val nonExistingOperationId = Action.Id.randomId
    val exception              = intercept[DOperationNotFoundException] {
      val catalog = ActionCatalog()
      catalog.createDOperation(nonExistingOperationId)
    }
    exception.operationId shouldBe nonExistingOperationId
  }

  test("It is possible to view list of registered Actions descriptors") {
    import DOperationCatalogTestResources._
    import ViewingTestResources._

    val exceptUnknown = catalog.operations - new UnknownOperation().id

    exceptUnknown shouldBe Map(idA -> expectedA, idB -> expectedB, idC -> expectedC, idD -> expectedD)
  }

  test("SortPriority inSequence assigns values with step 100") {
    import ai.deepsense.deeplang.catalogs.actions.DOperationCatalogTestResources.CategoryTree
    CategoryTree.ML.Classification.priority shouldBe SortPriority(300)
    CategoryTree.ML.Regression.priority shouldBe SortPriority(400)
  }

  test("It is possible to get tree of registered categories and Actions") {
    import DOperationCatalogTestResources.CategoryTree._
    import ViewingTestResources._

    val root: DOperationCategoryNode = catalog.categoryTree
    root.category shouldBe None
    root.operations shouldBe empty
    root.successors.keys should contain theSameElementsAs Seq(ML)

    val mlNode = root.successors(ML)
    mlNode.category shouldBe Some(ML)
    mlNode.operations shouldBe List(expectedD)
    mlNode.successors.keys.toList shouldBe List(ML.Classification, ML.Regression)

    val regressionNode = mlNode.successors(ML.Regression)
    regressionNode.category shouldBe Some(ML.Regression)
    regressionNode.operations shouldBe List(expectedB, expectedA)
    regressionNode.successors.keys shouldBe empty

    val classificationNode = mlNode.successors(ML.Classification)
    classificationNode.category shouldBe Some(ML.Classification)
    classificationNode.operations should contain theSameElementsAs Seq(expectedC)
    classificationNode.successors.keys shouldBe empty
  }

}
