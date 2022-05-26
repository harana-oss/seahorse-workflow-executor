package ai.deepsense.deeplang

object Jenkins {

  def isRunningOnJenkins = userName == "Jenkins" || userName == "jenkins"

  private def userName = System.getProperty("user.name")

}
