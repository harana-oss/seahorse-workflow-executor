package ai.deepsense.deeplang.doperables.serialization

import spray.json._

import ai.deepsense.deeplang.ExecutionContext

object JsonObjectPersistence {

  def saveJsonToFile(ctx: ExecutionContext, path: String, json: JsValue): Unit =
    ctx.sparkContext.parallelize(Seq(json.compactPrint), 1).saveAsTextFile(path)

  def loadJsonFromFile(ctx: ExecutionContext, path: String): JsValue = {
    val paramsString: String = ctx.sparkContext.textFile(path, 1).first()
    paramsString.parseJson
  }

}
