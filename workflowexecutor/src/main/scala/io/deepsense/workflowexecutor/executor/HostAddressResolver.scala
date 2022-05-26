package io.deepsense.workflowexecutor.executor

import java.net.Inet6Address
import java.net.InetAddress
import java.net.NetworkInterface

import scala.util.Try

import io.deepsense.commons.utils.Logging

object HostAddressResolver extends Logging {

  def findHostAddress(): InetAddress = {
    import collection.JavaConversions._
    Try {
      val interfaces = NetworkInterface.getNetworkInterfaces.toIterable
      interfaces.flatMap { n =>
        n.getInetAddresses.toIterable.filter { address =>
          !address.isInstanceOf[Inet6Address] &&
          !address.isLoopbackAddress &&
          !address.isSiteLocalAddress &&
          !address.isLinkLocalAddress &&
          !address.isAnyLocalAddress &&
          !address.isMulticastAddress &&
          !(address.getHostAddress == "255.255.255.255")
        }
      }
    }.get.headOption.getOrElse(InetAddress.getByName("127.0.0.1"))
  }

}
