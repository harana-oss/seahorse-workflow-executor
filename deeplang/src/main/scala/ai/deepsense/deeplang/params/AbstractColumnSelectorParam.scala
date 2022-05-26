package ai.deepsense.deeplang.params

import spray.json.DefaultJsonProtocol._
import spray.json._

abstract class AbstractColumnSelectorParam[T: JsonFormat] extends ParamWithJsFormat[T] {

  /** Tells if this selectors selects single column or many. */
  protected val isSingle: Boolean

  /** Input port index of the data source. */
  protected val portIndex: Int

  override protected def extraJsFields: Map[String, JsValue] =
    super.extraJsFields ++ Map("isSingle" -> isSingle.toJson, "portIndex" -> portIndex.toJson)

}
