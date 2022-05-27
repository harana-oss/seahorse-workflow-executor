package ai.deepsense.models.json.workflow

trait DeepLangJsonProtocol
    extends ActionCategoryNodeJsonProtocol
    with ActionDescriptorJsonProtocol
    with HierarchyDescriptorJsonProtocol
    with ActionEnvelopesJsonProtocol

object DeepLangJsonProtocol extends DeepLangJsonProtocol
