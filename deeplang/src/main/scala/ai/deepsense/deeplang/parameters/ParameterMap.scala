package ai.deepsense.deeplang.parameters

import scala.collection.mutable

case class ParameterMap private[deeplang](private val map: mutable.Map[Parameter[Any], Any]) extends Serializable {

  def put[T](param: Parameter[T], value: T): this.type = put(param -> value)

  def put(paramPairs: ParamPair[_]*): this.type = {
    paramPairs.foreach(p => map(p.param.asInstanceOf[Parameter[Any]]) = p.value)
    this
  }

  def get[T](param: Parameter[T]): Option[T] =
    map.get(param.asInstanceOf[Parameter[Any]]).asInstanceOf[Option[T]]

  def getOrElse[T](param: Parameter[T], default: T): T =
    get(param).getOrElse(default)

  def apply[T](param: Parameter[T]): T = {
    get(param).getOrElse {
      throw new NoSuchElementException(s"Cannot find param ${param.name}.")
    }
  }

  def contains(param: Parameter[_]): Boolean =
    map.contains(param.asInstanceOf[Parameter[Any]])

  def remove[T](param: Parameter[T]): Option[T] =
    map.remove(param.asInstanceOf[Parameter[Any]]).asInstanceOf[Option[T]]

  def ++(other: ParameterMap): ParameterMap =
    ParameterMap(this.map ++ other.map)

  def ++=(other: ParameterMap): this.type = {
    this.map ++= other.map
    this
  }

  def toSeq: Seq[ParamPair[_]] = {
    map.toSeq.map { case (param, value) =>
      ParamPair(param, value)
    }
  }

  def size: Int = map.size

}

object ParameterMap {

  def apply(): ParameterMap = ParameterMap(mutable.Map.empty[Parameter[Any], Any])

  def empty: ParameterMap = ParameterMap()

  def apply(paramPairs: ParamPair[_]*): ParameterMap =
    ParameterMap().put(paramPairs: _*)

}
