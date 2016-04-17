#!/bin/bash

./prepare-for-performance-measurements.sh

inputDir="../inputs/"
mcSampleSize=100000

ts=$(date +"%m%d%H%M")
reportFile="reports/report-$ts"
touch "$reportFile"

cd runners/
printf "SEQUENTIAL SECTION\n"
./sequential-measurer-runner.sh "$mcSampleSize" "$inputDir" | tee -a "../$reportFile"
printf "\n"

printf "PARALLEL SECTION\n"
./parallel-measurer-runner.sh 1 "$mcSampleSize" "$inputDir" | tee -a "../$reportFile"
./parallel-measurer-runner.sh 2 "$mcSampleSize" "$inputDir" | tee -a "../$reportFile"
./parallel-measurer-runner.sh 3 "$mcSampleSize" "$inputDir" | tee -a "../$reportFile"
./parallel-measurer-runner.sh 4 "$mcSampleSize" "$inputDir" | tee -a "../$reportFile"
./parallel-measurer-runner.sh 5 "$mcSampleSize" "$inputDir" | tee -a "../$reportFile"
./parallel-measurer-runner.sh 6 "$mcSampleSize" "$inputDir" | tee -a "../$reportFile"
./parallel-measurer-runner.sh 7 "$mcSampleSize" "$inputDir" | tee -a "../$reportFile"
./parallel-measurer-runner.sh 8 "$mcSampleSize" "$inputDir" | tee -a "../$reportFile"
printf "\n"

printf "MPI SECTION\n"
./mpj-measurer-runner.sh 1 "$mcSampleSize" "$inputDir" | tee -a "../$reportFile"
printf "\n"
./mpj-measurer-runner.sh 2 "$mcSampleSize" "$inputDir" | tee -a "../$reportFile"
printf "\n"
./mpj-measurer-runner.sh 3 "$mcSampleSize" "$inputDir" | tee -a "../$reportFile"
printf "\n"
./mpj-measurer-runner.sh 4 "$mcSampleSize" "$inputDir" | tee -a "../$reportFile"
printf "\n"
./mpj-measurer-runner.sh 5 "$mcSampleSize" "$inputDir" | tee -a "../$reportFile"
printf "\n"
./mpj-measurer-runner.sh 6 "$mcSampleSize" "$inputDir" | tee -a "../$reportFile"
printf "\n"
./mpj-measurer-runner.sh 7 "$mcSampleSize" "$inputDir" | tee -a "../$reportFile"
printf "\n"
./mpj-measurer-runner.sh 8 "$mcSampleSize" "$inputDir" | tee -a "../$reportFile"

cd ..