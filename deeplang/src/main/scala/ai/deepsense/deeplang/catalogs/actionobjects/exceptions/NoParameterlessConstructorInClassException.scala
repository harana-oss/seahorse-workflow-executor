package ai.deepsense.deeplang.catalogs.actionobjects.exceptions

case class NoParameterlessConstructorInClassException(classCanonicalName: String)
    extends ActionObjectCatalogException(
      s"Concrete class registered in hierarchy has to have" +
        s" parameterless constructor ($classCanonicalName has no parameterless constructor)"
    )
