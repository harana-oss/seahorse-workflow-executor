package io.deepsense.reportlib.model

object ReportType extends Enumeration {
  type ReportType = Value
  val
    Empty,
    Estimator,
    Evaluator,
    DataFrameFull,
    DataFrameSimplified,
    GridSearch,
    MetricValue,
    Model
      = Value
}
