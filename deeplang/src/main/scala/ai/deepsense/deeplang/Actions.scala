package ai.deepsense.deeplang

import scala.reflect.runtime.{universe => ru}

import ai.deepsense.deeplang.ToVectorConversions._
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings

/** Implicit conversions used to convert results of execute and inferKnowledge to Vectors */
private object ToVectorConversions {

  implicit def singleValueToVector[T1 <: ActionObject](t: T1): Vector[ActionObject] =
    Vector(t)

  implicit def tuple2ToVector[T1 <: ActionObject, T2 <: ActionObject](t: (T1, T2)): Vector[ActionObject] =
    Vector(t._1, t._2)

  implicit def tuple3ToVector[T1 <: ActionObject, T2 <: ActionObject, T3 <: ActionObject](t: (T1, T2, T3)): Vector[ActionObject] =
    Vector(t._1, t._2, t._3)

  implicit def dKnowledgeSingletonToVector[T1 <: ActionObject](
      t: (Knowledge[T1], InferenceWarnings)
  ): (Vector[Knowledge[ActionObject]], InferenceWarnings) =
    (Vector(t._1), t._2)

  implicit def dKnowledgeTuple2ToVector[T1 <: ActionObject, T2 <: ActionObject](
      t: ((Knowledge[T1], Knowledge[T2]), InferenceWarnings)
  ): (Vector[Knowledge[ActionObject]], InferenceWarnings) = {
    val (k, w) = t
    (Vector(k._1, k._2), w)
  }

  implicit def dKnowledgeTuple3ToVector[T1 <: ActionObject, T2 <: ActionObject, T3 <: ActionObject](
      t: ((Knowledge[T1], Knowledge[T2], Knowledge[T3]), InferenceWarnings)
  ): (Vector[Knowledge[ActionObject]], InferenceWarnings) = {
    val (k, w) = t
    (Vector(k._1, k._2, k._3), w)
  }

}

/** Following classes are generated automatically. */
// scalastyle:off
abstract class Action0To1[TO_0 <: ActionObject] extends Action {

  final override val inArity = 0

  final override val outArity = 1

  def tTagTO_0: ru.TypeTag[TO_0]

  @transient
  final override lazy val inPortTypes: Vector[ru.TypeTag[_]] = Vector()

  @transient
  final override lazy val outPortTypes: Vector[ru.TypeTag[_]] = Vector(ru.typeTag[TO_0](tTagTO_0))

  final override def executeUntyped(arguments: Vector[ActionObject])(context: ExecutionContext): Vector[ActionObject] =
    execute()(context)

  final override def inferKnowledgeUntyped(knowledge: Vector[Knowledge[ActionObject]])(
      context: InferContext
  ): (Vector[Knowledge[ActionObject]], InferenceWarnings) =
    inferKnowledge()(context)

  protected def execute()(context: ExecutionContext): TO_0

  protected def inferKnowledge()(context: InferContext): (Knowledge[TO_0], InferenceWarnings) =
    (Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0](tTagTO_0)), InferenceWarnings.empty)

}

abstract class Action0To2[TO_0 <: ActionObject, TO_1 <: ActionObject] extends Action {

  final override val inArity = 0

  final override val outArity = 2

  def tTagTO_0: ru.TypeTag[TO_0]

  def tTagTO_1: ru.TypeTag[TO_1]

  @transient
  final override lazy val inPortTypes: Vector[ru.TypeTag[_]] = Vector()

  @transient
  final override lazy val outPortTypes: Vector[ru.TypeTag[_]] =
    Vector(ru.typeTag[TO_0](tTagTO_0), ru.typeTag[TO_1](tTagTO_1))

  final override def executeUntyped(arguments: Vector[ActionObject])(context: ExecutionContext): Vector[ActionObject] =
    execute()(context)

  final override def inferKnowledgeUntyped(knowledge: Vector[Knowledge[ActionObject]])(
      context: InferContext
  ): (Vector[Knowledge[ActionObject]], InferenceWarnings) =
    inferKnowledge()(context)

  protected def execute()(context: ExecutionContext): (TO_0, TO_1)

