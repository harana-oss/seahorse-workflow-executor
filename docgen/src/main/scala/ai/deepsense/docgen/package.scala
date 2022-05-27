package ai.deepsense

import ai.deepsense.deeplang.Action
import ai.deepsense.deeplang.documentation.OperationDocumentation

package object docgen {

  type DocumentedOperation = Action with OperationDocumentation

}
