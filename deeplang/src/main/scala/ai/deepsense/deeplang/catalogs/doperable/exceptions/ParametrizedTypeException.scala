package ai.deepsense.deeplang.catalogs.doperable.exceptions

import scala.reflect.runtime.{universe => ru}

case class ParametrizedTypeException(t: ru.Type)
    extends DOperableCatalogException(s"Cannot register parametrized type in hierarchy (Type $t is parametrized)")
