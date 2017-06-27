#!/bin/bash

# Author: Kasper Luckow

# This is a script for batch processing jpf files from a directory specified by the first argument,
# outputting all the results to the second argument supplied this script.
# The reason for not doing this in Java, is that we actually *want* to destroy the java process after each
# jpf file has been processed. The reason is that there currently seems to be a memory leak in spf/z3---*sigh*---and, thus,
# after ~12 hours of experiments, the 4gb application server will start swapping thus ruining the experiments.


INPUT_DIR=$1
OUTPUT_DIR=$2
WORKSPACE_ROOT=..


for file in ${INPUT_DIR}/*
 do
     # Only process files with .jpf extension
     if [[ $file == *.jpf ]]; then
    echo "processing file $file"
        java -classpath build/jpf-security.jar:lib/*:${WORKSPACE_ROOT}/jpf-core/build/jpf.jar:${WORKSPACE_ROOT}/jpf-symbc/build/jpf-symbc.jar:${WORKSPACE_ROOT}/jpf-symbc/build/jpf-symbc-classes.jar:${WORKSPACE_ROOT}/jpf-symbc/lib/* edu.cmu.sv.isstac.sampling.batch.BatchProcessor $file ${OUTPUT_DIR}
     fi
 done
