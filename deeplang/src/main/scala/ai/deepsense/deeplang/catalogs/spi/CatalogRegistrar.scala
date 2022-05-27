/** Copyright 2018 Astraea, Inc.
  *
  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
  * the License. You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
  * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
  * specific language governing permissions and limitations under the License.
  */

package ai.deepsense.deeplang.catalogs.spi

import ai.deepsense.deeplang.catalogs.actionobjects.ActionObjectCatalog
import ai.deepsense.deeplang.catalogs.actions.ActionCategory
import ai.deepsense.deeplang.catalogs.actions.ActionCatalog
import ai.deepsense.deeplang.catalogs.spi.CatalogRegistrar.OperableType
import ai.deepsense.deeplang.catalogs.spi.CatalogRegistrar.OperationFactory
import ai.deepsense.deeplang.catalogs.FlowCatalog
import ai.deepsense.deeplang.catalogs.SortPriority
import ai.deepsense.deeplang.ActionObject
import ai.deepsense.deeplang.Action

import scala.reflect.runtime.universe._

/** Service context interface for enabling CatalogRegistrant-s add their categories, operations, and operables to the
  * system.
  */
trait CatalogRegistrar {

  def registeredCategories: Seq[ActionCategory]

  def registeredOperations: Seq[(ActionCategory, OperationFactory, SortPriority)]

  def registerOperation(category: ActionCategory, factory: OperationFactory, priority: SortPriority): Unit

  def registerOperable[C <: ActionObject: TypeTag](): Unit

}

object CatalogRegistrar {

  type OperationFactory = () => Action

  type OperableType = TypeTag[_ <: ActionObject]

  class DefaultCatalogRegistrar() extends CatalogRegistrar {

    private val operationsCatalog = ActionCatalog()

    private var operables = ActionObjectCatalog()

    override def registeredCategories: Seq[ActionCategory] = operationsCatalog.categoryTree.getCategories

    override def registeredOperations: Seq[(ActionCategory, OperationFactory, SortPriority)] =
      operationsCatalog.registeredOperations

    override def registerOperation(
                                    category: ActionCategory,
                                    factory: OperationFactory,
                                    priority: SortPriority
    ): Unit =
      operationsCatalog.registerDOperation(category, factory, priority)

    override def registerOperable[C <: ActionObject: TypeTag](): Unit =
      operables.register(typeTag[C])

    /** Constructs a DCatalog from the set of registered deeplang components. */
    def catalog: FlowCatalog =
      new FlowCatalog(operationsCatalog.categories, operables, operationsCatalog)

  }

}
