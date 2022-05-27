package ai.deepsense.deeplang.actionobjects.spark.wrappers.transformers

import org.apache.spark.ml.feature.Tokenizer

import ai.deepsense.deeplang.actionobjects.SparkTransformerAsMultiColumnTransformer

class StringTokenizer extends SparkTransformerAsMultiColumnTransformer[Tokenizer]
