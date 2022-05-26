package io.deepsense.deeplang.doperations.spark.wrappers.estimators

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.documentation.SparkOperationDocumentation
import io.deepsense.deeplang.doperables.spark.wrappers.estimators.LDA
import io.deepsense.deeplang.doperations.EstimatorAsFactory

class CreateLDA extends EstimatorAsFactory[LDA] with SparkOperationDocumentation {

  override val id: Id = "a385f8fe-c64e-4d71-870a-9d5048747a3c"

  override val name: String = "LDA"

  override val description: String =
    """Latent Dirichlet Allocation (LDA), a topic model designed for text documents. LDA is given a
      |collection of documents as input data, via the `features column` parameter. Each document is
      |specified as a vector of length equal to the vocabulary size, where each entry is the count
      |for the corresponding term (word) in the document. Feature transformers such as Tokenize and
      |Count Vectorizer can be useful for converting text to word count vectors.""".stripMargin

  override protected[this] val docsGuideLocation =
    Some("ml-clustering.html#latent-dirichlet-allocation-lda")

  override val since: Version = Version(1, 1, 0)

}
