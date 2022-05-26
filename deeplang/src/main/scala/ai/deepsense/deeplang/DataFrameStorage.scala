package ai.deepsense.deeplang

import org.apache.spark.sql.{DataFrame => SparkDataFrame}

import ai.deepsense.commons.models.Id

trait DataFrameStorage {

  /** Returns custom operation's input dataframe.
    * @param workflowId
    *   workflow id.
    * @param nodeId
    *   node id.
    * @return
    *   input dataframe of the operation.
    */
  def getInputDataFrame(workflowId: Id, nodeId: Id, portNumber: Int): Option[SparkDataFrame]

  /** Sets custom operation's input dataframe.
    * @param workflowId
    *   workflow id.
    * @param nodeId
    *   node id.
    * @param dataFrame
    *   input dataframe of the operation.
    */
  def setInputDataFrame(workflowId: Id, nodeId: Id, portNumber: Int, dataFrame: SparkDataFrame): Unit

  def removeNodeInputDataFrames(workflowId: Id, nodeId: Id, portNumber: Int): Unit

  def removeNodeInputDataFrames(workflowId: Id, nodeId: Id): Unit

  /** Returns custom operation's output dataframe.
    * @param workflowId
    *   workflow id.
    * @param nodeId
    *   node id.
    * @return
    *   output dataframe of the operation.
    */
  def getOutputDataFrame(workflowId: Id, nodeId: Id, portNumber: Int): Option[SparkDataFrame]

  /** Sets custom operation's output dataframe.
    * @param workflowId
    *   workflow id.
    * @param nodeId
    *   node id.
    * @param dataFrame
    *   output dataframe of the operation.
    */
  def setOutputDataFrame(workflowId: Id, nodeId: Id, portNumber: Int, dataFrame: SparkDataFrame): Unit

  def removeNodeOutputDataFrames(workflowId: Id, nodeId: Id): Unit

}

object DataFrameStorage {

  type DataFrameName = String

  type DataFrameId = (Id, DataFrameName)

}
