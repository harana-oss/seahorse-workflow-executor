#!/bin/bash -ex

TASKS="clean scalastyle test:scalastyle test it:compile ds-it"
sbt -DSPARK_VERSION=2.2.0 $TASKS