  protected def inferKnowledge()(context: InferContext): ((Knowledge[TO_0], Knowledge[TO_1]), InferenceWarnings) = {
    (
      (
        Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0](tTagTO_0)),
        Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_1](tTagTO_1))
      ),
      InferenceWarnings.empty
    )
  }

}

abstract class Action0To3[TO_0 <: ActionObject, TO_1 <: ActionObject, TO_2 <: ActionObject] extends Action {

  final override val inArity = 0

  final override val outArity = 3

  def tTagTO_0: ru.TypeTag[TO_0]

  def tTagTO_1: ru.TypeTag[TO_1]

  def tTagTO_2: ru.TypeTag[TO_2]

  @transient
  final override lazy val inPortTypes: Vector[ru.TypeTag[_]] = Vector()

  @transient
  final override lazy val outPortTypes: Vector[ru.TypeTag[_]] =
    Vector(ru.typeTag[TO_0](tTagTO_0), ru.typeTag[TO_1](tTagTO_1), ru.typeTag[TO_2](tTagTO_2))

  final override def executeUntyped(arguments: Vector[ActionObject])(context: ExecutionContext): Vector[ActionObject] =
    execute()(context)

  final override def inferKnowledgeUntyped(knowledge: Vector[Knowledge[ActionObject]])(
      context: InferContext
  ): (Vector[Knowledge[ActionObject]], InferenceWarnings) =
    inferKnowledge()(context)

  protected def execute()(context: ExecutionContext): (TO_0, TO_1, TO_2)

  protected def inferKnowledge()(
      context: InferContext
  ): ((Knowledge[TO_0], Knowledge[TO_1], Knowledge[TO_2]), InferenceWarnings) = {
    (
      (
        Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0](tTagTO_0)),
        Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_1](tTagTO_1)),
        Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_2](tTagTO_2))
      ),
      InferenceWarnings.empty
    )
  }

}

abstract class Action1To0[TI_0 <: ActionObject] extends Action {

  final override val inArity = 1

  final override val outArity = 0

  def tTagTI_0: ru.TypeTag[TI_0]

  @transient
  final override lazy val inPortTypes: Vector[ru.TypeTag[_]] = Vector(ru.typeTag[TI_0](tTagTI_0))

  @transient
  final override lazy val outPortTypes: Vector[ru.TypeTag[_]] = Vector()

  final override def executeUntyped(arguments: Vector[ActionObject])(context: ExecutionContext): Vector[ActionObject] = {
    execute(arguments(0).asInstanceOf[TI_0])(context)
    Vector()
  }

  final override def inferKnowledgeUntyped(
      knowledge: Vector[Knowledge[ActionObject]]
  )(context: InferContext): (Vector[Knowledge[ActionObject]], InferenceWarnings) = {
    inferKnowledge(knowledge(0).asInstanceOf[Knowledge[TI_0]])(context)
    (Vector(), InferenceWarnings.empty)
  }

  protected def execute(t0: TI_0)(context: ExecutionContext): Unit

  protected def inferKnowledge(k0: Knowledge[TI_0])(context: InferContext): (Unit, InferenceWarnings) =
    ((), InferenceWarnings.empty)

}

abstract class Action1To1[TI_0 <: ActionObject, TO_0 <: ActionObject] extends Action {

  final override val inArity = 1

  final override val outArity = 1

  def tTagTI_0: ru.TypeTag[TI_0]

  def tTagTO_0: ru.TypeTag[TO_0]

  @transient
  final override lazy val inPortTypes: Vector[ru.TypeTag[_]] = Vector(ru.typeTag[TI_0](tTagTI_0))

  @transient
  final override lazy val outPortTypes: Vector[ru.TypeTag[_]] = Vector(ru.typeTag[TO_0](tTagTO_0))

  final override def executeUntyped(arguments: Vector[ActionObject])(context: ExecutionContext): Vector[ActionObject] =
    execute(arguments(0).asInstanceOf[TI_0])(context)

  final override def inferKnowledgeUntyped(knowledge: Vector[Knowledge[ActionObject]])(
      context: InferContext
  ): (Vector[Knowledge[ActionObject]], InferenceWarnings) =
    inferKnowledge(knowledge(0).asInstanceOf[Knowledge[TI_0]])(context)

