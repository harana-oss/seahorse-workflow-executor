package io.deepsense.deeplang

import scala.reflect.runtime.{universe => ru}

import io.deepsense.deeplang.ToVectorConversions._
import io.deepsense.deeplang.inference.InferContext
import io.deepsense.deeplang.inference.InferenceWarnings

/** Implicit conversions used to convert results of execute and inferKnowledge to Vectors */
private object ToVectorConversions {

  implicit def singleValueToVector[T1 <: DOperable](t: T1): Vector[DOperable] =
    Vector(t)

  implicit def tuple2ToVector[T1 <: DOperable, T2 <: DOperable](t: (T1, T2)): Vector[DOperable] =
    Vector(t._1, t._2)

  implicit def tuple3ToVector[T1 <: DOperable, T2 <: DOperable, T3 <: DOperable](t: (T1, T2, T3)): Vector[DOperable] =
    Vector(t._1, t._2, t._3)

  implicit def dKnowledgeSingletonToVector[T1 <: DOperable](
      t: (DKnowledge[T1], InferenceWarnings)
  ): (Vector[DKnowledge[DOperable]], InferenceWarnings) =
    (Vector(t._1), t._2)

  implicit def dKnowledgeTuple2ToVector[T1 <: DOperable, T2 <: DOperable](
      t: ((DKnowledge[T1], DKnowledge[T2]), InferenceWarnings)
  ): (Vector[DKnowledge[DOperable]], InferenceWarnings) = {
    val (k, w) = t
    (Vector(k._1, k._2), w)
  }

  implicit def dKnowledgeTuple3ToVector[T1 <: DOperable, T2 <: DOperable, T3 <: DOperable](
      t: ((DKnowledge[T1], DKnowledge[T2], DKnowledge[T3]), InferenceWarnings)
  ): (Vector[DKnowledge[DOperable]], InferenceWarnings) = {
    val (k, w) = t
    (Vector(k._1, k._2, k._3), w)
  }

}

/** Following classes are generated automatically. */
// scalastyle:off
abstract class DOperation0To1[TO_0 <: DOperable] extends DOperation {

  final override val inArity = 0

  final override val outArity = 1

  def tTagTO_0: ru.TypeTag[TO_0]

  @transient
  final override lazy val inPortTypes: Vector[ru.TypeTag[_]] = Vector()

  @transient
  final override lazy val outPortTypes: Vector[ru.TypeTag[_]] = Vector(ru.typeTag[TO_0](tTagTO_0))

  final override def executeUntyped(arguments: Vector[DOperable])(context: ExecutionContext): Vector[DOperable] =
    execute()(context)

  final override def inferKnowledgeUntyped(knowledge: Vector[DKnowledge[DOperable]])(
      context: InferContext
  ): (Vector[DKnowledge[DOperable]], InferenceWarnings) =
    inferKnowledge()(context)

  protected def execute()(context: ExecutionContext): TO_0

  protected def inferKnowledge()(context: InferContext): (DKnowledge[TO_0], InferenceWarnings) =
    (DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0](tTagTO_0)), InferenceWarnings.empty)

}

abstract class DOperation0To2[TO_0 <: DOperable, TO_1 <: DOperable] extends DOperation {

  final override val inArity = 0

  final override val outArity = 2

  def tTagTO_0: ru.TypeTag[TO_0]

  def tTagTO_1: ru.TypeTag[TO_1]

  @transient
  final override lazy val inPortTypes: Vector[ru.TypeTag[_]] = Vector()

  @transient
  final override lazy val outPortTypes: Vector[ru.TypeTag[_]] =
    Vector(ru.typeTag[TO_0](tTagTO_0), ru.typeTag[TO_1](tTagTO_1))

  final override def executeUntyped(arguments: Vector[DOperable])(context: ExecutionContext): Vector[DOperable] =
    execute()(context)

  final override def inferKnowledgeUntyped(knowledge: Vector[DKnowledge[DOperable]])(
      context: InferContext
  ): (Vector[DKnowledge[DOperable]], InferenceWarnings) =
    inferKnowledge()(context)

  protected def execute()(context: ExecutionContext): (TO_0, TO_1)

  protected def inferKnowledge()(context: InferContext): ((DKnowledge[TO_0], DKnowledge[TO_1]), InferenceWarnings) =
    (
      (
        DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0](tTagTO_0)),
        DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_1](tTagTO_1))
      ),
      InferenceWarnings.empty
    )

}

