package ai.deepsense.deeplang.actions.examples

import ai.deepsense.deeplang.actions.SqlTransformation

class SqlTransformationExample extends AbstractOperationExample[SqlTransformation] {

  override def dOperation: SqlTransformation = {
    val op = new SqlTransformation()
    op.transformer
      .setDataFrameId("inputDF")
      .setExpression("select avg(temp) as avg_temp, max(windspeed) as max_windspeed from inputDF")
    op.set(op.transformer.extractParamMap())
  }

  override def fileNames: Seq[String] = Seq("example_datetime_windspeed_hum_temp")

}
