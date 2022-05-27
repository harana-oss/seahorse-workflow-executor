package ai.deepsense.deeplang

import scala.reflect.runtime.{universe => ru}

import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings

// code below is generated automatically
// scalastyle:off
abstract class DMethod0To1[P, +TO_0 <: ActionObject: ru.TypeTag] extends DMethod {

  def apply(context: ExecutionContext)(parameters: P)(): TO_0

  def infer(context: InferContext)(parameters: P)(): (Knowledge[TO_0], InferenceWarnings) =
    (Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0]), InferenceWarnings.empty)

}

abstract class DMethod0To2[P, +TO_0 <: ActionObject: ru.TypeTag, +TO_1 <: ActionObject: ru.TypeTag] extends DMethod {

  def apply(context: ExecutionContext)(parameters: P)(): (TO_0, TO_1)

  def infer(context: InferContext)(parameters: P)(): ((Knowledge[TO_0], Knowledge[TO_1]), InferenceWarnings) = {
    (
      (
        Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0]),
        Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_1])
      ),
      InferenceWarnings.empty
    )
  }

}

abstract class DMethod0To3[
    P,
    +TO_0 <: ActionObject: ru.TypeTag,
    +TO_1 <: ActionObject: ru.TypeTag,
    +TO_2 <: ActionObject: ru.TypeTag
] extends DMethod {

  def apply(context: ExecutionContext)(parameters: P)(): (TO_0, TO_1, TO_2)

  def infer(
      context: InferContext
  )(parameters: P)(): ((Knowledge[TO_0], Knowledge[TO_1], Knowledge[TO_2]), InferenceWarnings) = {
    (
      (
        Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0]),
        Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_1]),
        Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_2])
      ),
      InferenceWarnings.empty
    )
  }

}

abstract class DMethod1To0[P, TI_0 <: ActionObject: ru.TypeTag] extends DMethod {

  def apply(context: ExecutionContext)(parameters: P)(t0: TI_0): Unit

  def infer(context: InferContext)(parameters: P)(k0: Knowledge[TI_0]): (Unit, InferenceWarnings) =
    ((), InferenceWarnings.empty)

}

abstract class DMethod1To1[P, TI_0 <: ActionObject: ru.TypeTag, +TO_0 <: ActionObject: ru.TypeTag] extends DMethod {

  def apply(context: ExecutionContext)(parameters: P)(t0: TI_0): TO_0

  def infer(context: InferContext)(parameters: P)(k0: Knowledge[TI_0]): (Knowledge[TO_0], InferenceWarnings) =
    (Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0]), InferenceWarnings.empty)

}

abstract class DMethod1To2[
    P,
    TI_0 <: ActionObject: ru.TypeTag,
    +TO_0 <: ActionObject: ru.TypeTag,
    +TO_1 <: ActionObject: ru.TypeTag
] extends DMethod {

  def apply(context: ExecutionContext)(parameters: P)(t0: TI_0): (TO_0, TO_1)

  def infer(
      context: InferContext
  )(parameters: P)(k0: Knowledge[TI_0]): ((Knowledge[TO_0], Knowledge[TO_1]), InferenceWarnings) = {
    (
      (
        Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0]),
        Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_1])
      ),
      InferenceWarnings.empty
    )
  }

}

abstract class DMethod1To3[
    P,
    TI_0 <: ActionObject: ru.TypeTag,
    +TO_0 <: ActionObject: ru.TypeTag,
    +TO_1 <: ActionObject: ru.TypeTag,
    +TO_2 <: ActionObject: ru.TypeTag
] extends DMethod {

  def apply(context: ExecutionContext)(parameters: P)(t0: TI_0): (TO_0, TO_1, TO_2)

  def infer(context: InferContext)(
      parameters: P
  )(k0: Knowledge[TI_0]): ((Knowledge[TO_0], Knowledge[TO_1], Knowledge[TO_2]), InferenceWarnings) = {
    (
      (
        Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0]),
        Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_1]),
        Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_2])
      ),
      InferenceWarnings.empty
    )
  }

}

abstract class DMethod2To0[P, TI_0 <: ActionObject: ru.TypeTag, TI_1 <: ActionObject: ru.TypeTag] extends DMethod {

  def apply(context: ExecutionContext)(parameters: P)(t0: TI_0, t1: TI_1): Unit

  def infer(context: InferContext)(
      parameters: P
  )(k0: Knowledge[TI_0], k1: Knowledge[TI_1]): (Unit, InferenceWarnings) = ((), InferenceWarnings.empty)

}

abstract class DMethod2To1[
    P,
    TI_0 <: ActionObject: ru.TypeTag,
    TI_1 <: ActionObject: ru.TypeTag,
    +TO_0 <: ActionObject: ru.TypeTag
] extends DMethod {

  def apply(context: ExecutionContext)(parameters: P)(t0: TI_0, t1: TI_1): TO_0

  def infer(
      context: InferContext
  )(parameters: P)(k0: Knowledge[TI_0], k1: Knowledge[TI_1]): (Knowledge[TO_0], InferenceWarnings) =
    (Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0]), InferenceWarnings.empty)

}

