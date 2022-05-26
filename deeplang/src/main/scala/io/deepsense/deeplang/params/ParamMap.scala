package io.deepsense.deeplang.params

import scala.collection.mutable

case class ParamMap private[deeplang] (private val map: mutable.Map[Param[Any], Any]) extends Serializable {

  def put[T](param: Param[T], value: T): this.type = put(param -> value)

  def put(paramPairs: ParamPair[_]*): this.type = {
    paramPairs.foreach(p => map(p.param.asInstanceOf[Param[Any]]) = p.value)
    this
  }

  def get[T](param: Param[T]): Option[T] =
    map.get(param.asInstanceOf[Param[Any]]).asInstanceOf[Option[T]]

  def getOrElse[T](param: Param[T], default: T): T =
    get(param).getOrElse(default)

  def apply[T](param: Param[T]): T =
    get(param).getOrElse {
      throw new NoSuchElementException(s"Cannot find param ${param.name}.")
    }

  def contains(param: Param[_]): Boolean =
    map.contains(param.asInstanceOf[Param[Any]])

  def remove[T](param: Param[T]): Option[T] =
    map.remove(param.asInstanceOf[Param[Any]]).asInstanceOf[Option[T]]

  def ++(other: ParamMap): ParamMap =
    ParamMap(this.map ++ other.map)

  def ++=(other: ParamMap): this.type = {
    this.map ++= other.map
    this
  }

  def toSeq: Seq[ParamPair[_]] =
    map.toSeq.map { case (param, value) =>
      ParamPair(param, value)
    }

  def size: Int = map.size

}

object ParamMap {

  def apply(): ParamMap = ParamMap(mutable.Map.empty[Param[Any], Any])

  def empty: ParamMap = ParamMap()

  def apply(paramPairs: ParamPair[_]*): ParamMap =
    ParamMap().put(paramPairs: _*)

}
