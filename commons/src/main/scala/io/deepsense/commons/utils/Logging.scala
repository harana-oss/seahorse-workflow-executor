package io.deepsense.commons.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory

trait Logging {

  @transient
  protected lazy val logger: Logger = LoggerFactory.getLogger(getClass.getName)

}
