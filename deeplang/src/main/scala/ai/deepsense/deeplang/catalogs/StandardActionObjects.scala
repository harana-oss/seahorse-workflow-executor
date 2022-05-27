package ai.deepsense.deeplang.catalogs

import ai.deepsense.deeplang.catalogs.spi.CatalogRegistrant
import ai.deepsense.deeplang.catalogs.spi.CatalogRegistrar
import ai.deepsense.deeplang.actionobjects._
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actionobjects.report.Report
import ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators._
import ai.deepsense.deeplang.actionobjects.spark.wrappers.evaluators._
import ai.deepsense.deeplang.actionobjects.spark.wrappers.models._
import ai.deepsense.deeplang.actionobjects.spark.wrappers.transformers._

/** Class responsible for registering the default built-in set of Seahorse operations. */
class StandardActionObjects extends CatalogRegistrant {

  override def register(registrar: CatalogRegistrar): Unit = {
    registrar.registerOperable[DataFrame]()
    registrar.registerOperable[Report]()
    registrar.registerOperable[MetricValue]()
    registrar.registerOperable[ColumnsFilterer]()
    registrar.registerOperable[RowsFilterer]()
    registrar.registerOperable[MissingValuesHandler]()
    registrar.registerOperable[Projector]()
    registrar.registerOperable[DatetimeComposer]()
    registrar.registerOperable[DatetimeDecomposer]()
    registrar.registerOperable[SqlTransformer]()
    registrar.registerOperable[SqlColumnTransformer]()
    registrar.registerOperable[PythonTransformer]()
    registrar.registerOperable[PythonColumnTransformer]()
    registrar.registerOperable[RTransformer]()
    registrar.registerOperable[RColumnTransformer]()
    registrar.registerOperable[TypeConverter]()
    registrar.registerOperable[CustomTransformer]()
    registrar.registerOperable[GetFromVectorTransformer]()
    registrar.registerOperable[SortTransformer]()

    // wrapped Spark ML estimators & models
    registrar.registerOperable[LogisticRegression]()
    registrar.registerOperable[LogisticRegressionModel]()
    registrar.registerOperable[NaiveBayes]()
    registrar.registerOperable[NaiveBayesModel]()
    registrar.registerOperable[AFTSurvivalRegression]()
    registrar.registerOperable[AFTSurvivalRegressionModel]()
    registrar.registerOperable[ALS]()
    registrar.registerOperable[ALSModel]()
    registrar.registerOperable[KMeans]()
    registrar.registerOperable[KMeansModel]()
    registrar.registerOperable[LDA]()
    registrar.registerOperable[LDAModel]()
    registrar.registerOperable[GBTRegression]()
    registrar.registerOperable[GBTRegressionModel]()
    registrar.registerOperable[IsotonicRegression]()
    registrar.registerOperable[IsotonicRegressionModel]()
    registrar.registerOperable[LinearRegression]()
    registrar.registerOperable[LinearRegressionModel]()
    registrar.registerOperable[RandomForestRegression]()
    registrar.registerOperable[RandomForestRegressionModel]()
    registrar.registerOperable[DecisionTreeRegression]()
    registrar.registerOperable[DecisionTreeRegressionModel]()
    registrar.registerOperable[PCAEstimator]()
    registrar.registerOperable[PCAModel]()
    registrar.registerOperable[StandardScalerEstimator]()
    registrar.registerOperable[StandardScalerModel]()
    registrar.registerOperable[MinMaxScalerEstimator]()
    registrar.registerOperable[MinMaxScalerModel]()
    registrar.registerOperable[VectorIndexerEstimator]()
    registrar.registerOperable[VectorIndexerModel]()
    registrar.registerOperable[StringIndexerEstimator]()
    registrar.registerOperable[MultiColumnStringIndexerModel]()
    registrar.registerOperable[SingleColumnStringIndexerModel]()
    registrar.registerOperable[Word2VecEstimator]()
    registrar.registerOperable[Word2VecModel]()
    registrar.registerOperable[CountVectorizerEstimator]()
    registrar.registerOperable[CountVectorizerModel]()
    registrar.registerOperable[IDFEstimator]()
    registrar.registerOperable[IDFModel]()
    registrar.registerOperable[GBTClassifier]()
    registrar.registerOperable[GBTClassificationModel]()
    registrar.registerOperable[RandomForestClassifier]()
    registrar.registerOperable[RandomForestClassificationModel]()
    registrar.registerOperable[DecisionTreeClassifier]()
    registrar.registerOperable[DecisionTreeClassificationModel]()
    registrar.registerOperable[MultilayerPerceptronClassifier]()
    registrar.registerOperable[MultilayerPerceptronClassifierModel]()
    registrar.registerOperable[QuantileDiscretizerEstimator]()
    registrar.registerOperable[QuantileDiscretizerModel]()
    registrar.registerOperable[ChiSqSelectorEstimator]()
    registrar.registerOperable[ChiSqSelectorModel]()

    // wrapped Spark transformers
    registrar.registerOperable[Binarizer]()
    registrar.registerOperable[DiscreteCosineTransformer]()
    registrar.registerOperable[NGramTransformer]()
    registrar.registerOperable[Normalizer]()
    registrar.registerOperable[OneHotEncoder]()
    registrar.registerOperable[PolynomialExpander]()
    registrar.registerOperable[RegexTokenizer]()
    registrar.registerOperable[StopWordsRemover]()
    registrar.registerOperable[StringTokenizer]()
    registrar.registerOperable[VectorAssembler]()
    registrar.registerOperable[HashingTFTransformer]()
    registrar.registerOperable[PythonEvaluator]()
    registrar.registerOperable[REvaluator]()

    // wrapped Spark evaluators
    registrar.registerOperable[BinaryClassificationEvaluator]()
    registrar.registerOperable[MulticlassClassificationEvaluator]()
    registrar.registerOperable[RegressionEvaluator]()
  }

}
