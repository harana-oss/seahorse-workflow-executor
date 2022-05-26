package io.deepsense

import io.deepsense.deeplang.DOperation
import io.deepsense.deeplang.documentation.OperationDocumentation

package object docgen {

  type DocumentedOperation = DOperation with OperationDocumentation

}
