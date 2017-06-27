#!/bin/bash

rm -f result.txt
for jpf in benchmarks/*.jpf; do
	echo $jpf
	../../bin/run ${jpf} >> result.txt
done
