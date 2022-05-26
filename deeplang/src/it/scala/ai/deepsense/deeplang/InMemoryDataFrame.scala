package ai.deepsense.deeplang

import org.apache.spark.sql.Row
import org.apache.spark.sql.types._
import org.scalatest.BeforeAndAfter
import org.scalatest.BeforeAndAfterAll

trait InMemoryDataFrame { self: BeforeAndAfter with BeforeAndAfterAll with LocalExecutionContext =>

  lazy val inMemoryDataFrame = createDataFrame(rows, schema)

  private lazy val schema: StructType =
    StructType(
      Seq(
        StructField("boolean", BooleanType),
        StructField("double", DoubleType),
        StructField("string", StringType)
      )
    )

  private lazy val rows = {
    val base             = Seq(
      Row(true, 0.45, "3.14"),
      Row(false, 0.2, "\"testing...\""),
      Row(false, 3.14159, "Hello, world!"),
      Row(true, 0.1, "asd")
    )
    val repeatedFewTimes = (1 to 10).flatMap(_ => base)
    repeatedFewTimes
  }

}
