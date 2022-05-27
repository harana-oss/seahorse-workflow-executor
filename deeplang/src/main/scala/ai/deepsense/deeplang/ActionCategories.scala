package ai.deepsense.deeplang

import ai.deepsense.deeplang.catalogs.SortPriority
import ai.deepsense.deeplang.catalogs.actions.ActionCategory

object ActionCategories {

  import ai.deepsense.commons.models.Id._

  object IO extends ActionCategory("5a39e324-15f4-464c-83a5-2d7fba2858aa", "Input/Output", SortPriority.coreDefault)

  object Action extends ActionCategory("f0202a40-7fe7-4d11-bfda-b11b2199cc12", "Action", IO.priority.nextCore())

  object SetOperation
      extends ActionCategory("6c730c11-9708-4a84-9dbd-3845903f32ac", "Set operation", Action.priority.nextCore())

  object Filtering
      extends ActionCategory("a6114fc2-3144-4828-b350-4232d0d32f91", "Filtering", SetOperation.priority.nextCore())

  object Transformation
      extends ActionCategory(
        "3fcc6ce8-11df-433f-8db3-fa1dcc545ed8",
        "Transformation",
        Filtering.priority.nextCore()
      ) {

    object Custom
        extends ActionCategory(
          "c866200b-9b7e-49d8-8582-d182593629a2",
          "Custom",
          SortPriority.coreDefault,
          Transformation
        )

    object FeatureConversion
        extends ActionCategory(
          "6d84c023-a5f9-4713-8707-1db2c94ccd09",
          "Feature conversion",
          Custom.priority.nextCore(),
          Transformation
        )

    object FeatureScaling
        extends ActionCategory(
          "da9ec3ca-d3ba-4fca-ad22-7298b725d747",
          "Feature scaling",
          FeatureConversion.priority.nextCore(),
          Transformation
        )

    object TextProcessing
        extends ActionCategory(
          "abfc2e76-e2b7-46ad-8fc2-4f80af421432",
          "Text processing",
          FeatureScaling.priority.nextCore(),
          Transformation
        )

  }

  object ML
      extends ActionCategory(
        "c730c11-9708-4a84-9dbd-3845903f32ac",
        "Machine learning",
        Transformation.priority.nextCore()
      ) {

    object HyperOptimization
        extends ActionCategory(
          "5a26f196-4805-4d8e-9a8b-b4c5c4538b0b",
          "Hyper Optimization",
          SortPriority.coreDefault,
          ML
        )

    object Regression
        extends ActionCategory(
          "c80397a8-7840-4bdb-83b3-dc12f1f5bc3c",
          "Regression",
          HyperOptimization.priority.nextCore(),
          ML
        )

    object Classification
        extends ActionCategory(
          "ff13cbbd-f4ec-4df3-b0c3-f6fd4b019edf",
          "Classification",
          Regression.priority.nextCore(),
          ML
        )

    object Clustering
        extends ActionCategory(
          "5d6ed17f-7dc5-4b50-954c-8b2bbe6da2fd",
          "Clustering",
          Classification.priority.nextCore(),
          ML
        )

    object FeatureSelection
        extends ActionCategory(
          "e6b28974-d2da-4615-b357-bc6055238cff",
          "Feature selection",
          Clustering.priority.nextCore(),
          ML
        )

    object DimensionalityReduction
        extends ActionCategory(
          "a112511e-5433-4ed2-a675-098a14a63c00",
          "Dimensionality reduction",
          FeatureSelection.priority.nextCore(),
          ML
        )

    object Recommendation
        extends ActionCategory(
          "daf4586c-4107-4aab-bfab-2fe4e1652784",
          "Recommendation",
          DimensionalityReduction.priority.nextCore(),
          ML
        )

    object ModelEvaluation
        extends ActionCategory(
          "b5d34823-3f2c-4a9a-9114-3c126ce8dfb6",
          "Model evaluation",
          Recommendation.priority.nextCore(),
          ML
        )

  }

  object UserDefined
      extends ActionCategory("9a9c8c50-fcc6-44d5-90f1-967ef3295ded", "User defined", ML.priority.nextCore())

  object Other
      extends ActionCategory("57c7a964-0f53-43cb-af6d-b6c0f1f9d9bc", "Other", UserDefined.priority.nextCore())

}
