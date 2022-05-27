package ai.deepsense.deeplang.actionobjects.serialization

import org.apache.hadoop.fs.Path
import org.apache.spark.SparkContext
import org.apache.spark.ml.param.ParamPair
import org.apache.spark.ml.param.Params
import org.apache.spark.ml.util.MLWriter
import org.json4s.JsonDSL._
import org.json4s._
import org.json4s.jackson.JsonMethods._

import ai.deepsense.deeplang.actionobjects.Transformer
import ai.deepsense.sparkutils.ML.MLWriterWithSparkContext

class DefaultMLWriter[T <: Params](instance: T) extends MLWriter with MLWriterWithSparkContext {

  def saveImpl(path: String): Unit = {
    val modelPath = Transformer.modelFilePath(path)
    saveMetadata(instance, path, sc)
    CustomPersistence.save(sparkContext, instance, modelPath)
  }

  /** Saves metadata + Params to: path + "/metadata"
    *   - class
    *   - timestamp
    *   - sparkVersion
    *   - uid
    *   - paramMap
    *   - (optionally, extra metadata)
    *
    * @param extraMetadata
    *   Extra metadata to be saved at same level as uid, paramMap, etc.
    * @param paramMap
    *   If given, this is saved in the "paramMap" field. Otherwise, all [[org.apache.spark.ml.param.Param]]s are encoded
    *   using [[org.apache.spark.ml.param.Param.jsonEncode()]].
    */
  // Copied from org.apache.spark.ml.util.DefaultParamWriter.
  // We need to be consistent with Spark Format, but this method is private.
  private def saveMetadata(
      instance: Params,
      path: String,
      sc: SparkContext,
      extraMetadata: Option[JObject] = None,
      paramMap: Option[JValue] = None
  ): Unit = {
    val uid           = instance.uid
    val cls           = instance.getClass.getName
    val params        = instance.extractParamMap().toSeq.asInstanceOf[Seq[ParamPair[Any]]]
    val jsonParams    = paramMap.getOrElse(render(params.map { case ParamPair(p, v) =>
      p.name -> parse(p.jsonEncode(v))
    }.toList))
    val basicMetadata = ("class" -> cls) ~
      ("timestamp"    -> System.currentTimeMillis()) ~
      ("sparkVersion" -> sc.version) ~
      ("uid"          -> uid) ~
      ("paramMap"     -> jsonParams)
    val metadata     = extraMetadata match {
      case Some(jObject) =>
        basicMetadata ~ jObject
      case None          =>
        basicMetadata
    }
    val metadataPath = new Path(path, "metadata").toString
    val metadataJson = compact(render(metadata))
    sc.parallelize(Seq(metadataJson), 1).saveAsTextFile(metadataPath)
  }

}
