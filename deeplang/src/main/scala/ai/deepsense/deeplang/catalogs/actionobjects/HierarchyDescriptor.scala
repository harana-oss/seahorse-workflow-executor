package ai.deepsense.deeplang.catalogs.actionobjects

/** Describes hierarchy of traits and classes.
  * @param traits
  *   descriptors of all registered traits, mapped using descriptor name.
  * @param classes
  *   descriptors of all registered classes, mapped using descriptor name.
  */
case class HierarchyDescriptor(traits: Map[String, TraitDescriptor], classes: Map[String, ClassDescriptor])
