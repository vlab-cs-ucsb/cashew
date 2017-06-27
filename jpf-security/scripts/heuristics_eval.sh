#!/bin/bash

# Kasper

#Example: ./test_heuristics.sh inputfile.jpf 2 10 1 3
# where 2 is the minimum input size, 10, is the maximum input size, 1 is the increment in the input size, and 3 is input size at which the annotated CFG was geneated

JPF_CMD="java -jar /home/luckow/code/isstac-workspace/jpf-core/build/RunJPF.jar +native_classpath=/home/luckow/projects/isstac/jpf-symbc/lib/com.microsoft.z3.jar"

INPUT="${1}_tmp"

MIN_INPUT_SIZE=$2
MAX_INPUT_SIZE=$3
INCREMENT_INPUT_SIZE=$4
CFG_SIZE=$5

for i in `seq $MIN_INPUT_SIZE $INCREMENT_INPUT_SIZE $MAX_INPUT_SIZE`;
do
echo $i
	sed -e "s/target\.args=[0-9]*/target\.args=$i/" $1 > $INPUT
	printf "\n\n\nreport.console.heuristics.cfginputsize=$CFG_SIZE" >> $INPUT
	printf "\nreport.console.class=tree.cf.cfg.HeuristicResultsPublisher" >> $INPUT
	printf "\nreport.console.heuristics.resultsdir=\${jpf-security}/heuristics_results" >> $INPUT
	$JPF_CMD $INPUT
done    

#rm $INPUT
