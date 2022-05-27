package ai.deepsense.deeplang.refl

import java.io.File
import java.net.URL
import java.net.URLClassLoader

import ai.deepsense.commons.utils.Logging
import ai.deepsense.deeplang.catalogs.SortPriority
import ai.deepsense.deeplang.catalogs.spi.CatalogRegistrant
import ai.deepsense.deeplang.catalogs.spi.CatalogRegistrar
import ai.deepsense.deeplang.Action
import ai.deepsense.deeplang.ActionCategories
import ai.deepsense.deeplang.utils.TypeUtils
import org.reflections.Reflections
import org.reflections.util.ConfigurationBuilder

import scala.collection.JavaConversions._

/** Scanner for operations and operables. It scans given jars and additionally a jar containing this class.
  * @param jarsUrls
  *   Jars to scan
  */
class CatalogScanner(jarsUrls: Seq[URL]) extends CatalogRegistrant with Logging {

  /** Scans jars on classpath for classes annotated with [[ai.deepsense.deeplang.refl.Register]] annotation and at the
    * same time implementing [[ai.deepsense.deeplang.Action]] interface. Found classes are then registered in
    * appropriate catalogs.
    *
    * @see
    *   [[ai.deepsense.deeplang.refl.Register]]
    */
  override def register(registrar: CatalogRegistrar): Unit = {
    logger.info(s"Scanning registrables. Following jars will be scanned: ${jarsUrls.mkString(";")}.")
    val scanned    = scanForRegistrables().iterator
    val priorities = SortPriority.sdkInSequence
    for {
      (registrable, priority) <- scanned.zip(priorities)
    } {
      logger.debug(s"Trying to register class $registrable")
      registrable match {
        case DOperationMatcher(doperation) => registerDOperation(registrar, doperation, priority)
        case other                         => logger.warn(s"Only DOperation can be `@Register`ed. '$other' not supported.")
      }
    }
  }

  private def scanForRegistrables(): Set[Class[_]] = {

    val urls = thisJarURLOpt ++ jarsUrls

    if (urls.nonEmpty) {

      val configBuilder = ConfigurationBuilder
        .build(urls.toSeq: _*)
        .addClassLoader(getClass.getClassLoader)
        .setExpandSuperTypes(false)

      if (jarsUrls.nonEmpty)
        configBuilder.addClassLoader(URLClassLoader.newInstance(jarsUrls.toArray, getClass.getClassLoader))

      new Reflections(configBuilder).getTypesAnnotatedWith(classOf[Register]).toSet
    } else
      Set()

  }

  private lazy val thisJarURLOpt: Option[URL] = {
    val jarRegex = """jar:(file:.*\.jar)!.*""".r

    val url =
      getClass.getClassLoader.getResource(getClass.getCanonicalName.replaceAll("\\.", File.separator) + ".class")

    url.toString match {
      case jarRegex(jar) => Some(new URL(jar))
      case _             => None
    }
  }

  private def registerDOperation(
                                  registrar: CatalogRegistrar,
                                  operation: Class[Action],
                                  priority: SortPriority
  ): Unit = TypeUtils.constructorForClass(operation) match {
    case Some(constructor) =>
      registrar.registerOperation(
        ActionCategories.UserDefined,
        () => TypeUtils.createInstance[Action](constructor),
        priority
      )
    case None              =>
      logger.error(
        s"Class $operation could not be registered." +
          "It needs to have parameterless constructor"
      )
  }

  class AssignableFromExtractor[T](targetClass: Class[T]) {

    def unapply(clazz: Class[_]): Option[Class[T]] = {
      if (targetClass.isAssignableFrom(clazz))
        Some(clazz.asInstanceOf[Class[T]])
      else
        None
    }

  }

  object DOperationMatcher extends AssignableFromExtractor(classOf[Action])

}