  protected def execute(t0: TI_0)(context: ExecutionContext): TO_0

  protected def inferKnowledge(k0: Knowledge[TI_0])(context: InferContext): (Knowledge[TO_0], InferenceWarnings) =
    (Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0](tTagTO_0)), InferenceWarnings.empty)

}

abstract class Action1To2[TI_0 <: ActionObject, TO_0 <: ActionObject, TO_1 <: ActionObject] extends Action {

  final override val inArity = 1

  final override val outArity = 2

  def tTagTI_0: ru.TypeTag[TI_0]

  def tTagTO_0: ru.TypeTag[TO_0]

  def tTagTO_1: ru.TypeTag[TO_1]

  @transient
  final override lazy val inPortTypes: Vector[ru.TypeTag[_]] = Vector(ru.typeTag[TI_0](tTagTI_0))

  @transient
  final override lazy val outPortTypes: Vector[ru.TypeTag[_]] =
    Vector(ru.typeTag[TO_0](tTagTO_0), ru.typeTag[TO_1](tTagTO_1))

  final override def executeUntyped(arguments: Vector[ActionObject])(context: ExecutionContext): Vector[ActionObject] =
    execute(arguments(0).asInstanceOf[TI_0])(context)

  final override def inferKnowledgeUntyped(knowledge: Vector[Knowledge[ActionObject]])(
      context: InferContext
  ): (Vector[Knowledge[ActionObject]], InferenceWarnings) =
    inferKnowledge(knowledge(0).asInstanceOf[Knowledge[TI_0]])(context)

  protected def execute(t0: TI_0)(context: ExecutionContext): (TO_0, TO_1)

  protected def inferKnowledge(
      k0: Knowledge[TI_0]
  )(context: InferContext): ((Knowledge[TO_0], Knowledge[TO_1]), InferenceWarnings) = {
    (
      (
        Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0](tTagTO_0)),
        Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_1](tTagTO_1))
      ),
      InferenceWarnings.empty
    )
  }

}

abstract class Action1To3[TI_0 <: ActionObject, TO_0 <: ActionObject, TO_1 <: ActionObject, TO_2 <: ActionObject]
    extends Action {

  final override val inArity = 1

  final override val outArity = 3

  def tTagTI_0: ru.TypeTag[TI_0]

  def tTagTO_0: ru.TypeTag[TO_0]

  def tTagTO_1: ru.TypeTag[TO_1]

  def tTagTO_2: ru.TypeTag[TO_2]

  @transient
  final override lazy val inPortTypes: Vector[ru.TypeTag[_]] = Vector(ru.typeTag[TI_0](tTagTI_0))

  @transient
  final override lazy val outPortTypes: Vector[ru.TypeTag[_]] =
    Vector(ru.typeTag[TO_0](tTagTO_0), ru.typeTag[TO_1](tTagTO_1), ru.typeTag[TO_2](tTagTO_2))

  final override def executeUntyped(arguments: Vector[ActionObject])(context: ExecutionContext): Vector[ActionObject] =
    execute(arguments(0).asInstanceOf[TI_0])(context)

  final override def inferKnowledgeUntyped(knowledge: Vector[Knowledge[ActionObject]])(
      context: InferContext
  ): (Vector[Knowledge[ActionObject]], InferenceWarnings) =
    inferKnowledge(knowledge(0).asInstanceOf[Knowledge[TI_0]])(context)

  protected def execute(t0: TI_0)(context: ExecutionContext): (TO_0, TO_1, TO_2)

  protected def inferKnowledge(
      k0: Knowledge[TI_0]
  )(context: InferContext): ((Knowledge[TO_0], Knowledge[TO_1], Knowledge[TO_2]), InferenceWarnings) = {
    (
      (
        Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0](tTagTO_0)),
        Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_1](tTagTO_1)),
        Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_2](tTagTO_2))
      ),
      InferenceWarnings.empty
    )
  }

}

abstract class Action2To0[TI_0 <: ActionObject, TI_1 <: ActionObject] extends Action {

  final override val inArity = 2

  final override val outArity = 0

