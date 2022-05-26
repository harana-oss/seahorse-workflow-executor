package ai.deepsense.deeplang.catalogs.doperable.exceptions

case class NoParameterlessConstructorInClassException(classCanonicalName: String)
    extends DOperableCatalogException(
      s"Concrete class registered in hierarchy has to have" +
        s" parameterless constructor ($classCanonicalName has no parameterless constructor)"
    )
