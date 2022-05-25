package io.deepsense.sparkutils.readwritedataframe

import scala.language.reflectiveCalls

// TODO duplicated from io.deepsense.commons.resources.ManagedResource

object ManagedResource {
  def apply[T, Q](c: T {def close(): Unit})(f: (T) => Q): Q = {
    try {
      f(c)
    } finally {
      c.close()
    }
  }
}