abstract class DMethod2To2[
    P,
    TI_0 <: ActionObject: ru.TypeTag,
    TI_1 <: ActionObject: ru.TypeTag,
    +TO_0 <: ActionObject: ru.TypeTag,
    +TO_1 <: ActionObject: ru.TypeTag
] extends DMethod {

  def apply(context: ExecutionContext)(parameters: P)(t0: TI_0, t1: TI_1): (TO_0, TO_1)

  def infer(context: InferContext)(
      parameters: P
  )(k0: Knowledge[TI_0], k1: Knowledge[TI_1]): ((Knowledge[TO_0], Knowledge[TO_1]), InferenceWarnings) = {
    (
      (
        Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0]),
        Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_1])
      ),
      InferenceWarnings.empty
    )
  }

}

abstract class DMethod2To3[
    P,
    TI_0 <: ActionObject: ru.TypeTag,
    TI_1 <: ActionObject: ru.TypeTag,
    +TO_0 <: ActionObject: ru.TypeTag,
    +TO_1 <: ActionObject: ru.TypeTag,
    +TO_2 <: ActionObject: ru.TypeTag
] extends DMethod {

  def apply(context: ExecutionContext)(parameters: P)(t0: TI_0, t1: TI_1): (TO_0, TO_1, TO_2)

  def infer(context: InferContext)(parameters: P)(
    k0: Knowledge[TI_0],
    k1: Knowledge[TI_1]
  ): ((Knowledge[TO_0], Knowledge[TO_1], Knowledge[TO_2]), InferenceWarnings) = {
    (
      (
        Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0]),
        Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_1]),
        Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_2])
      ),
      InferenceWarnings.empty
    )
  }

}

abstract class DMethod3To0[
    P,
    TI_0 <: ActionObject: ru.TypeTag,
    TI_1 <: ActionObject: ru.TypeTag,
    TI_2 <: ActionObject: ru.TypeTag
] extends DMethod {

  def apply(context: ExecutionContext)(parameters: P)(t0: TI_0, t1: TI_1, t2: TI_2): Unit

  def infer(
      context: InferContext
  )(parameters: P)(k0: Knowledge[TI_0], k1: Knowledge[TI_1], k2: Knowledge[TI_2]): (Unit, InferenceWarnings) =
    ((), InferenceWarnings.empty)

}

abstract class DMethod3To1[
    P,
    TI_0 <: ActionObject: ru.TypeTag,
    TI_1 <: ActionObject: ru.TypeTag,
    TI_2 <: ActionObject: ru.TypeTag,
    +TO_0 <: ActionObject: ru.TypeTag
] extends DMethod {

  def apply(context: ExecutionContext)(parameters: P)(t0: TI_0, t1: TI_1, t2: TI_2): TO_0

  def infer(context: InferContext)(
      parameters: P
  )(k0: Knowledge[TI_0], k1: Knowledge[TI_1], k2: Knowledge[TI_2]): (Knowledge[TO_0], InferenceWarnings) =
    (Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0]), InferenceWarnings.empty)

}

abstract class DMethod3To2[
    P,
    TI_0 <: ActionObject: ru.TypeTag,
    TI_1 <: ActionObject: ru.TypeTag,
    TI_2 <: ActionObject: ru.TypeTag,
    +TO_0 <: ActionObject: ru.TypeTag,
    +TO_1 <: ActionObject: ru.TypeTag
] extends DMethod {

  def apply(context: ExecutionContext)(parameters: P)(t0: TI_0, t1: TI_1, t2: TI_2): (TO_0, TO_1)

  def infer(context: InferContext)(parameters: P)(
    k0: Knowledge[TI_0],
    k1: Knowledge[TI_1],
    k2: Knowledge[TI_2]
  ): ((Knowledge[TO_0], Knowledge[TO_1]), InferenceWarnings) = {
    (
      (
        Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0]),
        Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_1])
      ),
      InferenceWarnings.empty
    )
  }

}

abstract class DMethod3To3[
    P,
    TI_0 <: ActionObject: ru.TypeTag,
    TI_1 <: ActionObject: ru.TypeTag,
    TI_2 <: ActionObject: ru.TypeTag,
    +TO_0 <: ActionObject: ru.TypeTag,
    +TO_1 <: ActionObject: ru.TypeTag,
    +TO_2 <: ActionObject: ru.TypeTag
] extends DMethod {

  def apply(context: ExecutionContext)(parameters: P)(t0: TI_0, t1: TI_1, t2: TI_2): (TO_0, TO_1, TO_2)

  def infer(context: InferContext)(parameters: P)(
    k0: Knowledge[TI_0],
    k1: Knowledge[TI_1],
    k2: Knowledge[TI_2]
  ): ((Knowledge[TO_0], Knowledge[TO_1], Knowledge[TO_2]), InferenceWarnings) = {
    (
      (
        Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0]),
        Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_1]),
        Knowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_2])
      ),
      InferenceWarnings.empty
    )
  }

}

// scalastyle:on
