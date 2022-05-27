package ai.deepsense.docgen

import ai.deepsense.deeplang.catalogs.actions.ActionCategory
import ai.deepsense.deeplang.actionobjects._
import ai.deepsense.deeplang.actionobjects.stringindexingwrapper.StringIndexingEstimatorWrapper
import ai.deepsense.deeplang.actions.EstimatorAsFactory
import ai.deepsense.deeplang.actions.EstimatorAsOperation
import ai.deepsense.deeplang.actions.EvaluatorAsFactory
import ai.deepsense.deeplang.actions.TransformerAsOperation
import ai.deepsense.deeplang.CatalogRecorder
import ai.deepsense.deeplang.Action

trait SparkOperationsExtractor {

  private val catalog = CatalogRecorder.resourcesCatalogRecorder.catalogs.operations

  def sparkOperations(): Seq[OperationWithSparkClassName] = {
    val operationIds = catalog.operations.keys
    operationIds
      .map(operationId => catalog.createAction(operationId))
      .flatMap(operation =>
        sparkClassName(operation)
          .map(OperationWithSparkClassName(operation.asInstanceOf[DocumentedOperation], _))
      )
      .toSeq
  }

  private def sparkClassName(operation: Action): Option[String] = {
    operation match {
      case (t: TransformerAsOperation[_]) =>
        t.transformer match {
          case (st: SparkTransformerWrapper[_]) =>
            Some(st.sparkTransformer.getClass.getCanonicalName)
          case (st: SparkTransformerAsMultiColumnTransformer[_]) =>
            Some(st.sparkTransformer.getClass.getCanonicalName)
          case _ => None
        }
      case e: (EstimatorAsFactory[_]) =>
        e.estimator match {
          case (se: SparkEstimatorWrapper[_, _, _]) =>
            Some(se.sparkEstimator.getClass.getCanonicalName)
          case (se: SparkMultiColumnEstimatorWrapper[_, _, _, _, _, _]) =>
            Some(se.sparkEstimatorWrapper.sparkEstimator.getClass.getCanonicalName)
          case (siw: StringIndexingEstimatorWrapper[_, _, _, _]) =>
            Some(siw.sparkClassCanonicalName)
          case _ => None
        }
      case ev: (EvaluatorAsFactory[_]) =>
        ev.evaluator match {
          case (sev: SparkEvaluatorWrapper[_]) =>
            Some(sev.sparkEvaluator.getClass.getCanonicalName)
          case _ => None
        }
      case es: (EstimatorAsOperation[_, _]) =>
        es.estimator match {
          case (ses: SparkMultiColumnEstimatorWrapper[_, _, _, _, _, _]) =>
            Some(ses.sparkEstimatorWrapper.sparkEstimator.getClass.getCanonicalName)
          case (ses: SparkEstimatorWrapper[_, _, _]) =>
            Some(ses.sparkEstimator.getClass.getCanonicalName)
          case (siw: StringIndexingEstimatorWrapper[_, _, _, _]) =>
            Some(siw.sparkClassCanonicalName)
          case _ => None
        }
      case _ => None
    }
  }

  def mapByCategory(
      operations: Seq[OperationWithSparkClassName]
  ): Map[ActionCategory, Seq[OperationWithSparkClassName]] = {

    val operationsWithCategories = operations.map { operationWithName =>
      val category = catalog.operations(operationWithName.op.id).category
      OperationWithCategory(category, operationWithName)
    }

    val categories = operationsWithCategories.map(_.category).toSet

    Map(
      categories.toList
        .sortBy(_.name)
        .map(category => category -> operationsWithCategories.filter(_.category == category).map(_.op)): _*
    )
  }

  private case class OperationWithCategory(category: ActionCategory, op: OperationWithSparkClassName)

}
