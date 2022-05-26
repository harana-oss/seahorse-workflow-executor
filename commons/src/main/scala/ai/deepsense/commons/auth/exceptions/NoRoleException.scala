package ai.deepsense.commons.auth.exceptions

import ai.deepsense.commons.auth.usercontext.UserContext

case class NoRoleException(userContext: UserContext, expectedRole: String)
    extends AuthException(s"No role $expectedRole in userContext = $userContext")
