package io.deepsense.commons.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object LoggerForCallerClass {

  def apply(): Logger = {
    // We use the third stack element; second is this method, first is .getStackTrace()
    val myCaller = Thread.currentThread().getStackTrace()(2)
    assert(myCaller.getMethodName() == "<init>", "Must be called in constructor")
    LoggerFactory.getLogger(myCaller.getClassName)
  }

}
