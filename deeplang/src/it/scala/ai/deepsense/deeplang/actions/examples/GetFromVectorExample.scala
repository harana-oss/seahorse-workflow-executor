package ai.deepsense.deeplang.actions.examples

import ai.deepsense.sparkutils.Linalg.Vectors

import ai.deepsense.deeplang.actionobjects.GetFromVectorTransformer
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actions.GetFromVector

class GetFromVectorExample extends AbstractOperationExample[GetFromVector] {

  override def dOperation: GetFromVector = {
    val op          = new GetFromVector()
    val transformer = new GetFromVectorTransformer()
    op.transformer.setIndex(1)
    op.transformer.setSingleColumn("features", "second_feature")
    op.set(op.transformer.extractParamMap())
  }

  override def inputDataFrames: Seq[DataFrame] = {
    val data = Seq(
      Vectors.sparse(3, Seq((0, -2.0), (1, 2.3))),
      Vectors.dense(0.01, 0.2, 3.0),
      null,
      Vectors.sparse(3, Seq((1, 0.91), (2, 3.2))),
      Vectors.sparse(3, Seq((0, 5.7), (2, 2.7))),
      Vectors.sparse(3, Seq()).toDense
    ).map(Tuple1(_))
    Seq(DataFrame.fromSparkDataFrame(sparkSQLSession.createDataFrame(data).toDF("features")))
  }

}
