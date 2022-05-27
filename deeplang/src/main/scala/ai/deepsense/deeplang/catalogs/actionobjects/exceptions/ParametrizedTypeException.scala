package ai.deepsense.deeplang.catalogs.actionobjects.exceptions

import scala.reflect.runtime.{universe => ru}

case class ParametrizedTypeException(t: ru.Type)
    extends ActionObjectCatalogException(s"Cannot register parametrized type in hierarchy (Type $t is parametrized)")
