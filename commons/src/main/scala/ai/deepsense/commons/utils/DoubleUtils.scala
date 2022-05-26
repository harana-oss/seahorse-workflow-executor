package ai.deepsense.commons.utils

import java.math.MathContext
import java.math.RoundingMode

object DoubleUtils {

  private val significantFigures = 6

  private val mathContext = new MathContext(significantFigures, RoundingMode.HALF_UP)

  def double2String(d: Double): String = {
    if (d.isNaN || d.isInfinity)
      d.toString
    else {
      val decimal = BigDecimal(d)
        .round(mathContext)
        .toString()
      if (decimal.contains("E"))
        decimal.replaceAll("\\.?0*E", "e")
      else if (decimal.contains("."))
        decimal.replaceAll("\\.?0*$", "")
      else
        decimal
    }
  }

}
