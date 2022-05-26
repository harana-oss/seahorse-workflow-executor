package ai.deepsense.deeplang.documentation

import ai.deepsense.commons.utils.Version

/** Represents documentation attributes. */
trait Documentable {

  /** Since Seahorse version. Format: Major.Minor.Patch */
  def since: Version

  def generateDocs: Option[String] = None

}
