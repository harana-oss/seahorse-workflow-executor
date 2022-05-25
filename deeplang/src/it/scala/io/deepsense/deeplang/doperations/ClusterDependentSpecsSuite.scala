package io.deepsense.deeplang.doperations

import org.scalatest.{BeforeAndAfterAll, Suites}

import io.deepsense.deeplang.StandaloneSparkClusterForTests

class ClusterDependentSpecsSuite extends Suites(new InputOutputSpec) with BeforeAndAfterAll {
  override def beforeAll(): Unit = StandaloneSparkClusterForTests.startDockerizedCluster()
  override def afterAll(): Unit = StandaloneSparkClusterForTests.stopDockerizedCluster()
}