abstract class DOperation0To3[TO_0 <: DOperable, TO_1 <: DOperable, TO_2 <: DOperable] extends DOperation {

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

  final override def executeUntyped(arguments: Vector[DOperable])(context: ExecutionContext): Vector[DOperable] =
    execute()(context)

  final override def inferKnowledgeUntyped(knowledge: Vector[DKnowledge[DOperable]])(
      context: InferContext
  ): (Vector[DKnowledge[DOperable]], InferenceWarnings) =
    inferKnowledge()(context)

  protected def execute()(context: ExecutionContext): (TO_0, TO_1, TO_2)

  protected def inferKnowledge()(
      context: InferContext
  ): ((DKnowledge[TO_0], DKnowledge[TO_1], DKnowledge[TO_2]), InferenceWarnings) =
    (
      (
        DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0](tTagTO_0)),
        DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_1](tTagTO_1)),
        DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_2](tTagTO_2))
      ),
      InferenceWarnings.empty
    )

}

abstract class DOperation1To0[TI_0 <: DOperable] extends DOperation {

  final override val inArity = 1

  final override val outArity = 0

  def tTagTI_0: ru.TypeTag[TI_0]

  @transient
  final override lazy val inPortTypes: Vector[ru.TypeTag[_]] = Vector(ru.typeTag[TI_0](tTagTI_0))

  @transient
  final override lazy val outPortTypes: Vector[ru.TypeTag[_]] = Vector()

  final override def executeUntyped(arguments: Vector[DOperable])(context: ExecutionContext): Vector[DOperable] = {
    execute(arguments(0).asInstanceOf[TI_0])(context)
    Vector()
  }

  final override def inferKnowledgeUntyped(
      knowledge: Vector[DKnowledge[DOperable]]
  )(context: InferContext): (Vector[DKnowledge[DOperable]], InferenceWarnings) = {
    inferKnowledge(knowledge(0).asInstanceOf[DKnowledge[TI_0]])(context)
    (Vector(), InferenceWarnings.empty)
  }

  protected def execute(t0: TI_0)(context: ExecutionContext): Unit

  protected def inferKnowledge(k0: DKnowledge[TI_0])(context: InferContext): (Unit, InferenceWarnings) =
    ((), InferenceWarnings.empty)

}

abstract class DOperation1To1[TI_0 <: DOperable, TO_0 <: DOperable] extends DOperation {

  final override val inArity = 1

  final override val outArity = 1

  def tTagTI_0: ru.TypeTag[TI_0]

  def tTagTO_0: ru.TypeTag[TO_0]

  @transient
  final override lazy val inPortTypes: Vector[ru.TypeTag[_]] = Vector(ru.typeTag[TI_0](tTagTI_0))

  @transient
  final override lazy val outPortTypes: Vector[ru.TypeTag[_]] = Vector(ru.typeTag[TO_0](tTagTO_0))

  final override def executeUntyped(arguments: Vector[DOperable])(context: ExecutionContext): Vector[DOperable] =
    execute(arguments(0).asInstanceOf[TI_0])(context)

  final override def inferKnowledgeUntyped(knowledge: Vector[DKnowledge[DOperable]])(
      context: InferContext
  ): (Vector[DKnowledge[DOperable]], InferenceWarnings) =
    inferKnowledge(knowledge(0).asInstanceOf[DKnowledge[TI_0]])(context)

  protected def execute(t0: TI_0)(context: ExecutionContext): TO_0

  protected def inferKnowledge(k0: DKnowledge[TI_0])(context: InferContext): (DKnowledge[TO_0], InferenceWarnings) =
    (DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0](tTagTO_0)), InferenceWarnings.empty)

}

abstract class DOperation1To2[TI_0 <: DOperable, TO_0 <: DOperable, TO_1 <: DOperable] extends DOperation {

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

  final override def executeUntyped(arguments: Vector[DOperable])(context: ExecutionContext): Vector[DOperable] =
    execute(arguments(0).asInstanceOf[TI_0])(context)

  final override def inferKnowledgeUntyped(knowledge: Vector[DKnowledge[DOperable]])(
      context: InferContext
  ): (Vector[DKnowledge[DOperable]], InferenceWarnings) =
    inferKnowledge(knowledge(0).asInstanceOf[DKnowledge[TI_0]])(context)

