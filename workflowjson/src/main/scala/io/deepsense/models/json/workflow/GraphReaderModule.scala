package io.deepsense.models.json.workflow

import com.google.inject.{AbstractModule, Provides, Singleton}

import io.deepsense.deeplang.catalogs.doperations.DOperationsCatalog
import io.deepsense.models.json.graph.GraphJsonProtocol.GraphReader

class GraphReaderModule extends AbstractModule {
  override def configure(): Unit = {
    // Done by 'provides' methods.
  }

  @Singleton
  @Provides
  def provideGraphReader(dOperationsCatalog: DOperationsCatalog): GraphReader = {
    new GraphReader(dOperationsCatalog)
  }
}