  def tTagTI_0: ru.TypeTag[TI_0]

  def tTagTI_1: ru.TypeTag[TI_1]

  @transient
  final override lazy val inPortTypes: Vector[ru.TypeTag[_]] =
    Vector(ru.typeTag[TI_0](tTagTI_0), ru.typeTag[TI_1](tTagTI_1))

  @transient
  final override lazy val outPortTypes: Vector[ru.TypeTag[_]] = Vector()

  final override def executeUntyped(arguments: Vector[ActionObject])(context: ExecutionContext): Vector[ActionObject] = {
    execute(arguments(0).asInstanceOf[TI_0], arguments(1).asInstanceOf[TI_1])(context)
    Vector()
  }

  final override def inferKnowledgeUntyped(
      knowledge: Vector[Knowledge[ActionObject]]
  )(context: InferContext): (Vector[Knowledge[ActionObject]], InferenceWarnings) = {
    inferKnowledge(knowledge(0).asInstanceOf[Knowledge[TI_0]], knowledge(1).asInstanceOf[Knowledge[TI_1]])(context)
    (Vector(), InferenceWarnings.empty)
  }

  protected def execute(t0: TI_0, t1: TI_1)(context: ExecutionContext): Unit

  protected def inferKnowledge(k0: Knowledge[TI_0], k1: Knowledge[TI_1])(
      context: InferContext
  ): (Unit, InferenceWarnings) = ((), InferenceWarnings.empty)

}

abstract class Action2To1[TI_0 <: ActionObject, TI_1 <: ActionObject, TO_0 <: ActionObject] extends Action {

  final override val inArity = 2

  final override val outArity = 1

  def tTagTI_0: ru.TypeTag[TI_0]

  def tTagTI_1: ru.TypeTag[TI_1]

  def tTagTO_0: ru.TypeTag[TO_0]

  @transient
  final override lazy val inPortTypes: Vector[ru.TypeTag[_]] =
    Vector(ru.typeTag[TI_0](tTagTI_0), ru.typeTag[TI_1](tTagTI_1))

  @transient
  final override lazy val outPortTypes: Vector[ru.TypeTag[_]] = Vector(ru.typeTag[TO_0](tTagTO_0))

  final override def executeUntyped(arguments: Vector[ActionObject])(context: ExecutionContext): Vector[ActionObject] =
    execute(arguments(0).asInstanceOf[TI_0], arguments(1).asInstanceOf[TI_1])(context)

  final override def inferKnowledgeUntyped(
      knowledge: Vector[Knowledge[ActionObject]]
  )(context: InferContext): (Vector[Knowledge[ActionObject]], InferenceWarnings) =
    inferKnowledge(knowledge(0).asInstanceOf[Knowledge[TI_0]], knowledge(1).asInstanceOf[Knowledge[TI_1]])(context)

  protected def execute(t0: TI_0, t1: TI_1)(context: ExecutionContext): TO_0

  protected def inferKnowledge(k0: Knowledge[TI_0], k1: Knowledge[TI_1])(
      context: InferContext
  ): (Knowledge[TO_0], InferenceWarnings) =
    (Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0](tTagTO_0)), InferenceWarnings.empty)

}

