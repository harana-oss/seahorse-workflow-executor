package ai.deepsense.commons.auth

import ai.deepsense.commons.auth.exceptions.ResourceAccessDeniedException
import ai.deepsense.commons.auth.usercontext.UserContext

/** Describes an object with an owner. */
trait Ownable extends HasTenantId {

  /** Checks if this object is owned by user defined in UserContext.
    * @param owner
    *   Owner's UserContext.
    * @return
    *   The object when it is owned by the user defined in the UserContext. Otherwise throws
    *   [[ai.deepsense.commons.auth.exceptions.ResourceAccessDeniedException ResourceAccessDeniedException]].
    */
  def assureOwnedBy(owner: UserContext): this.type = {
    if (isOwnedBy(owner))
      this
    else
      throw new ResourceAccessDeniedException(owner, this)
  }

  /** Checks if this object is owned by user defined in UserContext.
    * @param owner
    *   Owner's UserContext.
    * @return
    *   True if the object is owned by the user defined in the UserContext. Otherwise false.
    */
  def isOwnedBy(owner: UserContext): Boolean = this.tenantMatches(owner)

}