  protected def execute(t0: TI_0)(context: ExecutionContext): (TO_0, TO_1)

  protected def inferKnowledge(
      k0: DKnowledge[TI_0]
  )(context: InferContext): ((DKnowledge[TO_0], DKnowledge[TO_1]), InferenceWarnings) =
    (
      (
        DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0](tTagTO_0)),
        DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_1](tTagTO_1))
      ),
      InferenceWarnings.empty
    )

}

abstract class DOperation1To3[TI_0 <: DOperable, TO_0 <: DOperable, TO_1 <: DOperable, TO_2 <: DOperable]
    extends DOperation {

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

  final override def executeUntyped(arguments: Vector[DOperable])(context: ExecutionContext): Vector[DOperable] =
    execute(arguments(0).asInstanceOf[TI_0])(context)

  final override def inferKnowledgeUntyped(knowledge: Vector[DKnowledge[DOperable]])(
      context: InferContext
  ): (Vector[DKnowledge[DOperable]], InferenceWarnings) =
    inferKnowledge(knowledge(0).asInstanceOf[DKnowledge[TI_0]])(context)

  protected def execute(t0: TI_0)(context: ExecutionContext): (TO_0, TO_1, TO_2)

  protected def inferKnowledge(
      k0: DKnowledge[TI_0]
  )(context: InferContext): ((DKnowledge[TO_0], DKnowledge[TO_1], DKnowledge[TO_2]), InferenceWarnings) =
    (
      (
        DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0](tTagTO_0)),
        DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_1](tTagTO_1)),
        DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_2](tTagTO_2))
      ),
      InferenceWarnings.empty
    )

}

abstract class DOperation2To0[TI_0 <: DOperable, TI_1 <: DOperable] extends DOperation {

  final override val inArity = 2

  final override val outArity = 0

  def tTagTI_0: ru.TypeTag[TI_0]

  def tTagTI_1: ru.TypeTag[TI_1]

  @transient
  final override lazy val inPortTypes: Vector[ru.TypeTag[_]] =
    Vector(ru.typeTag[TI_0](tTagTI_0), ru.typeTag[TI_1](tTagTI_1))

  @transient
  final override lazy val outPortTypes: Vector[ru.TypeTag[_]] = Vector()

  final override def executeUntyped(arguments: Vector[DOperable])(context: ExecutionContext): Vector[DOperable] = {
    execute(arguments(0).asInstanceOf[TI_0], arguments(1).asInstanceOf[TI_1])(context)
    Vector()
  }

  final override def inferKnowledgeUntyped(
      knowledge: Vector[DKnowledge[DOperable]]
  )(context: InferContext): (Vector[DKnowledge[DOperable]], InferenceWarnings) = {
    inferKnowledge(knowledge(0).asInstanceOf[DKnowledge[TI_0]], knowledge(1).asInstanceOf[DKnowledge[TI_1]])(context)
    (Vector(), InferenceWarnings.empty)
  }

  protected def execute(t0: TI_0, t1: TI_1)(context: ExecutionContext): Unit

  protected def inferKnowledge(k0: DKnowledge[TI_0], k1: DKnowledge[TI_1])(
      context: InferContext
  ): (Unit, InferenceWarnings) = ((), InferenceWarnings.empty)

}

abstract class DOperation2To1[TI_0 <: DOperable, TI_1 <: DOperable, TO_0 <: DOperable] extends DOperation {

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

  final override def executeUntyped(arguments: Vector[DOperable])(context: ExecutionContext): Vector[DOperable] =
    execute(arguments(0).asInstanceOf[TI_0], arguments(1).asInstanceOf[TI_1])(context)

  final override def inferKnowledgeUntyped(knowledge: Vector[DKnowledge[DOperable]])(
      context: InferContext
  ): (Vector[DKnowledge[DOperable]], InferenceWarnings) =
    inferKnowledge(knowledge(0).asInstanceOf[DKnowledge[TI_0]], knowledge(1).asInstanceOf[DKnowledge[TI_1]])(context)

  protected def execute(t0: TI_0, t1: TI_1)(context: ExecutionContext): TO_0

  protected def inferKnowledge(k0: DKnowledge[TI_0], k1: DKnowledge[TI_1])(
      context: InferContext
  ): (DKnowledge[TO_0], InferenceWarnings) =
    (DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0](tTagTO_0)), InferenceWarnings.empty)

}

