package ai.deepsense.deeplang.actions.inout

import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.StringParameter

trait JdbcParameters {
  this: Params =>

  val jdbcUrl = StringParameter(name = "url", description = None)

  setDefault(jdbcUrl, "jdbc:mysql://HOST:PORT/DATABASE?user=DB_USER&password=DB_PASSWORD")

  def getJdbcUrl: String = $(jdbcUrl)

  def setJdbcUrl(value: String): this.type = set(jdbcUrl, value)

  val jdbcDriverClassName = StringParameter(name = "driver", description = None)

  setDefault(jdbcDriverClassName, "com.mysql.jdbc.Driver")

  def getJdbcDriverClassName: String = $(jdbcDriverClassName)

  def setJdbcDriverClassName(value: String): this.type = set(jdbcDriverClassName, value)

  val jdbcTableName = StringParameter(name = "table", description = None)

  def getJdbcTableName: String = $(jdbcTableName)

  def setJdbcTableName(value: String): this.type = set(jdbcTableName, value)

}