abstract class Action2To2[TI_0 <: ActionObject, TI_1 <: ActionObject, TO_0 <: ActionObject, TO_1 <: ActionObject]
    extends Action {

  final override val inArity = 2

  final override val outArity = 2

  def tTagTI_0: ru.TypeTag[TI_0]

  def tTagTI_1: ru.TypeTag[TI_1]

  def tTagTO_0: ru.TypeTag[TO_0]

  def tTagTO_1: ru.TypeTag[TO_1]

  @transient
  final override lazy val inPortTypes: Vector[ru.TypeTag[_]] =
    Vector(ru.typeTag[TI_0](tTagTI_0), ru.typeTag[TI_1](tTagTI_1))

  @transient
  final override lazy val outPortTypes: Vector[ru.TypeTag[_]] =
    Vector(ru.typeTag[TO_0](tTagTO_0), ru.typeTag[TO_1](tTagTO_1))

  final override def executeUntyped(arguments: Vector[ActionObject])(context: ExecutionContext): Vector[ActionObject] =
    execute(arguments(0).asInstanceOf[TI_0], arguments(1).asInstanceOf[TI_1])(context)

  final override def inferKnowledgeUntyped(
      knowledge: Vector[Knowledge[ActionObject]]
  )(context: InferContext): (Vector[Knowledge[ActionObject]], InferenceWarnings) =
    inferKnowledge(knowledge(0).asInstanceOf[Knowledge[TI_0]], knowledge(1).asInstanceOf[Knowledge[TI_1]])(context)

  protected def execute(t0: TI_0, t1: TI_1)(context: ExecutionContext): (TO_0, TO_1)

  protected def inferKnowledge(k0: Knowledge[TI_0], k1: Knowledge[TI_1])(
      context: InferContext
  ): ((Knowledge[TO_0], Knowledge[TO_1]), InferenceWarnings) = {
    (
      (
        Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0](tTagTO_0)),
        Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_1](tTagTO_1))
      ),
      InferenceWarnings.empty
    )
  }

}

abstract class Action2To3[
    TI_0 <: ActionObject,
    TI_1 <: ActionObject,
    TO_0 <: ActionObject,
    TO_1 <: ActionObject,
    TO_2 <: ActionObject
] extends Action {

  final override val inArity = 2

  final override val outArity = 3

  def tTagTI_0: ru.TypeTag[TI_0]

  def tTagTI_1: ru.TypeTag[TI_1]

  def tTagTO_0: ru.TypeTag[TO_0]

  def tTagTO_1: ru.TypeTag[TO_1]

  def tTagTO_2: ru.TypeTag[TO_2]

  @transient
  final override lazy val inPortTypes: Vector[ru.TypeTag[_]] =
    Vector(ru.typeTag[TI_0](tTagTI_0), ru.typeTag[TI_1](tTagTI_1))

  @transient
  final override lazy val outPortTypes: Vector[ru.TypeTag[_]] =
    Vector(ru.typeTag[TO_0](tTagTO_0), ru.typeTag[TO_1](tTagTO_1), ru.typeTag[TO_2](tTagTO_2))

  final override def executeUntyped(arguments: Vector[ActionObject])(context: ExecutionContext): Vector[ActionObject] =
    execute(arguments(0).asInstanceOf[TI_0], arguments(1).asInstanceOf[TI_1])(context)

  final override def inferKnowledgeUntyped(
      knowledge: Vector[Knowledge[ActionObject]]
  )(context: InferContext): (Vector[Knowledge[ActionObject]], InferenceWarnings) =
    inferKnowledge(knowledge(0).asInstanceOf[Knowledge[TI_0]], knowledge(1).asInstanceOf[Knowledge[TI_1]])(context)

  protected def execute(t0: TI_0, t1: TI_1)(context: ExecutionContext): (TO_0, TO_1, TO_2)

  protected def inferKnowledge(k0: Knowledge[TI_0], k1: Knowledge[TI_1])(
      context: InferContext
  ): ((Knowledge[TO_0], Knowledge[TO_1], Knowledge[TO_2]), InferenceWarnings) = {
    (
      (
        Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0](tTagTO_0)),
        Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_1](tTagTO_1)),
        Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_2](tTagTO_2))
      ),
      InferenceWarnings.empty
    )
  }

}

abstract class Action3To0[TI_0 <: ActionObject, TI_1 <: ActionObject, TI_2 <: ActionObject] extends Action {

  final override val inArity = 3

  final override val outArity = 0

  def tTagTI_0: ru.TypeTag[TI_0]

  def tTagTI_1: ru.TypeTag[TI_1]

  def tTagTI_2: ru.TypeTag[TI_2]

  @transient
  final override lazy val inPortTypes: Vector[ru.TypeTag[_]] =
    Vector(ru.typeTag[TI_0](tTagTI_0), ru.typeTag[TI_1](tTagTI_1), ru.typeTag[TI_2](tTagTI_2))

  @transient
  final override lazy val outPortTypes: Vector[ru.TypeTag[_]] = Vector()

