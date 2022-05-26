package ai.deepsense

import ai.deepsense.deeplang.DOperation
import ai.deepsense.deeplang.documentation.OperationDocumentation

package object docgen {

  type DocumentedOperation = DOperation with OperationDocumentation

}
