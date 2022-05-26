package ai.deepsense.workflowexecutor.session.storage

import scala.collection.concurrent.TrieMap

import org.apache.spark.sql.{DataFrame => SparkDataFrame}

import ai.deepsense.commons.models.Id
import ai.deepsense.deeplang.DataFrameStorage

class DataFrameStorageImpl extends DataFrameStorage {

  private val inputDataFrames: TrieMap[(Id, Id, Int), SparkDataFrame]  = TrieMap.empty

  private val outputDataFrames: TrieMap[(Id, Id, Int), SparkDataFrame] = TrieMap.empty

  override def getInputDataFrame(workflowId: Id, nodeId: Id, portNumber: Int): Option[SparkDataFrame] =
    inputDataFrames.get((workflowId, nodeId, portNumber))

  override def setInputDataFrame(workflowId: Id, nodeId: Id, portNumber: Int, dataFrame: SparkDataFrame): Unit =
    inputDataFrames.put((workflowId, nodeId, portNumber), dataFrame)

  override def removeNodeInputDataFrames(workflowId: Id, nodeId: Id, portNumber: Int): Unit =
    inputDataFrames.remove((workflowId, nodeId, portNumber))

  override def removeNodeInputDataFrames(workflowId: Id, nodeId: Id): Unit =
    inputDataFrames.retain((k, _) => k._1 != workflowId || k._2 != nodeId)

  override def getOutputDataFrame(workflowId: Id, nodeId: Id, portNumber: Int): Option[SparkDataFrame] =
    outputDataFrames.get((workflowId, nodeId, portNumber))

  override def setOutputDataFrame(workflowId: Id, nodeId: Id, portNumber: Int, dataFrame: SparkDataFrame): Unit =
    outputDataFrames.put((workflowId, nodeId, portNumber), dataFrame)

  override def removeNodeOutputDataFrames(workflowId: Id, nodeId: Id): Unit =
    outputDataFrames.retain((k, _) => k._1 != workflowId || k._2 != nodeId)

}
