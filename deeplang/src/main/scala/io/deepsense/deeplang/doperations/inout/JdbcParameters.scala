package io.deepsense.deeplang.doperations.inout

import io.deepsense.deeplang.params.Params
import io.deepsense.deeplang.params.StringParam

trait JdbcParameters {
  this: Params =>

  val jdbcUrl = StringParam(name = "url", description = None)

  setDefault(jdbcUrl, "jdbc:mysql://HOST:PORT/DATABASE?user=DB_USER&password=DB_PASSWORD")

  def getJdbcUrl: String = $(jdbcUrl)

  def setJdbcUrl(value: String): this.type = set(jdbcUrl, value)

  val jdbcDriverClassName = StringParam(name = "driver", description = None)

  setDefault(jdbcDriverClassName, "com.mysql.jdbc.Driver")

  def getJdbcDriverClassName: String = $(jdbcDriverClassName)

  def setJdbcDriverClassName(value: String): this.type = set(jdbcDriverClassName, value)

  val jdbcTableName = StringParam(name = "table", description = None)

  def getJdbcTableName: String = $(jdbcTableName)

  def setJdbcTableName(value: String): this.type = set(jdbcTableName, value)

}
