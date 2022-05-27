package ai.deepsense.deeplang

import java.io.File
import java.net.URL
import java.net.URLClassLoader
import ai.deepsense.commons.utils.LoggerForCallerClass
import ai.deepsense.deeplang.catalogs.FlowCatalog
import ai.deepsense.deeplang.catalogs.spi.CatalogRegistrant
import ai.deepsense.deeplang.catalogs.spi.CatalogRegistrar.DefaultCatalogRegistrar
import ai.deepsense.deeplang.refl.CatalogScanner
import com.typesafe.config.ConfigFactory
import org.apache.spark.SparkContext

/** Class used to register all desired ActionObjects and Actions. */
class CatalogRecorder private (jars: Seq[URL]) {

  def withDir(jarsDir: File): CatalogRecorder = {
    val additionalJars =
      if (jarsDir.exists && jarsDir.isDirectory)
        jarsDir.listFiles().toSeq.filter(f => f.isFile && f.getName.endsWith(".jar"))
      else
        Seq.empty
    withJars(additionalJars)
  }

  def withJars(additionalJars: Seq[File]): CatalogRecorder =
    new CatalogRecorder(jars ++ additionalJars.map(_.toURI.toURL))

  def withSparkContext(sparkContext: SparkContext): CatalogRecorder =
    new CatalogRecorder(jars ++ sparkContext.jars.map(new URL(_)))

  lazy val catalogs: FlowCatalog = {
    val registrar = new DefaultCatalogRegistrar()
    val loader    = URLClassLoader.newInstance(jars.toArray, getClass.getClassLoader)
    CatalogRegistrant.load(registrar, loader)
    new CatalogScanner(jars).register(registrar)
    registrar.catalog
  }

}

object CatalogRecorder {

  val logger = LoggerForCallerClass()

  def fromDir(dir: File): CatalogRecorder =
    new CatalogRecorder(Seq.empty).withDir(dir)

  def fromJars(jars: Seq[URL]): CatalogRecorder =
    new CatalogRecorder(jars)

  def fromSparkContext(sparkContext: SparkContext): CatalogRecorder =
    new CatalogRecorder(Seq.empty).withSparkContext(sparkContext)

  lazy val resourcesCatalogRecorder: CatalogRecorder =
    fromDir(new File(ConfigFactory.load("deeplang.conf").getString("spark-resources-jars-dir")))

}
