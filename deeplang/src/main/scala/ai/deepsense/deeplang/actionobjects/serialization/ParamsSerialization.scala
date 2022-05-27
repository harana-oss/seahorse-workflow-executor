package ai.deepsense.deeplang.actionobjects.serialization

import spray.json.DefaultJsonProtocol
import spray.json.JsObject
import spray.json.JsString
import spray.json.JsValue

import ai.deepsense.deeplang.catalogs.actionobjects.exceptions.NoParameterlessConstructorInClassException
import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.CatalogRecorder
import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.utils.TypeUtils
import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphReader

trait ParamsSerialization {

  self: Params =>

  def saveObjectWithParams(ctx: ExecutionContext, path: String): Unit = {
    saveMetadata(ctx, path)
    saveParams(ctx, path)
  }

  def loadAndSetParams(ctx: ExecutionContext, path: String): this.type =
    setParams(loadParams(ctx, path), ctx.inferContext.graphReader)

  protected def saveMetadata(ctx: ExecutionContext, path: String) = {
    val metadataFilePath = ParamsSerialization.metadataFilePath(path)
    val metadataJson     = JsObject(
      ParamsSerialization.classNameKey -> JsString(this.getClass.getName)
    )
    JsonObjectPersistence.saveJsonToFile(ctx, metadataFilePath, metadataJson)
  }

  protected def saveParams(ctx: ExecutionContext, path: String): Unit = {
    val paramsFilePath = ParamsSerialization.paramsFilePath(path)
    JsonObjectPersistence.saveJsonToFile(ctx, paramsFilePath, paramValuesToJson)
  }

  protected def loadParams(ctx: ExecutionContext, path: String): JsValue =
    JsonObjectPersistence.loadJsonFromFile(ctx, ParamsSerialization.paramsFilePath(path))

  private def setParams(paramsJson: JsValue, graphReader: GraphReader): this.type =
    this.set(paramPairsFromJson(paramsJson, graphReader): _*)

}

object ParamsSerialization {

  val classNameKey = "className"

  val paramsFileName = "params"

  val metadataFileName = "metadata"

  def load(ctx: ExecutionContext, path: String): Loadable = {
    import DefaultJsonProtocol._
    val metadataPath           = metadataFilePath(path)
    val metadataJson: JsObject =
      JsonObjectPersistence.loadJsonFromFile(ctx, metadataPath).asJsObject
    val className              = metadataJson.fields(classNameKey).convertTo[String]
    val clazz: Class[_]        = Class.forName(className)
    val loadable               = TypeUtils
      .createInstance(
        TypeUtils
          .constructorForClass(clazz)
          .getOrElse(throw new NoParameterlessConstructorInClassException(clazz.getCanonicalName))
      )
      .asInstanceOf[Loadable]
    loadable.load(ctx, path)
  }

  def metadataFilePath(path: String): String =
    PathsUtils.combinePaths(path, metadataFileName)

  def paramsFilePath(path: String): String =
    PathsUtils.combinePaths(path, paramsFileName)

}
