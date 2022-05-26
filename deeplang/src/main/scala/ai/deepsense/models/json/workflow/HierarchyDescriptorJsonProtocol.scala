package ai.deepsense.models.json.workflow

import spray.httpx.SprayJsonSupport
import spray.json._

import ai.deepsense.deeplang.catalogs.doperable.ClassDescriptor
import ai.deepsense.deeplang.catalogs.doperable.HierarchyDescriptor
import ai.deepsense.deeplang.catalogs.doperable.TraitDescriptor

trait HierarchyDescriptorJsonProtocol extends DefaultJsonProtocol with SprayJsonSupport with NullOptions {

  implicit val traitDescriptorFormat = jsonFormat2(TraitDescriptor)

  implicit val classDescriptorFormat = jsonFormat3(ClassDescriptor)

  implicit val hierarchyDescriptorFormat = jsonFormat2(HierarchyDescriptor)

}

object HierarchyDescriptorJsonProtocol extends HierarchyDescriptorJsonProtocol
