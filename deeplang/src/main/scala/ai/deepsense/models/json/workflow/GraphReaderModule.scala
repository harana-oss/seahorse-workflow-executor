package ai.deepsense.models.json.workflow

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton

import ai.deepsense.deeplang.catalogs.actions.ActionCatalog
import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphReader

class GraphReaderModule extends AbstractModule {

  override def configure(): Unit = {
    // Done by 'provides' methods.
  }

  @Singleton
  @Provides
  def provideGraphReader(dOperationsCatalog: ActionCatalog): GraphReader =
    new GraphReader(dOperationsCatalog)

}
