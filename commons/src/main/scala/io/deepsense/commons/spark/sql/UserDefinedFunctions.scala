package io.deepsense.commons.spark.sql

import java.lang.{Double => JavaDouble}

import org.apache.spark.sql.UDFRegistration

/** Holds user defined functions that can be injected to UDFRegistration All the functions have to operate on
  * java.lang.Double as input and output, scala.Double does not support null values (null is converted to 0.0) which
  * would lead to undesired information loss (we expect null values in DataFrames)
  */
object UserDefinedFunctions extends Serializable {

  /** Registers user defined function in given UDFRegistration */
  def registerFunctions(udf: UDFRegistration): Unit = {
    udf.register("ABS", nullSafeSingleParamOp(math.abs))
    udf.register("EXP", nullSafeSingleParamOp(math.exp))
    udf.register("POW", nullSafeTwoParamOp(math.pow))
    udf.register("SQRT", nullSafeSingleParamOp(math.sqrt))
    udf.register("SIN", nullSafeSingleParamOp(math.sin))
    udf.register("COS", nullSafeSingleParamOp(math.cos))
    udf.register("TAN", nullSafeSingleParamOp(math.tan))
    udf.register("LN", nullSafeSingleParamOp(math.log))
    udf.register("MINIMUM", nullSafeTwoParamOp(math.min))
    udf.register("MAXIMUM", nullSafeTwoParamOp(math.max))
    udf.register("FLOOR", nullSafeSingleParamOp(math.floor))
    udf.register("CEIL", nullSafeSingleParamOp(math.ceil))
    udf.register("SIGNUM", nullSafeSingleParamOp(math.signum))
  }

  private def nullSafeTwoParamOp(f: (Double, Double) => Double): (JavaDouble, JavaDouble) => java.lang.Double = {
    case (d1, d2) =>
      if (d1 == null || d2 == null)
        null
      else
        f(d1, d2)
  }

  private def nullSafeSingleParamOp(f: (Double) => Double): (JavaDouble) => JavaDouble = { case (d) =>
    if (d == null)
      null
    else
      f(d)
  }

}