  final override def executeUntyped(arguments: Vector[ActionObject])(context: ExecutionContext): Vector[ActionObject] = {
    execute(arguments(0).asInstanceOf[TI_0], arguments(1).asInstanceOf[TI_1], arguments(2).asInstanceOf[TI_2])(context)
    Vector()
  }

  final override def inferKnowledgeUntyped(
      knowledge: Vector[Knowledge[ActionObject]]
  )(context: InferContext): (Vector[Knowledge[ActionObject]], InferenceWarnings) = {
    inferKnowledge(
      knowledge(0).asInstanceOf[Knowledge[TI_0]],
      knowledge(1).asInstanceOf[Knowledge[TI_1]],
      knowledge(2).asInstanceOf[Knowledge[TI_2]]
    )(context)
    (Vector(), InferenceWarnings.empty)
  }

  protected def execute(t0: TI_0, t1: TI_1, t2: TI_2)(context: ExecutionContext): Unit

  protected def inferKnowledge(k0: Knowledge[TI_0], k1: Knowledge[TI_1], k2: Knowledge[TI_2])(
      context: InferContext
  ): (Unit, InferenceWarnings) = ((), InferenceWarnings.empty)

}

abstract class Action3To1[TI_0 <: ActionObject, TI_1 <: ActionObject, TI_2 <: ActionObject, TO_0 <: ActionObject]
    extends Action {

  final override val inArity = 3

  final override val outArity = 1

  def tTagTI_0: ru.TypeTag[TI_0]

  def tTagTI_1: ru.TypeTag[TI_1]

  def tTagTI_2: ru.TypeTag[TI_2]

  def tTagTO_0: ru.TypeTag[TO_0]

  @transient
  final override lazy val inPortTypes: Vector[ru.TypeTag[_]] =
    Vector(ru.typeTag[TI_0](tTagTI_0), ru.typeTag[TI_1](tTagTI_1), ru.typeTag[TI_2](tTagTI_2))

  @transient
  final override lazy val outPortTypes: Vector[ru.TypeTag[_]] = Vector(ru.typeTag[TO_0](tTagTO_0))

  final override def executeUntyped(arguments: Vector[ActionObject])(context: ExecutionContext): Vector[ActionObject] =
    execute(arguments(0).asInstanceOf[TI_0], arguments(1).asInstanceOf[TI_1], arguments(2).asInstanceOf[TI_2])(context)

  final override def inferKnowledgeUntyped(
      knowledge: Vector[Knowledge[ActionObject]]
  )(context: InferContext): (Vector[Knowledge[ActionObject]], InferenceWarnings) = {
    inferKnowledge(
      knowledge(0).asInstanceOf[Knowledge[TI_0]],
      knowledge(1).asInstanceOf[Knowledge[TI_1]],
      knowledge(2).asInstanceOf[Knowledge[TI_2]]
    )(context)
  }

  protected def execute(t0: TI_0, t1: TI_1, t2: TI_2)(context: ExecutionContext): TO_0

  protected def inferKnowledge(k0: Knowledge[TI_0], k1: Knowledge[TI_1], k2: Knowledge[TI_2])(
      context: InferContext
  ): (Knowledge[TO_0], InferenceWarnings) =
    (Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0](tTagTO_0)), InferenceWarnings.empty)

}

