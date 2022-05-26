import _root_.io.swagger.codegen.{ClientOptInput, ClientOpts, CodegenConstants, DefaultGenerator}
import _root_.io.swagger.codegen.languages.JavaClientCodegen

import scala.util.Random
import collection.JavaConverters._
import _root_.io.swagger.parser.SwaggerParser

name := "seahorse-executor-api"
description := "API used between subcomponents of Seahorse"

lazy val javaSourceManaged = settingKey[File]("root directory of generated Java files")
lazy val buildSwaggerClients: TaskKey[Seq[File]] = taskKey[Seq[File]]("build datasourcemanager client")
lazy val swaggerPackages = settingKey[Seq[(String, String)]](
  "(package, swagger.json) pairs for which Java classes should be generated." +
    " Swagger location is relative to api/ project.")

swaggerPackages := Seq(
  ("ai.deepsense.api.datasourcemanager", "src/main/resources/datasourcemanager.swagger.json"))

javaSourceManaged := target.value / "java" / "srcManaged"

Compile / managedSourceDirectories += javaSourceManaged.value
Compile / sourceGenerators += (Compile / buildSwaggerClients).taskValue
Compile / buildSwaggerClients := {
  swaggerPackages.value.flatMap { case (pack, swaggerRelativeLocation) =>
    val tmpDir = {
      val randomInt = Random.nextInt().abs
      val dir = new File(s"/tmp/$randomInt")
      assert(!dir.exists(), s"Random directory $dir is non-empty!")
      IO.createDirectory(dir)
      dir
    }
    val swagger = {
      val swaggerPath = (baseDirectory.value / swaggerRelativeLocation).absolutePath
      new SwaggerParser().read(swaggerPath)
    }
    val generator = {
      val codegen = new JavaClientCodegen()
      codegen.setOutputDir(tmpDir.absolutePath)
      val opts = new ClientOpts
      opts.setProperties(Map(
        CodegenConstants.MODEL_PACKAGE -> s"$pack.model",
        CodegenConstants.API_PACKAGE -> s"$pack.client",
        CodegenConstants.INVOKER_PACKAGE -> pack,
        CodegenConstants.LIBRARY -> "retrofit2"
      ).asJava)
      val input = new ClientOptInput().swagger(swagger).config(codegen).opts(opts)
      new DefaultGenerator().opts(input)
    }
    val files = generator.generate()
    val relevantFiles = for {
      file <- files.asScala.toSeq
      // Generator generates also a lot of junk, we are interested only in source files.
      srcRelativeLocation <- IO.relativize(tmpDir / "src/main/java", file)
      newFile = javaSourceManaged.value / srcRelativeLocation
      _ = IO.copyFile(file, newFile)
    } yield newFile
    relevantFiles
  }
}

libraryDependencies ++= Dependencies.api
