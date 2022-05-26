package io.deepsense.commons.datetime

import java.sql.Timestamp

import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat
import org.joda.time.DateTime
import org.joda.time.DateTimeZone

trait DateTimeConverter {

  val zone: DateTimeZone = DateTimeZone.getDefault

  val dateTimeFormatter: DateTimeFormatter = ISODateTimeFormat.dateTime()

  def toString(dateTime: DateTime): String = dateTime.toString(dateTimeFormatter)

  def parseDateTime(s: String): DateTime = dateTimeFormatter.parseDateTime(s).withZone(zone)

  def parseTimestamp(s: String): Timestamp = new Timestamp(parseDateTime(s).getMillis)

  def now: DateTime = new DateTime(zone)

  def fromMillis(millis: Long): DateTime = new DateTime(zone).withMillis(millis)

  def dateTime(
      year: Int,
      monthOfyear: Int,
      dayOfMonth: Int,
      hourOfDay: Int = 0,
      minutesOfHour: Int = 0,
      secondsOfMinute: Int = 0
  ): DateTime =
    new DateTime(year, monthOfyear, dayOfMonth, hourOfDay, minutesOfHour, secondsOfMinute, zone)

  def dateTimeFromUTC(
      year: Int,
      monthOfyear: Int,
      dayOfMonth: Int,
      hourOfDay: Int = 0,
      minutesOfHour: Int = 0,
      secondsOfMinute: Int = 0
  ): DateTime =
    new DateTime(year, monthOfyear, dayOfMonth, hourOfDay, minutesOfHour, secondsOfMinute, DateTimeZone.UTC)
      .withZone(DateTimeConverter.zone)

}

object DateTimeConverter extends DateTimeConverter
