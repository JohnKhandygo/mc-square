#!/bin/bash

java -cp ../mc-square-1.0-SNAPSHOT-jar-with-dependencies.jar \
"com.ksp.khandygo.performance.SequentialMCEstimatorRunner" "$1" "$2"