abstract class DOperation2To2[TI_0 <: DOperable, TI_1 <: DOperable, TO_0 <: DOperable, TO_1 <: DOperable]
    extends DOperation {

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

  final override def executeUntyped(arguments: Vector[DOperable])(context: ExecutionContext): Vector[DOperable] =
    execute(arguments(0).asInstanceOf[TI_0], arguments(1).asInstanceOf[TI_1])(context)

  final override def inferKnowledgeUntyped(knowledge: Vector[DKnowledge[DOperable]])(
      context: InferContext
  ): (Vector[DKnowledge[DOperable]], InferenceWarnings) =
    inferKnowledge(knowledge(0).asInstanceOf[DKnowledge[TI_0]], knowledge(1).asInstanceOf[DKnowledge[TI_1]])(context)

  protected def execute(t0: TI_0, t1: TI_1)(context: ExecutionContext): (TO_0, TO_1)

  protected def inferKnowledge(k0: DKnowledge[TI_0], k1: DKnowledge[TI_1])(
      context: InferContext
  ): ((DKnowledge[TO_0], DKnowledge[TO_1]), InferenceWarnings) =
    (
      (
        DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0](tTagTO_0)),
        DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_1](tTagTO_1))
      ),
      InferenceWarnings.empty
    )

}

abstract class DOperation2To3[
    TI_0 <: DOperable,
    TI_1 <: DOperable,
    TO_0 <: DOperable,
    TO_1 <: DOperable,
    TO_2 <: DOperable
] extends DOperation {

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

  final override def executeUntyped(arguments: Vector[DOperable])(context: ExecutionContext): Vector[DOperable] =
    execute(arguments(0).asInstanceOf[TI_0], arguments(1).asInstanceOf[TI_1])(context)

  final override def inferKnowledgeUntyped(knowledge: Vector[DKnowledge[DOperable]])(
      context: InferContext
  ): (Vector[DKnowledge[DOperable]], InferenceWarnings) =
    inferKnowledge(knowledge(0).asInstanceOf[DKnowledge[TI_0]], knowledge(1).asInstanceOf[DKnowledge[TI_1]])(context)

  protected def execute(t0: TI_0, t1: TI_1)(context: ExecutionContext): (TO_0, TO_1, TO_2)

  protected def inferKnowledge(k0: DKnowledge[TI_0], k1: DKnowledge[TI_1])(
      context: InferContext
  ): ((DKnowledge[TO_0], DKnowledge[TO_1], DKnowledge[TO_2]), InferenceWarnings) =
    (
      (
        DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0](tTagTO_0)),
        DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_1](tTagTO_1)),
        DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_2](tTagTO_2))
      ),
      InferenceWarnings.empty
    )

}

abstract class DOperation3To0[TI_0 <: DOperable, TI_1 <: DOperable, TI_2 <: DOperable] extends DOperation {

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

  final override def executeUntyped(arguments: Vector[DOperable])(context: ExecutionContext): Vector[DOperable] = {
    execute(arguments(0).asInstanceOf[TI_0], arguments(1).asInstanceOf[TI_1], arguments(2).asInstanceOf[TI_2])(context)
    Vector()
  }

  final override def inferKnowledgeUntyped(
      knowledge: Vector[DKnowledge[DOperable]]
  )(context: InferContext): (Vector[DKnowledge[DOperable]], InferenceWarnings) = {
    inferKnowledge(
      knowledge(0).asInstanceOf[DKnowledge[TI_0]],
      knowledge(1).asInstanceOf[DKnowledge[TI_1]],
      knowledge(2).asInstanceOf[DKnowledge[TI_2]]
    )(context)
    (Vector(), InferenceWarnings.empty)
  }

  protected def execute(t0: TI_0, t1: TI_1, t2: TI_2)(context: ExecutionContext): Unit

  protected def inferKnowledge(k0: DKnowledge[TI_0], k1: DKnowledge[TI_1], k2: DKnowledge[TI_2])(
      context: InferContext
  ): (Unit, InferenceWarnings) =
    ((), InferenceWarnings.empty)

}

