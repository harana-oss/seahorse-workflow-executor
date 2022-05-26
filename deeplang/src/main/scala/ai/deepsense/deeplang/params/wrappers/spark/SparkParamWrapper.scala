package ai.deepsense.deeplang.params.wrappers.spark

import org.apache.spark.ml
import org.apache.spark.sql.types.StructType

import ai.deepsense.deeplang.params.Param

/** This trait should be mixed in by deeplang parameters derived from Spark parameters. Wrapped Spark parameters should
  * be able to convert deeplang parameter values to Spark parameter values.
  *
  * @tparam T
  *   Type of wrapped Spark parameter value
  * @tparam U
  *   Type of deeplang parameter value
  */
trait SparkParamWrapper[P <: ml.param.Params, T, U] extends Param[U] {

  /** This function should extract wrapped parameter from Spark params of type P. */
  val sparkParamGetter: P => ml.param.Param[T]

  /** This method extracts wrapped Spark parameter from Params, using a function defined as sparkParamGetter. The method
    * is used in ParamsWithSparkWrappers to get parameter values.
    *
    * @param sparkEntity
    *   Spark params
    * @return
    *   Wrapped Spark parameter
    */
  def sparkParam(sparkEntity: ml.param.Params): ml.param.Param[T] =
    sparkParamGetter(sparkEntity.asInstanceOf[P])

  /** Convert deeplang parameter value to wrapped Spark parameter value.
    *
    * @param value
    *   deeplang parameter value
    * @param schema
    *   DataFrame schema, used in column selectors to extract column names
    * @return
    *   Spark parameter value
    */
  def convert(value: U)(schema: StructType): T

  def convertAny(value: Any)(schema: StructType): T = convert(value.asInstanceOf[U])(schema)

  /** @return Wrappers for nested parameters */
  def nestedWrappers: Seq[SparkParamWrapper[_, _, _]] = Seq.empty

}
