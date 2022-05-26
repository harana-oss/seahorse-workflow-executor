package ai.deepsense.deeplang.doperables.spark.wrappers.transformers

import org.apache.spark.ml.feature.Tokenizer

import ai.deepsense.deeplang.doperables.SparkTransformerAsMultiColumnTransformer

class StringTokenizer extends SparkTransformerAsMultiColumnTransformer[Tokenizer]