abstract class Action3To2[
    TI_0 <: ActionObject,
    TI_1 <: ActionObject,
    TI_2 <: ActionObject,
    TO_0 <: ActionObject,
    TO_1 <: ActionObject
] extends Action {

  final override val inArity = 3

  final override val outArity = 2

  def tTagTI_0: ru.TypeTag[TI_0]

  def tTagTI_1: ru.TypeTag[TI_1]

  def tTagTI_2: ru.TypeTag[TI_2]

  def tTagTO_0: ru.TypeTag[TO_0]

  def tTagTO_1: ru.TypeTag[TO_1]

  @transient
  final override lazy val inPortTypes: Vector[ru.TypeTag[_]] =
    Vector(ru.typeTag[TI_0](tTagTI_0), ru.typeTag[TI_1](tTagTI_1), ru.typeTag[TI_2](tTagTI_2))

  @transient
  final override lazy val outPortTypes: Vector[ru.TypeTag[_]] =
    Vector(ru.typeTag[TO_0](tTagTO_0), ru.typeTag[TO_1](tTagTO_1))

  final override def executeUntyped(arguments: Vector[ActionObject])(context: ExecutionContext): Vector[ActionObject] =
    execute(arguments(0).asInstanceOf[TI_0], arguments(1).asInstanceOf[TI_1], arguments(2).asInstanceOf[TI_2])(context)

  final override def inferKnowledgeUntyped(
      knowledge: Vector[Knowledge[ActionObject]]
  )(context: InferContext): (Vector[Knowledge[ActionObject]], InferenceWarnings) = {
    inferKnowledge(
      knowledge(0).asInstanceOf[Knowledge[TI_0]],
      knowledge(1).asInstanceOf[Knowledge[TI_1]],
      knowledge(2).asInstanceOf[Knowledge[TI_2]]
    )(context)
  }

  protected def execute(t0: TI_0, t1: TI_1, t2: TI_2)(context: ExecutionContext): (TO_0, TO_1)

  protected def inferKnowledge(k0: Knowledge[TI_0], k1: Knowledge[TI_1], k2: Knowledge[TI_2])(
      context: InferContext
  ): ((Knowledge[TO_0], Knowledge[TO_1]), InferenceWarnings) = {
    (
      (
        Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0](tTagTO_0)),
        Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_1](tTagTO_1))
      ),
      InferenceWarnings.empty
    )
  }

}

abstract class Action3To3[
    TI_0 <: ActionObject,
    TI_1 <: ActionObject,
    TI_2 <: ActionObject,
    TO_0 <: ActionObject,
    TO_1 <: ActionObject,
    TO_2 <: ActionObject
] extends Action {

  final override val inArity = 3

  final override val outArity = 3

  def tTagTI_0: ru.TypeTag[TI_0]

  def tTagTI_1: ru.TypeTag[TI_1]

  def tTagTI_2: ru.TypeTag[TI_2]

  def tTagTO_0: ru.TypeTag[TO_0]

  def tTagTO_1: ru.TypeTag[TO_1]

  def tTagTO_2: ru.TypeTag[TO_2]

  @transient
  final override lazy val inPortTypes: Vector[ru.TypeTag[_]] =
    Vector(ru.typeTag[TI_0](tTagTI_0), ru.typeTag[TI_1](tTagTI_1), ru.typeTag[TI_2](tTagTI_2))

  @transient
  final override lazy val outPortTypes: Vector[ru.TypeTag[_]] =
    Vector(ru.typeTag[TO_0](tTagTO_0), ru.typeTag[TO_1](tTagTO_1), ru.typeTag[TO_2](tTagTO_2))

  final override def executeUntyped(arguments: Vector[ActionObject])(context: ExecutionContext): Vector[ActionObject] =
    execute(arguments(0).asInstanceOf[TI_0], arguments(1).asInstanceOf[TI_1], arguments(2).asInstanceOf[TI_2])(context)

  final override def inferKnowledgeUntyped(
      knowledge: Vector[Knowledge[ActionObject]]
  )(context: InferContext): (Vector[Knowledge[ActionObject]], InferenceWarnings) = {
    inferKnowledge(
      knowledge(0).asInstanceOf[Knowledge[TI_0]],
      knowledge(1).asInstanceOf[Knowledge[TI_1]],
      knowledge(2).asInstanceOf[Knowledge[TI_2]]
    )(context)
  }

  protected def execute(t0: TI_0, t1: TI_1, t2: TI_2)(context: ExecutionContext): (TO_0, TO_1, TO_2)

  protected def inferKnowledge(k0: Knowledge[TI_0], k1: Knowledge[TI_1], k2: Knowledge[TI_2])(
      context: InferContext
  ): ((Knowledge[TO_0], Knowledge[TO_1], Knowledge[TO_2]), InferenceWarnings) = {
    (
      (
        Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0](tTagTO_0)),
        Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_1](tTagTO_1)),
        Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_2](tTagTO_2))
      ),
      InferenceWarnings.empty
    )
  }

}

// scalastyle:on
