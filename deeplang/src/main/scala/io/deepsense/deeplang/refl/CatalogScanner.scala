package io.deepsense.deeplang.refl

import java.io.File
import java.net.{URL, URLClassLoader}

import scala.collection.JavaConversions._

import org.reflections.Reflections
import org.reflections.util.ConfigurationBuilder

import io.deepsense.commons.utils.Logging
import io.deepsense.deeplang.catalogs.doperable.DOperableCatalog
import io.deepsense.deeplang.catalogs.doperations.DOperationsCatalog
import io.deepsense.deeplang.{DOperation, DOperationCategories, TypeUtils}

/**
  * Scanner for operations and operables. It scans given jars and additionally a jar containing this
  * class.
  * @param jarsUrls Jars to scan
  */
class CatalogScanner(jarsUrls: Seq[URL]) extends Logging {

  /**
    * Scans jars on classpath for classes annotated with [[io.deepsense.deeplang.refl.Register
    * Register]]
    * annotation and at the same time implementing [[io.deepsense.deeplang.DOperation DOperation]]
    * interface. Found classes are then registered in appropriate catalogs.
    *
    * @see [[io.deepsense.deeplang.refl.Register Register]]
    */
  def scanAndRegister(
      dOperableCatalog: DOperableCatalog,
      dOperationsCatalog: DOperationsCatalog
  ): Unit = {
    logger.info(
      s"Scanning registrables. Following jars will be scanned: ${jarsUrls.mkString(";")}.")
    for (registrable <- scanForRegistrables()) {
      logger.debug(s"Trying to register class $registrable")
      registrable match {
        case DOperationMatcher(doperation) => registerDOperation(dOperationsCatalog, doperation)
        case other => logger.warn(s"Only DOperation can be `@Register`ed")
      }
    }
  }

  private def scanForRegistrables(): Set[Class[_]] = {

    val urls = thisJarURLOpt ++ jarsUrls

    if (urls.nonEmpty) {

      val configBuilder = ConfigurationBuilder.build(urls.toSeq: _*)

      if (jarsUrls.nonEmpty) {
        configBuilder.addClassLoader(URLClassLoader.newInstance(jarsUrls.toArray))
      }

      new Reflections(configBuilder).getTypesAnnotatedWith(classOf[Register]).toSet
    } else {
      Set()
    }

  }

  private lazy val thisJarURLOpt: Option[URL] = {
    val jarRegex = """jar:(file:.*\.jar)!.*""".r

    val url = getClass.getClassLoader.getResource(
      getClass.getCanonicalName.replaceAll("\\.", File.separator) + ".class")

    url.toString match {
      case jarRegex(jar) => Some(new URL(jar))
      case _ => None
    }
  }


  private def registerDOperation(
      catalog: DOperationsCatalog,
      operation: Class[DOperation]
  ): Unit = TypeUtils.constructorForClass(operation) match {
    case Some(constructor) =>
      catalog.registerDOperation(
        DOperationCategories.UserDefined,
        () => TypeUtils.createInstance[DOperation](constructor)
      )
    case None => logger.error(
      s"Class $operation could not be registered." +
        "It needs to have parameterless constructor"
    )
  }

  class AssignableFromExtractor[T](targetClass: Class[T]) {
    def unapply(clazz: Class[_]): Option[Class[T]] = {
      if (targetClass.isAssignableFrom(clazz)) {
        Some(clazz.asInstanceOf[Class[T]])
      } else {
        None
      }
    }
  }

  object DOperationMatcher extends AssignableFromExtractor(classOf[DOperation])

}
