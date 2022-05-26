package io.deepsense.deeplang.doperables

import java.util.UUID

import io.deepsense.deeplang.OperationExecutionDispatcher._
import org.apache.spark.sql.types._
import io.deepsense.deeplang._
import io.deepsense.deeplang.params.CodeSnippetLanguage
import io.deepsense.deeplang.params.CodeSnippetParam
import io.deepsense.deeplang.params.Param

case class PythonColumnTransformer() extends CustomCodeColumnTransformer {

  override val codeParameter = CodeSnippetParam(
    name = "column operation code",
    description = None,
    language = CodeSnippetLanguage(CodeSnippetLanguage.python)
  )

  setDefault(codeParameter -> "def transform_value(value, column_name):\n    return value")

  override def getSpecificParams: Array[Param[_]] =
    Array(codeParameter, targetType)

  override def getComposedCode(
      userCode: String,
      inputColumn: String,
      outputColumn: String,
      targetType: DataType
  ): String = {
    val newFieldName = UUID.randomUUID().toString.replace("-", "")
    val newFieldJson =
      s"""{"name": "$newFieldName", "type":${targetType.json}, "nullable":true, "metadata":null}"""

    s"""
       |$userCode
       |
       |from pyspark.sql.types import *
       |import json
       |
       |def transform(dataframe):
       |    new_field = StructField.fromJson(json.loads(\"\"\"$newFieldJson\"\"\"))
       |    schema = StructType(dataframe.schema.fields + [new_field])
       |    def _transform_row(row):
       |        return row + (transform_value(row['$inputColumn'], '$inputColumn'),)
       |    return spark.createDataFrame(dataframe.rdd.map(_transform_row), schema)
    """.stripMargin
  }

  override def runCode(context: ExecutionContext, code: String): Result =
    context.customCodeExecutor.runPython(code)

  override def isValid(context: ExecutionContext, code: String): Boolean =
    context.customCodeExecutor.isPythonValid(code)

}