abstract class DOperation3To1[TI_0 <: DOperable, TI_1 <: DOperable, TI_2 <: DOperable, TO_0 <: DOperable]
    extends DOperation {

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

  final override def executeUntyped(arguments: Vector[DOperable])(context: ExecutionContext): Vector[DOperable] =
    execute(arguments(0).asInstanceOf[TI_0], arguments(1).asInstanceOf[TI_1], arguments(2).asInstanceOf[TI_2])(context)

  final override def inferKnowledgeUntyped(
      knowledge: Vector[DKnowledge[DOperable]]
  )(context: InferContext): (Vector[DKnowledge[DOperable]], InferenceWarnings) =
    inferKnowledge(
      knowledge(0).asInstanceOf[DKnowledge[TI_0]],
      knowledge(1).asInstanceOf[DKnowledge[TI_1]],
      knowledge(2).asInstanceOf[DKnowledge[TI_2]]
    )(context)

  protected def execute(t0: TI_0, t1: TI_1, t2: TI_2)(context: ExecutionContext): TO_0

  protected def inferKnowledge(k0: DKnowledge[TI_0], k1: DKnowledge[TI_1], k2: DKnowledge[TI_2])(
      context: InferContext
  ): (DKnowledge[TO_0], InferenceWarnings) =
    (DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0](tTagTO_0)), InferenceWarnings.empty)

}

abstract class DOperation3To2[
    TI_0 <: DOperable,
    TI_1 <: DOperable,
    TI_2 <: DOperable,
    TO_0 <: DOperable,
    TO_1 <: DOperable
] extends DOperation {

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

  final override def executeUntyped(arguments: Vector[DOperable])(context: ExecutionContext): Vector[DOperable] =
    execute(arguments(0).asInstanceOf[TI_0], arguments(1).asInstanceOf[TI_1], arguments(2).asInstanceOf[TI_2])(context)

  final override def inferKnowledgeUntyped(
      knowledge: Vector[DKnowledge[DOperable]]
  )(context: InferContext): (Vector[DKnowledge[DOperable]], InferenceWarnings) =
    inferKnowledge(
      knowledge(0).asInstanceOf[DKnowledge[TI_0]],
      knowledge(1).asInstanceOf[DKnowledge[TI_1]],
      knowledge(2).asInstanceOf[DKnowledge[TI_2]]
    )(context)

  protected def execute(t0: TI_0, t1: TI_1, t2: TI_2)(context: ExecutionContext): (TO_0, TO_1)

  protected def inferKnowledge(k0: DKnowledge[TI_0], k1: DKnowledge[TI_1], k2: DKnowledge[TI_2])(
      context: InferContext
  ): ((DKnowledge[TO_0], DKnowledge[TO_1]), InferenceWarnings) =
    (
      (
        DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0](tTagTO_0)),
        DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_1](tTagTO_1))
      ),
      InferenceWarnings.empty
    )

}

abstract class DOperation3To3[
    TI_0 <: DOperable,
    TI_1 <: DOperable,
    TI_2 <: DOperable,
    TO_0 <: DOperable,
    TO_1 <: DOperable,
    TO_2 <: DOperable
] extends DOperation {

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

  final override def executeUntyped(arguments: Vector[DOperable])(context: ExecutionContext): Vector[DOperable] =
    execute(arguments(0).asInstanceOf[TI_0], arguments(1).asInstanceOf[TI_1], arguments(2).asInstanceOf[TI_2])(context)

  final override def inferKnowledgeUntyped(
      knowledge: Vector[DKnowledge[DOperable]]
  )(context: InferContext): (Vector[DKnowledge[DOperable]], InferenceWarnings) =
    inferKnowledge(
      knowledge(0).asInstanceOf[DKnowledge[TI_0]],
      knowledge(1).asInstanceOf[DKnowledge[TI_1]],
      knowledge(2).asInstanceOf[DKnowledge[TI_2]]
    )(context)

  protected def execute(t0: TI_0, t1: TI_1, t2: TI_2)(context: ExecutionContext): (TO_0, TO_1, TO_2)

  protected def inferKnowledge(k0: DKnowledge[TI_0], k1: DKnowledge[TI_1], k2: DKnowledge[TI_2])(
      context: InferContext
  ): ((DKnowledge[TO_0], DKnowledge[TO_1], DKnowledge[TO_2]), InferenceWarnings) =
    (
      (
        DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0](tTagTO_0)),
        DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_1](tTagTO_1)),
        DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_2](tTagTO_2))
      ),
      InferenceWarnings.empty
    )

}

// scalastyle:on
