package ai.deepsense.commons.auth.exceptions

import ai.deepsense.commons.auth.HasTenantId
import ai.deepsense.commons.auth.usercontext.UserContext

case class ResourceAccessDeniedException(userContext: UserContext, resource: HasTenantId)
    extends AuthException(
      "Unauthorized user tenant " +
        s"${userContext.tenantId} != resource tenant ${resource.tenantId}"
    )
