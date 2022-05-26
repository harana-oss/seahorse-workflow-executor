package io.deepsense.deeplang.doperations.examples

import io.deepsense.deeplang.doperations.SqlCombine

class SqlCombineExample extends AbstractOperationExample[SqlCombine] {

  override def dOperation: SqlCombine =
    new SqlCombine()
      .setLeftTableName("beds")
      .setRightTableName("prices")
      .setSqlCombineExpression("""
                                 |SELECT DISTINCT beds.city, beds.beds
                                 |FROM beds
                                 |JOIN prices ON beds.city = prices.city
                                 |AND prices.price < 120000 * beds.beds
        """.stripMargin)

  override def fileNames: Seq[String] = Seq("example_city_beds", "example_city_price")

}
