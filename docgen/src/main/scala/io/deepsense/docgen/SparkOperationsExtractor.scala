package io.deepsense.docgen

import io.deepsense.deeplang.catalogs.doperations.DOperationCategory
import io.deepsense.deeplang.doperables._
import io.deepsense.deeplang.doperables.stringindexingwrapper.StringIndexingEstimatorWrapper
import io.deepsense.deeplang.doperations.EstimatorAsFactory
import io.deepsense.deeplang.doperations.EstimatorAsOperation
import io.deepsense.deeplang.doperations.EvaluatorAsFactory
import io.deepsense.deeplang.doperations.TransformerAsOperation
import io.deepsense.deeplang.CatalogRecorder
import io.deepsense.deeplang.DOperation

trait SparkOperationsExtractor {

  private val catalog = CatalogRecorder.resourcesCatalogRecorder.catalogs.dOperationsCatalog

  def sparkOperations(): Seq[OperationWithSparkClassName] = {
    val operationIds = catalog.operations.keys
    operationIds
      .map(operationId => catalog.createDOperation(operationId))
      .flatMap(operation =>
        sparkClassName(operation)
          .map(OperationWithSparkClassName(operation.asInstanceOf[DocumentedOperation], _))
      )
      .toSeq
  }

  private def sparkClassName(operation: DOperation): Option[String] =
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

  def mapByCategory(
      operations: Seq[OperationWithSparkClassName]
  ): Map[DOperationCategory, Seq[OperationWithSparkClassName]] = {

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

  private case class OperationWithCategory(category: DOperationCategory, op: OperationWithSparkClassName)

}
