package ai.deepsense.deeplang.doperations

import org.scalatest.BeforeAndAfterAll
import org.scalatest.Suites

import ai.deepsense.deeplang.StandaloneSparkClusterForTests

class ClusterDependentSpecsSuite extends Suites(new InputOutputSpec) with BeforeAndAfterAll {

  override def beforeAll(): Unit = StandaloneSparkClusterForTests.startDockerizedCluster()

  override def afterAll(): Unit = StandaloneSparkClusterForTests.stopDockerizedCluster()

}
