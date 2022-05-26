package io.deepsense.commons.json.datasources

import java.lang.reflect.Type

import com.google.gson._
import com.google.gson.reflect.TypeToken

import io.deepsense.api.datasourcemanager.model.Datasource
import io.deepsense.commons.rest.client.datasources.DatasourceTypes.DatasourceList
import org.joda

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

object DatasourceListJsonProtocol {

  private val t = new TypeToken[java.util.LinkedList[Datasource]]() {}.getType

  private val DateTimeType = new TypeToken[joda.time.DateTime]() {}.getType

  private val gson = new GsonBuilder()
    .serializeNulls()
    .registerTypeAdapter(DateTimeType, new DatetimeJsonProtocol)
    .create()

  def fromString(json: String): DatasourceList = {
    val ds: java.util.List[Datasource] = gson.fromJson(json, t)
    ds.toList
  }

  def toString(datasources: DatasourceList): String =
    gson.toJson(datasources.asJava)

}

class DatetimeJsonProtocol extends JsonDeserializer[joda.time.DateTime] with JsonSerializer[joda.time.DateTime] {

  override def deserialize(
      json: JsonElement,
      typeOfT: Type,
      context: JsonDeserializationContext
  ): joda.time.DateTime = {
    val fmt = joda.time.format.ISODateTimeFormat.dateTimeParser()
    fmt.parseDateTime(json.getAsString)
  }

  override def serialize(src: joda.time.DateTime, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
    val fmt = joda.time.format.ISODateTimeFormat.dateTime()
    new JsonPrimitive(fmt.print(src))
  }

}
