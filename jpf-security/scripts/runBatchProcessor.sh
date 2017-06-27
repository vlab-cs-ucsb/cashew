#!/bin/bash

WORKSPACE_ROOT=../..

java -classpath build/jpf-security.jar:lib/*:${WORKSPACE_ROOT}/jpf-core/build/jpf.jar:${WORKSPACE_ROOT}/jpf-symbc/build/jpf-symbc.jar:${WORKSPACE_ROOT}/jpf-symbc/build/jpf-symbc-classes.jar:${WORKSPACE_ROOT}/jpf-symbc/lib/* edu.cmu.sv.isstac.sampling.batch.BatchProcessor $file ${OUTPUT_DIR}

